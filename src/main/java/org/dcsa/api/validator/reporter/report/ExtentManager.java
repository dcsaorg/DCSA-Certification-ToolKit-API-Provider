package org.dcsa.api.validator.reporter.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.dcsa.api.validator.constant.TestStatusCode;
import org.dcsa.api.validator.model.HtmlReportModel;
import org.dcsa.api.validator.webservice.init.AppProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.Properties;

public class ExtentManager {

    private static ExtentReports extentReports;
    private static ExtentTest extentTest;
    private static String reportPath;
    private static String reportName;
    private static final String COMPANY = "Company";
    private static final String AUTHOR = "Author";
    private static final String OS_NAME = "os.name";
    private static final String USER_COUNTRY = "user.country";
    private static final String USER_LANGUAGE = "user.language";
    private static final String JAVA_RUNTIME = "java.runtime.name";
    private static final String JAVA_VERSION = "java.version";

    public static synchronized  ExtentReports getExtentReports() {
        if(extentReports == null){
            extentReports = new ExtentReports();
            extentReports.attachReporter(getExtentSparkReporter());
            setReportSystemInfo();
        }
        return extentReports;
    }

    private static void setReportSystemInfo(){
        Properties properties = System.getProperties();
        extentReports.setSystemInfo(COMPANY.toUpperCase(), AppProperty.REPORT_COMPANY);
        extentReports.setSystemInfo(AUTHOR.toUpperCase(), AppProperty.REPORT_AUTHOR);
        extentReports.setSystemInfo(OS_NAME.toUpperCase().replaceAll("\\."," "),
                                                                (String)properties.get(OS_NAME));
        extentReports.setSystemInfo(USER_COUNTRY.toUpperCase().replaceAll("\\."," "),
                                                                (String)properties.get(USER_COUNTRY));
        extentReports.setSystemInfo(USER_LANGUAGE.toUpperCase().replaceAll("\\."," "),
                                                                ((String)properties.get(USER_LANGUAGE)).toUpperCase());
        extentReports.setSystemInfo(JAVA_RUNTIME.toUpperCase().replaceAll("\\."," "),
                                                                (String)properties.get(JAVA_RUNTIME));
        extentReports.setSystemInfo(JAVA_VERSION.toUpperCase().replaceAll("\\."," "),
                                                                (String)properties.get(JAVA_VERSION));
    }

    public static void flush(){
        getExtentReports().flush();
    }

    public static ExtentSparkReporter getExtentSparkReporter(){
        DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy__hh-mm-ss");
        reportName = AppProperty.REPORT_NAME +
                    "_" +dateFormat.format(Calendar.getInstance().getTime()) + ".html";
        reportPath = System.getProperty("user.dir")+"/"+
                    AppProperty.REPORT_DIRECTORY+"/" + reportName;
        ExtentSparkReporter reporter = new ExtentSparkReporter( reportPath);

        reporter.config().setReportName(AppProperty.REPORT_NAME);
        reporter.config().setDocumentTitle( AppProperty.REPORT_TITLE);
        reporter.config().setTheme(Objects.equals(AppProperty.REPORT_THEME, Theme.DARK.getName()) ?
                                    Theme.DARK : Theme.STANDARD);
        reporter.config().setTimelineEnabled(Boolean.parseBoolean( AppProperty.REPORT_TIMELINE));
        reporter.config().setTimeStampFormat(AppProperty.REPORT_TIME_FORMAT);
        return reporter;
    }

    public static ExtentTest getExtentTest(String name){
        if(name != null){
            extentTest = ExtentManager.getExtentReports().createTest(name);
        }
        return extentTest;
    }

    public static void writeExtentTestReport(HtmlReportModel htmlReportModel){
        if(htmlReportModel != null){
            ExtentTest extentTest = ExtentManager.getExtentTest(htmlReportModel.getRequirementId()+ " " +
                    htmlReportModel.getRequirement());
            Markup markUp = null;
            if(htmlReportModel.getTestStatusCode().equals(TestStatusCode.PASSED)){
                extentTest.pass(htmlReportModel.getTestName());
                extentTest.info(htmlReportModel.getTestDetails());
                markUp = MarkupHelper.createLabel(TestStatusCode.PASSED.name().toUpperCase(),ExtentColor.GREEN);
            }else if(htmlReportModel.getTestStatusCode().equals(TestStatusCode.FAILED)){
                extentTest.pass(htmlReportModel.getTestName());
                extentTest.info(htmlReportModel.getTestDetails());
                markUp = MarkupHelper.createLabel(TestStatusCode.FAILED.name().toUpperCase(), ExtentColor.RED);
            }
            extentTest.info(markUp);
            ExtentManager.flush();
        }
    }
}
