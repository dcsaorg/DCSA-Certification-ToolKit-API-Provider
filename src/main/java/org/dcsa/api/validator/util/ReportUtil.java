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
    private static boolean assertionErrorContinue;

    private static OsType osType;
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

    public static void setOsType(OsType osType) {
        ReportUtil.osType = osType;
        if(osType == OsType.WINDOWS ){
            NEWMAN_TICK = "√";
        }else if(osType == OsType.LINUX ){
            NEWMAN_TICK = "✓";
        }else if(osType == OsType.DOCKER ){
            NEWMAN_TICK = "���";
        }
    }
    public static String getReportPath(String filePrefix, String fileExtension){
        if(htmlReportPath == null){
            populateReportPath(filePrefix, fileExtension);
        }
        return htmlReportPath;
        }

    private static void populateReportPath(String filePrefix, String fileExtension){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy__hh-mm-ss");
        htmlReportName = AppProperty.REPORT_NAME.replaceAll(" ", "-")  + "-" + filePrefix +
                    "-" +dateFormat.format(Calendar.getInstance().getTime()) + fileExtension;
        htmlReportPath = System.getProperty("user.dir")+ File.separator+ AppProperty.REPORT_DIRECTORY +File.separator+ htmlReportName;
    }
    public static String getReports(){
        if(htmlReportPath != null){
            return htmlReportPath;
        }else{
            return "";
        }
    }
    public static String modifyTestDetails(String testDetails){
        if(osType == OsType.WINDOWS ){
            return testDetails.replace("<h2>404 Not found</h2>", "404 Not found");
        }else if(osType == OsType.LINUX || osType == OsType.DOCKER){
            return testDetails.replace("<h1>HTTP Status 404 – Not Found</h1>", "HTTP Status 404 – Not Found");
        }
        return "";
    }
    public static void fillHtmlReportModel(String line){
        if(line.contains(".json")){ // ignore
            // ignore when it has .json
        }else if(line.contains("AssertionError")){ // test fail reason
            line = line.substring(5);
            String finalLine = line;
            htmlReportModels.forEach(item ->{
                if(finalLine.contains(item.getRequirementId())){
                    item.getFailureReason().append(finalLine.trim()).append(HTML_NEWLINE);
                    assertionErrorContinue = true;
                }
            });
        }else if(assertionErrorContinue){ // test fail reason continue
            String finalLine = line;
            htmlReportModels.forEach(item ->{
                if(item.getFailureReason().toString().contains(item.getRequirementId())){
                    item.getFailureReason().append(HTML_BOLD_START+finalLine.trim()+HTML_BOLD_END);
                    assertionErrorContinue = false;
                }
            });
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
            currentHtmlReportModel.getTestDetails().append(tokens[0])
                                    .append(HTML_NEWLINE).append(tokens[1]);
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
