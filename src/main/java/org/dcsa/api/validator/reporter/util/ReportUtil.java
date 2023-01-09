package org.dcsa.api.validator.reporter.util;

import org.dcsa.api.validator.model.HtmlReportModel;
import org.dcsa.api.validator.model.enums.ValidationRequirementId;
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
    private static HtmlReportModel currentHtmlReportModel = new HtmlReportModel();
    public static String htmlReportPath;
    public static String htmlReportName;
    public static final String HTML1 = "html";

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
        return testDetails.replace("<h2>404 Not found</h2>", "404 Not found");
    }
    public static void fillHtmlReportModel(String line){
        if(line.contains(".json")){ // ignore
            // ignore when it has .json
        }else if(line.contains("AssertionError")){ // test fail reason
            line = line.substring(5);
            String finalLine = line;
            htmlReportModels.forEach(item ->{
                if(finalLine.contains(item.getRequirementId())){
                    item.getFailureReason().append(finalLine.trim()).append("\n");
                    assertionErrorContinue = true;
                }
            });
        }else if(assertionErrorContinue){ // test fail reason continue
            String finalLine = line;
            htmlReportModels.forEach(item ->{
                if(item.getFailureReason().toString().contains(item.getRequirementId())){
                    item.getFailureReason().append(finalLine.trim());
                    assertionErrorContinue = false;
                }
            });
        } else if(line.contains("[INFO] Request:")){ // test details request
            line = line.replace("  √  ", "");
            String[] tokens = line.split(":</b>");
            currentHtmlReportModel.getTestDetails().append(tokens[0]+":</b>")
                    .append("<br>").append(tokens[1]).append("<br>");
        }else if(line.contains("[INFO] Response:")){ // test details response
            line = line.replace("  √  ", "");
            String[] tokens = line.split(":</b>");
            currentHtmlReportModel.getTestDetails().append(tokens[0]+":</b>")
                    .append("<br>").append(tokens[1]);
            htmlReportModels.add(currentHtmlReportModel);
            currentHtmlReportModel = new HtmlReportModel();
        }
        else if(line.contains("√") && line.contains(("_"))){ // pass case
            line = line.replace("√", "");
            currentHtmlReportModel.setTestName(line);
            String[] tokens = line.split("_");
            currentHtmlReportModel.setRequirementId(tokens[0].trim());
            currentHtmlReportModel.setRequirement(tokens[1].trim());
            currentHtmlReportModel.setTestStatusCode(PASSED);
            currentHtmlReportModel.setValidationRequirementID(ValidationRequirementId.getById(tokens[0].trim()));
        }else if(!line.contains("√") && line.contains(("_"))){ // failed case
            line = line.substring(4);
            currentHtmlReportModel.setTestName(line);
            String[] tokens = line.split("_");
            currentHtmlReportModel.setRequirementId(tokens[0].trim());
            currentHtmlReportModel.setRequirement(tokens[1].trim());
            currentHtmlReportModel.setTestStatusCode(FAILED);
            currentHtmlReportModel.setValidationRequirementID(ValidationRequirementId.getById(tokens[0].trim()));
        }
    }
    public static List<HtmlReportModel> getHtmlReportModels(){
        return htmlReportModels;
    }

}
