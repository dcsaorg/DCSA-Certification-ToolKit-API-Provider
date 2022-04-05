package org.dcsa.api.validator.reporter.util;

import org.dcsa.api.validator.reporter.report.PropertyLoader;
import org.dcsa.api.validator.webservice.init.AppProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportUtil {
    private static final String REPORT_AUTHOR_KEY = "app.report_author";
    private static final String REPORT_COMPANY_KEY = "app.report_company";
    private static final String REPORT_DIRECTORY_KEY = "app.report_directory";
    private static final String REPORT_NAME_KEY = "app.report_name";
    private static final String REPORT_TITLE_KEY = "app.report_title";
    private static final String REPORT_THEME_KEY = "app.report_theme";
    private static final String REPORT_TIME_FORMAT_KEY = "app.report_time_format";
    private static final String REPORT_TIMELINE_KEY = "app.report_timeline";

    public static String htmlReportPath;
    public static String htmlReportName;
    public static String excelReportPath;
    public static String excelReportName;
    public static final String HTML_EXTENSION = ".html";
    public static final String EXCEL_EXTENSION = ".xlsx";
    public static final String HTML = "html";
    public static final String EXCEL = "excel";

    public static String getReportPath(String filePrefix, String fileExtension, String reportType){
        if(htmlReportPath == null || excelReportPath == null){
            populateReportPath(filePrefix, fileExtension, reportType);
        }
        if(reportType.equals(HTML)){
            return htmlReportPath;
        }else if(reportType.equals(EXCEL)){
            return excelReportPath;
        }else {
            return "";
        }
    }

    private static void populateReportPath(String filePrefix, String fileExtension, String reportType){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy__hh-mm-ss");
        if(AppProperty.REPORT_DIRECTORY == null){
            AppProperty.REPORT_AUTHOR = PropertyLoader.getProperty(REPORT_AUTHOR_KEY);
            AppProperty.REPORT_COMPANY = PropertyLoader.getProperty(REPORT_COMPANY_KEY);
            AppProperty.REPORT_DIRECTORY = PropertyLoader.getProperty(REPORT_DIRECTORY_KEY);
            AppProperty.REPORT_NAME = PropertyLoader.getProperty(REPORT_NAME_KEY);
            AppProperty.REPORT_TITLE = PropertyLoader.getProperty(REPORT_TITLE_KEY);
            AppProperty.REPORT_THEME = PropertyLoader.getProperty(REPORT_THEME_KEY);
            AppProperty.REPORT_TIME_FORMAT = PropertyLoader.getProperty(REPORT_TIME_FORMAT_KEY);
            AppProperty.REPORT_TIMELINE = PropertyLoader.getProperty(REPORT_TIMELINE_KEY);
        }
        if(reportType.equals(HTML)){
            htmlReportName = AppProperty.REPORT_NAME.replaceAll(" ", "-")  + "-" + filePrefix +
                    "-" +dateFormat.format(Calendar.getInstance().getTime()) + fileExtension;
            htmlReportPath = System.getProperty("user.dir")+"/"+ AppProperty.REPORT_DIRECTORY + "/"+ htmlReportName;
        }else if(reportType.equals(EXCEL)){
            excelReportName = AppProperty.REPORT_NAME.replaceAll(" ", "-")  + "-" + filePrefix +
                    "-" +dateFormat.format(Calendar.getInstance().getTime()) + fileExtension;
            excelReportPath = System.getProperty("user.dir")+"/"+ AppProperty.REPORT_DIRECTORY + "/" + excelReportName;
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
