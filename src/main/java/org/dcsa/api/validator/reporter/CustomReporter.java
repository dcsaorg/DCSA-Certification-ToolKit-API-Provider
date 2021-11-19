package org.dcsa.api.validator.reporter;

import lombok.Data;
import lombok.extern.java.Log;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
            XSSFSheet spreadsheet = workbook.createSheet("Test result");
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
                testNo=testResults.size();
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
                int cellId = 0;
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
            }
            try {
                FileOutputStream   out = new FileOutputStream(
                        new File(outputDirectory + "/TestResult_" + suite.getName() + "_" + DateTime.now().toString("yyyyMMdd_HHmmss") + ".xlsx"));
                workbook.write(out);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, Object[]> getExcelResults(IResultMap tests, int testNo) {
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
    }

}
