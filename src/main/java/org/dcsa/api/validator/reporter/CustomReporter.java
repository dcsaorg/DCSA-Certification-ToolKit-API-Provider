package org.dcsa.api.validator.reporter;

import io.cucumber.java.sl.In;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import lombok.Data;
import lombok.extern.java.Log;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.dcsa.api.validator.hooks.TestSetup;
import org.dcsa.api.validator.model.TestContext;
import org.dcsa.api.validator.runner.SubscriberTestRunner;
import org.joda.time.DateTime;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.util.*;

@Data
@Log
public class CustomReporter implements IReporter {
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
                               String outputDirectory) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        for (ISuite suite : suites) {
            Map<String, TestContext> testContexts = TestSetup.TestContexts;
            XSSFSheet spreadsheetSummary = workbook.createSheet("TestResult");
            Map<String, Integer[]> resultSummary = new TreeMap<>();
            XSSFCreationHelper createHelper = workbook.getCreationHelper();

            String fileName = ("TestResult_" + suite.getName() + "_" + DateTime.now().toString("yyyyMMdd_HHmmss") + ".xlsx");

            for (Map.Entry<String, TestContext> testContext : testContexts.entrySet()) {
                String name = testContext.getValue().getTestCaseName();
                if (resultSummary.containsKey(name)) {
                    resultSummary.get(name)[0]++;
                    if (testContext.getValue().getStatus() == "PASSED")
                        resultSummary.get(name)[1]++;
                    else
                        resultSummary.get(name)[2]++;
                } else {
                    Integer[] stats = new Integer[]{0, 0, 0};
                    stats[0]++;
                    if (testContext.getValue().getStatus() == "PASSED")
                        stats[1]++;
                    else
                        stats[2]++;
                    resultSummary.put(name, stats);
                }

            }

            XSSFRow sheetRow;
            Set<String> keyIds = resultSummary.keySet();
            List<String> testCaseList = new ArrayList<String>(keyIds);
            Collections.sort(testCaseList);
            int rowId2 = 0;

            sheetRow = spreadsheetSummary.createRow(rowId2++);
            Object[] header = new Object[]{"Test Name", "Total", "Passed", "Failed"};

            XSSFCellStyle hlinkstyle = workbook.createCellStyle();
            XSSFFont hlinkfont = workbook.createFont();
            hlinkfont.setUnderline(XSSFFont.U_SINGLE);
            hlinkfont.setColor(IndexedColors.BLUE.getIndex());
            hlinkstyle.setFont(hlinkfont);

            int cellId = 0;
            for (Object obj : header) {
                Cell cell = sheetRow.createCell(cellId++);
                cell.setCellValue((String) obj);
            }
            int sheetNo = 1;
            for (String key : testCaseList) {
                String sheetName = "TestCase-" + sheetNo++;
                XSSFSheet spreadsheet = workbook.createSheet(sheetName);
                fillWithTestResult(spreadsheet, key, testContexts);
                sheetRow = spreadsheetSummary.createRow(rowId2++);
                Object[] objectArr = resultSummary.get(key);
                cellId = 0;
                Cell cell = sheetRow.createCell(cellId++);
                cell.setCellValue(key);
                XSSFHyperlink link = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.FILE);
                link.setAddress(fileName);
                link.setLocation("'" + sheetName + "'!A2");
                for (Object obj : objectArr) {
                    cell = sheetRow.createCell(cellId++);
                    cell.setCellValue((Integer) obj);
                }
                cell = sheetRow.createCell(cellId++);
                cell.setCellValue("details");
                cell.setHyperlink(link);
                cell.setCellStyle(hlinkstyle);
            }

          /*  XSSFSheet spreadsheet = workbook.createSheet("Test result");
            Map<String, ISuiteResult> results = suite.getResults();
            Map<String, Object[]> testResults = new TreeMap<String, Object[]>();
            testResults.put("1", new Object[]{"Test Name", "Status", "Validations", "Test Detail"});
            int testNo = 1;
            for (ISuiteResult result : results.values()) {
                ITestContext testContext = result.getTestContext();
                //Passes results
                IResultMap tests = testContext.getPassedTests();
                Map<String, Object[]> testResultsPassed;
                testResultsPassed = getExcelResults(tests, testNo);
                testResults.putAll(testResultsPassed);
                //Failed results
                IResultMap fTests = testContext.getFailedTests();
                Map<String, Object[]> testResultsFailed;
                testNo = testResults.size();
                testResultsFailed = getExcelResults(fTests, testNo);
                testResults.putAll(testResultsFailed);
            }

            XSSFRow row;
            Set<String> keyId = testResults.keySet();
            int rowId = 0;
            List<String> list = new ArrayList<String>(keyId);
            Collections.sort(list);

            CellStyle stylePass = workbook.createCellStyle();
            stylePass.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            stylePass.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleFail = workbook.createCellStyle();
            styleFail.setFillForegroundColor(IndexedColors.RED.getIndex());
            styleFail.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (String key : list) {
                row = spreadsheet.createRow(rowId++);
                Object[] objectArr = testResults.get(key);
                cellId = 0;
                for (Object obj : objectArr) {
                    Cell cell = row.createCell(cellId++);
                    if (cellId == 2) {
                        if ((obj).equals("Passed")) {
                            cell.setCellStyle(stylePass);
                        } else if ((obj).equals("Failed")) {
                            cell.setCellStyle(styleFail);
                        }
                    }
                    if (((String) obj).length() >= 32767)
                        cell.setCellValue(((String) obj).substring(0, 32766));
                    else
                        cell.setCellValue((String) obj);
                }
            }*/
            try {
                FileOutputStream out = new FileOutputStream(new File(outputDirectory + "/" + fileName));
                workbook.write(out);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//
    }

   /* private Map<String, Object[]> getExcelResults(IResultMap tests, int testNo) {
        Map<String, Object[]> testResults = new TreeMap<String, Object[]>();
        Set<ITestResult> testResultSet = tests.getAllResults();
        List<ITestResult> testResultsList = new ArrayList<ITestResult>(testResultSet);
        for (ITestResult testResult : testResultsList) {
            testNo++;
            Object[] testResultArrays = new Object[4];
            testResultArrays[0] = testResult.getParameters()[0].toString().replaceAll("\"", "");
            testResultArrays[1] = testResult.getStatus() == 1 ? "Passed" : "Failed";

            List<String> msgs = Reporter.getOutput(testResult);
            List<String> validations = new ArrayList<>();
            List<String> testDetails = new ArrayList<>();
            for (int i = 0; i < msgs.size(); i++) {
                if (msgs.get(i).contains("Validation Step-")) {
                    validations.add(msgs.get(i).substring(("Validation Step-").length()));
                } else
                    testDetails.add(msgs.get(i));
            }
            testResultArrays[2] = String.join("\n", validations.toArray(new String[0]));
            testResultArrays[3] = String.join("\n", testDetails.toArray(new String[0]));
            testResults.put(testNo + "", testResultArrays);
        }
        return testResults;
    }*/

    private void fillWithTestResult(XSSFSheet spreadsheet, String key, Map<String, TestContext> testContexts) {
        int rowId = 0;
        XSSFRow row;
        Object[] header = new Object[]{"Test Name", "Status", "Reason Of Failure", "Test Details"};
        int cellId = 0;
        row = spreadsheet.createRow(rowId++);
        for (Object obj : header) {
            Cell cell = row.createCell(cellId++);
            cell.setCellValue((String) obj);
        }
        for (TestContext testContext : testContexts.values()) {
            if (testContext.getTestCaseName().equals(key)) {
                Object[] objectArr = new Object[4];
                objectArr[0] = key;
                objectArr[1] = testContext.getStatus();
                objectArr[2] = testContext.getReasonOfFailure();
                String testDetails=getTestDetails(testContext);
                if(testDetails.length()>32766)
                     objectArr[3] = getTestDetails(testContext).substring(0, 32766);
                else
                    objectArr[3] = getTestDetails(testContext);
                cellId=0;
                row = spreadsheet.createRow(rowId++);
                Cell cell;
                for (Object obj : objectArr) {
                    cell = row.createCell(cellId++);
                    cell.setCellValue((String) obj);
                }
            }

        }
    }

    private String getTestDetails(TestContext testContext)
    {
        StringBuffer prettyPrint = new StringBuffer();
        for(int i=0;i<testContext.getRequestChain().size();i++)
        {
            FilterableRequestSpecification requestSpec=testContext.getRequestChain().get(i);
            Response response=testContext.getResponseChain().get(i);
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

        for(int i=0;i<testContext.getMessage().size();i++)
            prettyPrint.append(testContext.getMessage().get(i));
        return prettyPrint.toString();
    }

}
