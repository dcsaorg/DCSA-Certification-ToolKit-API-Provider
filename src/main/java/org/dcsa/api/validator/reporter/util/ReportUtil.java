package org.dcsa.api.validator.reporter.util;

import org.dcsa.api.validator.webservice.init.AppProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportUtil {

    public static String htmlReportPath;
    public static String htmlReportName;
    public static String excelReportPath;
    public static String excelReportName;
    public static final String HTML_EXTENSION = ".html";
    public static final String EXCEL_EXTENSION = ".xlsx";
    public static final String HTML = "html";
    public static final String EXCEL = "excel";

    public static String getReportPath(String filePrefix, String fileExtension, String reportType){
        DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy__hh-mm-ss");
        if(reportType.equals(HTML)){
            htmlReportName = AppProperty.REPORT_NAME.replaceAll(" ", "-")  + "-" + filePrefix +
                    "-" +dateFormat.format(Calendar.getInstance().getTime()) + fileExtension;
            htmlReportPath = System.getProperty("user.dir")+"/"+ AppProperty.REPORT_DIRECTORY + "/"+ htmlReportName;
            return htmlReportPath;
        }else if(reportType.equals(EXCEL)){
            excelReportName = AppProperty.REPORT_NAME.replaceAll(" ", "-")  + "-" + filePrefix +
                    "-" +dateFormat.format(Calendar.getInstance().getTime()) + fileExtension;
            excelReportPath = System.getProperty("user.dir")+"/"+ AppProperty.REPORT_DIRECTORY + "/" + excelReportName;
            return excelReportPath;
        }else {
            return "";
        }
    }

    public static List<String> getReports(){
        List<String> reports = new ArrayList<>();
        if(excelReportPath != null){
            reports.add(excelReportPath);
        }
        if(htmlReportPath != null){
            reports.add(htmlReportPath);
        }
        return reports;
    }
}
