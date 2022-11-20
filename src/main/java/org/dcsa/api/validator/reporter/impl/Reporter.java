package org.dcsa.api.validator.reporter.impl;

import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import org.dcsa.api.validator.constant.TestStatusCode;
import org.dcsa.api.validator.hook.TestSetup;
import org.dcsa.api.validator.model.HtmlReportModel;
import org.dcsa.api.validator.model.TestContext;
import org.dcsa.api.validator.model.enums.ValidationRequirementId;
import org.dcsa.api.validator.reporter.report.ExtentReportManager;
import org.dcsa.api.validator.reporter.report.ExtentReportModifier;
import org.dcsa.api.validator.reporter.util.ReportUtil;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

import java.util.*;

public class Reporter implements IReporter {
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
                               String outputDirectory) {
        for (ISuite suite : suites) {
            Map<String, TestContext> testContexts = TestSetup.TestContexts;
            Map<String, Integer[]> resultSummary = new TreeMap<>();
            Map<String, Set<String>> childTestCases = new TreeMap<>();

            for (Map.Entry<String, TestContext> testContext : testContexts.entrySet()) {
                String fullName = testContext.getValue().getTestCaseName();
                String name = getTestKey(fullName);
                if (resultSummary.containsKey(name)) {
                    resultSummary.get(name)[0]++;
                    if (testContext.getValue().getStatus().equals("PASSED"))
                        resultSummary.get(name)[1]++;
                    else
                        resultSummary.get(name)[2]++;
                    childTestCases.get(name).add(fullName);
                } else {
                    Integer[] stats = new Integer[]{0, 0, 0};
                    stats[0]++;
                    if (testContext.getValue().getStatus().equals("PASSED"))
                        stats[1]++;
                    else
                        stats[2]++;
                    resultSummary.put(name, stats);
                    Set<String> list = new HashSet<>();
                    list.add(fullName);
                    childTestCases.put(name, list);
                }
            }
            Set<String> keyIds = resultSummary.keySet();
            List<String> testCaseList = new ArrayList<>(keyIds);
            addResultSummary(suite, testContexts, testCaseList, childTestCases);
        }
    }

    private void addResultSummary(ISuite suite,  Map<String, TestContext> testContexts, List<String> testCaseList, Map<String, Set<String>> childTestCases ){
        for (String key : testCaseList) {
            for (String str : childTestCases.get(key)) {
                fillHtmlReport(str, testContexts, suite.getName());
            }
        }
        if(ReportUtil.htmlReportPath != null){
            ExtentReportModifier.modifyFile(ReportUtil.htmlReportPath);
        }
        ExtentReportManager.resetExtentTestReport();

    }

    private void fillHtmlReport(String key, Map<String, TestContext> testContexts, String testSuiteName) {
        for (TestContext testContext : testContexts.values()) {
            if (testContext.getTestCaseName().equals(key)) {
                HtmlReportModel htmlReportModel = new HtmlReportModel();
                String[] token = key.split("_");
                if(token.length == 3){
                    htmlReportModel.setRequirementId(token[0]);
                    htmlReportModel.setRequirement(token[1]);
                    htmlReportModel.setTestName(token[2]);
                //    htmlReportModel.setValidationRequirementID(ValidationRequirementId.getById(token[0]));
                }
                if(testContext.getStatus().equals(TestStatusCode.PASSED.name())){
                    htmlReportModel.setTestStatusCode(TestStatusCode.PASSED);
                } else if(testContext.getStatus().equals(TestStatusCode.FAILED.name())){
                    htmlReportModel.setTestStatusCode(TestStatusCode.FAILED);
                }
                htmlReportModel.setFailureReason(testContext.getReasonOfFailure());
                htmlReportModel.setTestDetails(getTestDetails(testContext));
                ExtentReportManager.writeExtentTestReport(htmlReportModel, testSuiteName);
            }
        }
    }
    private String getTestDetails(TestContext testContext) {
        StringBuilder prettyPrint = new StringBuilder();
        for (int i = 0; i < testContext.getRequestChain().size(); i++) {
            FilterableRequestSpecification requestSpec = testContext.getRequestChain().get(i);
            Response response = null;
            try {
                response = testContext.getResponseChain().get(i);
                prettyPrint.append("Request method: ").append(requestSpec.getMethod()).append("\n\n");
                prettyPrint.append("Request URI: ").append(requestSpec.getURI()).append("\n\n");
            }catch (Exception e){
                // ignore it
            }

            if (requestSpec.getQueryParams() != null) {
                if (requestSpec.getQueryParams().size() > 0) {
                    prettyPrint.append("Query params: ");
                    for (Map.Entry<String, String> queries : requestSpec.getQueryParams().entrySet()) {
                        prettyPrint.append(queries.getKey()).append("=").append(queries.getValue()).append(", ");
                    }
                    prettyPrint.append("\n\n");
                }
            }

            List<io.restassured.http.Header> headers = requestSpec.getHeaders().asList();
            if (headers != null) {
                prettyPrint.append("Request Headers: ");
                for (Header header : headers) {
                    prettyPrint.append(header.getName() + "=" + header.getValue() + ", ");
                }
                prettyPrint.append("\n\n");
            }
            if (requestSpec.getBody() != null)
                prettyPrint.append("Request Body ==>" + requestSpec.getBody() + "\n\n");
            else
                prettyPrint.append("Request Body ==> No Request Body\n\n");

            if (response != null) {
                prettyPrint.append("Response Http Code: " + response.getStatusCode() + "\n\n");
                prettyPrint.append("Response Headers: " + "Content-Type="
                        + response.getHeader("Content-Type") + ", API-Version="
                        + response.getHeader("API-Version")
                        + "\n\n");
                if (response.getBody() != null) {
                    String responseString = response.getBody().asString();
                    if (responseString != null && responseString != "")
                        prettyPrint.append("Response Body ==>" + responseString + "\n\n");
                    else
                        prettyPrint.append("Response Body ==> No Response Body\n\n");
                }
            }
        }
        for (int i = 0; i < testContext.getMessage().size(); i++) {
            prettyPrint.append(testContext.getMessage().get(i));
        }
        String testDetails;
        if (prettyPrint.length() > 32766) {
            testDetails = prettyPrint.substring(0, 32766);
        }
        else {
            testDetails = prettyPrint.toString();
        }
/*        if(!testContext.getTestDetails().toString().isBlank()){
            testDetails = testContext.getTestDetails().toString();
        }*/
        return testDetails;
    }

    String getTestKey(String testName) {
        String key = testName;
        String[] token = testName.split("_");
        if (token.length > 1)
            key = token[0] + "_" + token[1];
        return key;
    }
}
