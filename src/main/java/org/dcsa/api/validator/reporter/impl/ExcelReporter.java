package org.dcsa.api.validator.reporter.impl;

import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.dcsa.api.validator.config.Configuration;
import org.dcsa.api.validator.constant.TestStatusCode;
import org.dcsa.api.validator.hook.TestSetup;
import org.dcsa.api.validator.model.HtmlReportModel;
import org.dcsa.api.validator.model.TestContext;
import org.dcsa.api.validator.reporter.CustomReporter;
import org.dcsa.api.validator.reporter.report.ExtentReportManager;
import org.dcsa.api.validator.reporter.report.ExtentReportModifier;
import org.dcsa.api.validator.reporter.util.ReportUtil;
import org.dcsa.api.validator.webservice.init.AppProperty;
import org.joda.time.DateTime;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcelReporter implements CustomReporter {

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
                               String outputDirectory) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        for (ISuite suite : suites) {
            Map<String, TestContext> testContexts = TestSetup.TestContexts;
            XSSFSheet spreadsheetSummary = workbook.createSheet("TestSummary");
            Map<String, Integer[]> resultSummary = new TreeMap<>();
            Map<String, Set<String>> childTestCases = new TreeMap<>();
            XSSFCreationHelper createHelper = workbook.getCreationHelper();

            ReportUtil.getReportPath(suite.getName(), ReportUtil.EXCEL_EXTENSION, ReportUtil.EXCEL);

            for (Map.Entry<String, TestContext> testContext : testContexts.entrySet()) {
                String fullName = testContext.getValue().getTestCaseName();
                String name = getTestKey(fullName);
                if (resultSummary.containsKey(name)) {
                    resultSummary.get(name)[0]++;
                    if (testContext.getValue().getStatus() == "PASSED")
                        resultSummary.get(name)[1]++;
                    else
                        resultSummary.get(name)[2]++;
                    childTestCases.get(name).add(fullName);
                } else {
                    Integer[] stats = new Integer[]{0, 0, 0};
                    stats[0]++;
                    if (testContext.getValue().getStatus() == "PASSED")
                        stats[1]++;
                    else
                        stats[2]++;
                    resultSummary.put(name, stats);
                    Set<String> list = new HashSet<>();
                    list.add(fullName);
                    childTestCases.put(name, list);
                }

            }

            XSSFRow sheetRow;
            Set<String> keyIds = resultSummary.keySet();
            List<String> testCaseList = new ArrayList<>(keyIds);
            Collections.sort(testCaseList);
            int rowId2 = 0;
            SortedSet<String> keys = new TreeSet<>(childTestCases.keySet());

            sheetRow = spreadsheetSummary.createRow(rowId2++);
            Object[] header = new Object[]{"Requirement ID", "Requirement", "Total", "Passed", "Failed"};
            int cellId = 0;
            for (Object obj : header) {
                Cell cell = sheetRow.createCell(cellId++);
                cell.setCellValue((String) obj);
                cell.setCellStyle(getCellStyle("header", workbook));
            }
            int sheetNo = 1;
            for (String key : testCaseList) {
                String sheetName = "TestResult-"  + sheetNo++;
                XSSFSheet spreadsheet = workbook.createSheet(sheetName);
                int row = 0;
                for (String str : childTestCases.get(key)) {
                    row = fillWithTestResult(spreadsheet, str, testContexts, workbook, row);
                    fillHtmlReport(str, testContexts, suite.getName());
                }
                sheetRow = spreadsheetSummary.createRow(rowId2++);
                Object[] objectArr = resultSummary.get(key);
                cellId = 0;
                Cell cell = sheetRow.createCell(cellId++);
                cell.setCellValue(getRequirementId(key));
                cell.setCellStyle(getCellStyle("normal", workbook));
                cell = sheetRow.createCell(cellId++);
                cell.setCellValue(getRequirement(key));
                cell.setCellStyle(getCellStyle("normal", workbook));
                XSSFHyperlink link = createHelper.createHyperlink(HyperlinkType.FILE);

                link.setAddress(ReportUtil.excelReportName);
                link.setLocation("'" + sheetName + "'!A2");
                for (Object obj : objectArr) {
                    cell = sheetRow.createCell(cellId++);
                    cell.setCellValue((Integer) obj);
                    cell.setCellStyle(getCellStyle("normal", workbook));
                }
                cell = sheetRow.createCell(cellId++);
                cell.setCellValue("details");
                cell.setHyperlink(link);
                cell.setCellStyle(getCellStyle("link", workbook));
            }
            ExtentReportModifier.modifyFile(ReportUtil.htmlReportPath);
            try {
                FileOutputStream out = new FileOutputStream(ReportUtil.excelReportPath);
                workbook.write(out);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    private int fillWithTestResult(XSSFSheet spreadsheet, String key, Map<String, TestContext> testContexts, XSSFWorkbook workbook, int startIndex) {
        int rowId = startIndex;
        XSSFRow row;
        Object[] header = new Object[]{"Test Name", "Status", "Reason Of Failure", "Test Details"};
        int cellId = 0;
        if (rowId == 0) {
            row = spreadsheet.createRow(rowId++);
            for (Object obj : header) {
                Cell cell = row.createCell(cellId++);
                cell.setCellValue((String) obj);
                cell.setCellStyle(getCellStyle("header", workbook));
            }
        }
        for (TestContext testContext : testContexts.values()) {
            if (testContext.getTestCaseName().equals(key)) {
                Object[] objectArr = new Object[4];
                objectArr[0] = getTestDescription(key);
                objectArr[1] = testContext.getStatus();
                objectArr[2] = testContext.getReasonOfFailure();
                objectArr[3] = getTestDetails(testContext);
                cellId = 0;
                row = spreadsheet.createRow(rowId++);
                Cell cell;
                for (Object obj : objectArr) {
                    cell = row.createCell(cellId++);
                    cell.setCellValue((String) obj);
                    if (testContext.getStatus().equals("PASSED") && cellId == 2)
                        cell.setCellStyle(getCellStyle("passed", workbook));
                    else if (testContext.getStatus().equals("FAILED") && cellId == 2)
                        cell.setCellStyle(getCellStyle("failed", workbook));
                    else
                        cell.setCellStyle(getCellStyle("normal", workbook));
                }
            }

        }
        return rowId;
    }

    private String getTestDetails(TestContext testContext) {
        StringBuffer prettyPrint = new StringBuffer();
        for (int i = 0; i < testContext.getRequestChain().size(); i++) {
            FilterableRequestSpecification requestSpec = testContext.getRequestChain().get(i);
            Response response = testContext.getResponseChain().get(i);
            prettyPrint.append("Request method: " + requestSpec.getMethod() + "\n\n");
            prettyPrint.append("Request URI: " + requestSpec.getURI() + "\n\n");

            if (requestSpec.getQueryParams() != null) {
                if (requestSpec.getQueryParams().size() > 0) {
                    prettyPrint.append("Query params: ");
                    for (Map.Entry<String, String> queries : requestSpec.getQueryParams().entrySet()) {
                        prettyPrint.append(queries.getKey() + "=" + queries.getValue() + ", ");
                    }
                    prettyPrint.append("\n\n");
                }
            }

            List<Header> headers = requestSpec.getHeaders().asList();
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

        for (int i = 0; i < testContext.getMessage().size(); i++)
            prettyPrint.append(testContext.getMessage().get(i));
        String testDetails;
        if (prettyPrint.length() > 32766) {
            testDetails = prettyPrint.substring(0, 32766);
        }
        else {
            testDetails = prettyPrint.toString();
        }
        return testDetails;
    }

    private CellStyle getCellStyle(String type, XSSFWorkbook workbook) {

        if (type.equals("link")) {
            XSSFCellStyle hlinkstyle = workbook.createCellStyle();
            XSSFFont hlinkfont = workbook.createFont();
            hlinkfont.setUnderline(XSSFFont.U_SINGLE);
            hlinkfont.setColor(IndexedColors.BLUE.getIndex());
            hlinkstyle.setFont(hlinkfont);
            hlinkstyle.setBorderBottom(BorderStyle.THIN);
            hlinkstyle.setBorderLeft(BorderStyle.THIN);
            hlinkstyle.setBorderRight(BorderStyle.THIN);
            hlinkstyle.setBorderTop(BorderStyle.THIN);
            return hlinkstyle;
        } else if (type.equals("header")) {
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            borderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = workbook.createFont();
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            borderStyle.setFont(font);
            return borderStyle;
        } else if (type.equals("passed")) {
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            borderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            return borderStyle;
        } else if (type.equals("failed")) {
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            borderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            return borderStyle;
        } else {
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);
            borderStyle.setBorderTop(BorderStyle.THIN);
            return borderStyle;
        }

    }

    String getRequirementId(String key) {
        String[] token = key.split("_");
        if (token.length > 1)
            return token[0];
        return null;
    }

    String getRequirement(String key) {
        String[] token = key.split("_");
        if (token.length > 1)
            return token[1];
        return key;
    }

    String getTestKey(String testName) {
        String key = testName;
        String[] token = testName.split("_");
        if (token.length > 1)
            key = token[0] + "_" + token[1];
        return key;
    }

    String getTestDescription(String key) {
        String[] token = key.split("_");
        if (token.length > 2)
            key = token[2];
        return key;
    }
}
