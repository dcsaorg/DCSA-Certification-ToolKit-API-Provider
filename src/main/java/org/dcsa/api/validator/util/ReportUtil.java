package org.dcsa.api.validator.util;

import org.dcsa.api.validator.model.HtmlReportModel;
import org.dcsa.api.validator.model.enums.OsType;
import org.dcsa.api.validator.model.enums.ValidationRequirementId;
import org.dcsa.api.validator.reporter.report.ExtentReportManager;
import org.dcsa.api.validator.reporter.report.ExtentReportModifier;
import org.dcsa.api.validator.webservice.init.AppProperty;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.dcsa.api.validator.constant.TestStatusCode.*;

public class ReportUtil {
    private static List<HtmlReportModel> htmlReportModels = new ArrayList<>();
    private static OsType osType;
    private static boolean assertionTestContinue = false;
    private static HtmlReportModel currentHtmlReportModel = new HtmlReportModel();
    public static String htmlReportPath;
    public static String htmlReportName;
    public static final String REQUEST_RESPONSE_SPLIT = "##";
    public static final String REQUEST_REPLACE = "[INFO] Request:";
    public static final String REQUEST_REPLACE_BOLD = "<b>[INFO] Request:</b>";

    public static final String RESPONSE_REPLACE = "[INFO] Response:";
    public static final String RESPONSE_REPLACE_BOLD = "<b>[[INFO] Response:</b>";
    public static final String HTML_NEWLINE = "</br>";
    public static final String HTML_BOLD_START = "<b>";
    public static final String HTML_BOLD_END = "</b>";
    public static String NEWMAN_TICK = "√";

    private static final String TNT_3_0_REQUIREMENT_ID = "TNT.3.0-REQUIREMENT-ID";
    private static final String NEWMAN_TEST_TITLE = "└";
    private static final String REQUEST_INFO = "[INFO] Request:";
    private static final String RESPONSE_INFO = "[INFO] Response:";
    public static void setOsType(OsType osType) {
        ReportUtil.osType = osType;
        if (osType == OsType.WINDOWS) {
            NEWMAN_TICK = "√";
        } else if (osType == OsType.LINUX) {
            NEWMAN_TICK = "✓";
        } else if (osType == OsType.DOCKER) {
            NEWMAN_TICK = "���";
        }
    }
    public static String getReportPath(String filePrefix, String fileExtension) {
        if (htmlReportPath == null) {
            populateReportPath(filePrefix, fileExtension);
        }
        return htmlReportPath;
    }

    private static void populateReportPath(String filePrefix, String fileExtension) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy__hh-mm-ss");
        htmlReportName = AppProperty.REPORT_NAME.replaceAll(" ", "-") + "-" + filePrefix +
                "-" + dateFormat.format(Calendar.getInstance().getTime()) + fileExtension;
        htmlReportPath = System.getProperty("user.dir") + File.separator + AppProperty.REPORT_DIRECTORY + File.separator + htmlReportName;
    }

    public static String getReports() {
        if (htmlReportPath != null) {
            return htmlReportPath;
        } else {
            return "";
        }
    }

    public static String modifyTestDetails(String testDetails) {
        if (osType == OsType.WINDOWS) {
            return testDetails.replace("<h2>404 Not found</h2>", "404 Not found");
        } else if (osType == OsType.LINUX || osType == OsType.DOCKER) {
            return testDetails.replace("<h1>HTTP Status 404 – Not Found</h1>", "HTTP Status 404 – Not Found");
        }
        return "";
    }
    public static void fillHtmlReportTNT(String line) {
        if (line.contains(".json")) { // ignore
            // ignore when it has .json
        } else if (line.contains("AssertionError") && !assertionTestContinue) { // set test fail line 1
            setAssertionLine(line);
            assertionTestContinue = true;
        } else if(assertionTestContinue) { // set test fail continue
            if(line.contains("AssertionError")){
                setAssertionLine(line);
            }else{
                setAssertionNextLine(line);
            }
        }else if (line.contains(REQUEST_INFO)) { // set test details request info
            setRequestInfo(htmlReportModels, line);
        } else if (line.contains(RESPONSE_INFO)) { // test details response
            setResponseInfo(htmlReportModels, line);
            System.out.println("stop to check list");
        }else if (line.contains(NEWMAN_TICK) || line.contains(TNT_3_0_REQUIREMENT_ID) &&
                !line.contains(NEWMAN_TEST_TITLE)) { // pass or fail case
            HtmlReportModel htmlReportModel = new HtmlReportModel();
            if(!line.contains(NEWMAN_TICK)){ // if no tick then it is failure case.
                line = line.substring(5);
                htmlReportModel.setTestStatusCode(FAILED);
            }else{
                line = line.replace(NEWMAN_TICK, "").trim();
                htmlReportModel.setTestStatusCode(PASSED);
            }
            htmlReportModel.setTestName(line);
            String[] tokens = line.split("_");
            htmlReportModel.setRequirementId(tokens[0].trim());
            htmlReportModel.setRequirement(tokens[1].trim());
            htmlReportModel.setValidationRequirementID(ValidationRequirementId.getById(tokens[0].trim()));
            if(!htmlReportModels.contains(htmlReportModel)){
                htmlReportModels.add(htmlReportModel);
            }
        }else if(line.contains("Error")){
            System.out.println("TNT is not running. Pls run TNT and try again.");
        }
    }
    private static void setAssertionLine(String line){
        line = line.substring(5);
        line = line.replace("AssertionError", "").trim();
        String finalLine = line;
        htmlReportModels.forEach(item -> {
            if(finalLine.equals(item.getTestName())){
                item.getFailureReason().append(finalLine.trim());
                assertionTestContinue = true;
            }
        });
    }
    private static void setAssertionNextLine(String line){
        String finalLine = line.replace(".", "").trim();
        if(!finalLine.isBlank()){
            htmlReportModels.forEach(item -> {
                if(isTestFailureAndTestNameSame(item.getFailureReason().toString(), item.getTestName())){
                    item.getFailureReason().append(HTML_NEWLINE).append(finalLine);
                }
            });
        }
    }

    private static void setRequestInfo(List<HtmlReportModel> htmlReportModels, String requestInfo) {
        requestInfo = requestInfo.replace(NEWMAN_TICK, "").trim();
        String[] tokens = requestInfo.split(REQUEST_RESPONSE_SPLIT);
        tokens[0] = tokens[0].replace(REQUEST_REPLACE, REQUEST_REPLACE_BOLD);
        htmlReportModels.forEach(item -> {
                if (item.getTestDetails().toString().isBlank()) {
                    item.getTestDetails().append(tokens[0])
                            .append(HTML_NEWLINE).append(tokens[1]).append(HTML_NEWLINE);
                }
            });
    }
    private static void setResponseInfo(List<HtmlReportModel> htmlReportModels, String responseInfo) {
        responseInfo = responseInfo.replace(NEWMAN_TICK, "").trim();
        String[] tokens = responseInfo.split(REQUEST_RESPONSE_SPLIT);
        tokens[0] = tokens[0].replace(RESPONSE_REPLACE, RESPONSE_REPLACE_BOLD);

        htmlReportModels.forEach(item -> {
                if (!item.isDetailsFilled()) {
                    if (tokens.length > 1) {
                        item.getTestDetails().append(tokens[0])
                                .append(HTML_NEWLINE).append(tokens[1]);
                    } else {
                        item.getTestDetails().append(tokens[0])
                                .append(HTML_NEWLINE).append("No response found from the server");
                    }
                    item.setDetailsFilled(true);
                }
            });
    }
    private static boolean isTestFailureAndTestNameSame(String source, String subItem){
        if(!source.isBlank()){
            String[] tokens1 = source.split(" ");
            String[] tokens2 = subItem.split(" ");
            if(tokens1.length > 1 && tokens2.length > 1){
                if (tokens1[0].equals(tokens2[0])) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void fillHtmlReportModel(String line){
        if(line.contains(".json")){ // ignore
            // ignore when it has .json
        }else if(line.contains("AssertionError")){ // test fail reason
            line = line.substring(5);
            String finalLine = line;
            htmlReportModels.forEach(item ->{
                if(item.getRequirementId() == null ){
                    return;
                }
                if(finalLine.contains(item.getRequirementId())){
                    item.getFailureReason().append(finalLine.trim()).append(HTML_NEWLINE);
                }
            });
        /*}else if(assertionContinueLine1){ // test fail reason continue
            String finalLine = line;
            htmlReportModels.forEach(item ->{
                if(item.getRequirementId() == null ){
                    return;
                }
                if(item.getFailureReason().toString().contains(item.getRequirementId())){
                    item.getFailureReason().append(HTML_BOLD_START+finalLine.trim()+HTML_BOLD_END);
                    assertionContinueLine1 = false;
                }
            });*/
        } else if(line.contains("[INFO] Request:")){ // test details request
            line = line.replace(NEWMAN_TICK, "").trim();
            String[] tokens = line.split(REQUEST_RESPONSE_SPLIT);
            tokens[0] = tokens[0].replace(REQUEST_REPLACE, REQUEST_REPLACE_BOLD);
            currentHtmlReportModel.getTestDetails().append(tokens[0])
                                    .append(HTML_NEWLINE).append(tokens[1]).append(HTML_NEWLINE);
        }else if(line.contains("[INFO] Response:")){ // test details response
            line = line.replace(NEWMAN_TICK, "").trim();
            String[] tokens = line.split(REQUEST_RESPONSE_SPLIT);
            tokens[0] = tokens[0].replace(RESPONSE_REPLACE, RESPONSE_REPLACE_BOLD);
            if(tokens.length > 1) {
                currentHtmlReportModel.getTestDetails().append(tokens[0])
                        .append(HTML_NEWLINE).append(tokens[1]);
            }else{
                currentHtmlReportModel.getTestDetails().append(tokens[0])
                        .append(HTML_NEWLINE).append("No response found from the server");
            }

            htmlReportModels.add(currentHtmlReportModel);
            currentHtmlReportModel = new HtmlReportModel();
        } else if(line.contains(NEWMAN_TICK) && line.contains(("_"))){ // pass case
            line = line.replace(NEWMAN_TICK, "").trim();
            currentHtmlReportModel.setTestName(line);
            String[] tokens = line.split("_");
            currentHtmlReportModel.setRequirementId(tokens[0].trim());
            currentHtmlReportModel.setRequirement(tokens[1].trim());
            currentHtmlReportModel.setTestStatusCode(PASSED);
            currentHtmlReportModel.setValidationRequirementID(ValidationRequirementId.getById(tokens[0].trim()));
        }else if(!line.contains(NEWMAN_TICK) && line.contains(("_"))){ // failed case
            line = line.substring(4);
            currentHtmlReportModel.setTestName(line);
            String[] tokens = line.split("_");
            currentHtmlReportModel.setRequirementId(tokens[0].trim());
            currentHtmlReportModel.setRequirement(tokens[1].trim());
            currentHtmlReportModel.setTestStatusCode(FAILED);
            currentHtmlReportModel.setValidationRequirementID(ValidationRequirementId.getById(tokens[0].trim()));
        }
    }
    public static void writeReport(){
        htmlReportModels.forEach( htmlReportModel -> {
            ExtentReportManager.writeExtentTestReport(htmlReportModel, "TNT");
        });
        addLogo();
    }
    private static void addLogo() {
        if(ReportUtil.htmlReportPath != null){
            ExtentReportModifier.modifyFile(ReportUtil.htmlReportPath);
        }
    }


}
