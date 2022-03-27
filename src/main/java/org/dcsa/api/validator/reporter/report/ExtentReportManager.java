package org.dcsa.api.validator.reporter.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.dcsa.api.validator.constant.TestStatusCode;
import org.dcsa.api.validator.model.HtmlReportModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.Properties;

public class ExtentReportManager {

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
        extentReports.setSystemInfo(COMPANY.toUpperCase(), PropertyLoader.getInstance().getProperty("report.company"));
        extentReports.setSystemInfo(AUTHOR.toUpperCase(), PropertyLoader.getInstance().getProperty("report.author"));
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
        reportName = PropertyLoader.getInstance().getProperty("report.file.name") +
                "_" +dateFormat.format(Calendar.getInstance().getTime()) + ".html";
        reportPath = System.getProperty("user.dir")+"/"+
                PropertyLoader.getInstance().getProperty("report.file.location")+"/" + reportName;
        ExtentSparkReporter reporter = new ExtentSparkReporter( reportPath);

        reporter.config().setReportName(PropertyLoader.getInstance().getProperty("report.name"));
        reporter.config().setDocumentTitle(PropertyLoader.getInstance().getProperty("report.title"));
        reporter.config().setTheme(
                Objects.equals(PropertyLoader.getInstance().getProperty("report.theme"), Theme.DARK.getName()) ?
                        Theme.DARK : Theme.STANDARD);
        reporter.config().setTimelineEnabled(Boolean.parseBoolean(
                PropertyLoader.getInstance().getProperty("report.timeline.enabled")));
        reporter.config().setTimeStampFormat(PropertyLoader.getInstance().getProperty("report.time.stamp.format"));
        return reporter;
    }

    public static String getReportPath(){
        return reportPath;
    }

    public static String getReportName(){
        return reportName;
    }

    public static ExtentTest getExtentTest(String name){
        if(name != null){
            extentTest = ExtentReportManager.getExtentReports().createTest(name);
        }
        return extentTest;
    }

    public static void writeExtentTestReport(HtmlReportModel htmlReportModel){
        if(htmlReportModel != null){
            ExtentTest extentTest = ExtentReportManager.getExtentTest(htmlReportModel.getRequirementId()+ " " +
                    htmlReportModel.getRequirement());
            extentTest.assignCategory(htmlReportModel.getRequirementId());
            if(htmlReportModel.getTestStatusCode().equals(TestStatusCode.PASSED)){
                extentTest.pass(htmlReportModel.getTestName());
                if(!htmlReportModel.getTestDetails().isEmpty())
                    extentTest.info(htmlReportModel.getTestDetails());
                Markup markUp = MarkupHelper.createLabel(TestStatusCode.PASSED.name().toUpperCase(),ExtentColor.GREEN);
                extentTest.log(Status.INFO, markUp);
            }else if(htmlReportModel.getTestStatusCode().equals(TestStatusCode.FAILED)){
                extentTest.fail(htmlReportModel.getTestName());
                if(!htmlReportModel.getTestDetails().isEmpty())
                    extentTest.info(htmlReportModel.getTestDetails());
                Markup markUp = MarkupHelper.createLabel(TestStatusCode.FAILED.name().toUpperCase(), ExtentColor.RED);
                extentTest.log(Status.WARNING, markUp);
            }
            ExtentReportManager.flush();
        }
    }
}
