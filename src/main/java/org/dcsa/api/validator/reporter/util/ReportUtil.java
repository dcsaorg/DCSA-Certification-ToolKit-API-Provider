package org.dcsa.api.validator.reporter.util;

import org.dcsa.api.validator.webservice.init.AppProperty;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportUtil {

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
}
