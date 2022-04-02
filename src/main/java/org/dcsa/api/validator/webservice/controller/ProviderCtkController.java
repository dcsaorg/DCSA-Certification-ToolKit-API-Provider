package org.dcsa.api.validator.webservice.controller;


import org.dcsa.api.validator.reporter.util.ReportUtil;
import org.dcsa.api.validator.util.FileUtility;
import org.dcsa.api.validator.webservice.init.AppProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.testng.TestNG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ProviderCtkController {

    private static final String RUN_TEST_SUCCESS = "The report is ready. Get the report by GET /download/report/{reportType}, reportType is html or excel";
    private static final String RUN_TEST_FAIL = "The compatibility tool failed to run. Please check all configurations and try again.";
    private static final String NO_REPORT_ERROR =  "No report was found. Please run the compatibility tool by GET /run to generate reports.";
    private static final String UNKNOWN_REPORT_TYPE = "Unknown report type. Only html or excel report type is supported.";

    private final AppProperty appProperty;

    public ProviderCtkController(AppProperty appProperty) {
        this.appProperty = appProperty;
        this.appProperty.init();
    }

   @GetMapping(value = "/run" )
  String runTestNg(){
      TestNG testng = new TestNG();
      final String suitePath = System.getProperty("user.dir")+"\\suitexmls\\"+AppProperty.TEST_SUITE_NAME;
      List<String> xmlList = new ArrayList<>();
      xmlList.add(suitePath);
      testng.setTestSuites(xmlList);
      testng.run();
      if( testng.getStatus() == 1 ){
          return RUN_TEST_SUCCESS;
      }else{
          return RUN_TEST_FAIL;
      }
  }

    @GetMapping(value = "/download/report/{reportType}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Object> downloadReport(@PathVariable Optional<String> reportType) throws IOException {
        String defaultReportType = ReportUtil.HTML;
        if(reportType.isPresent()){
            defaultReportType = reportType.get();
        }
        HttpHeaders header = new HttpHeaders();
        ByteArrayResource resource = null;

        if( ReportUtil.htmlReportPath == null || ReportUtil.excelReportPath == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(NO_REPORT_ERROR);
        }

        if(defaultReportType.equalsIgnoreCase(ReportUtil.HTML)){
            resource = FileUtility.getFile( ReportUtil.htmlReportPath);
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + ReportUtil.htmlReportName);

        }else if(defaultReportType.equalsIgnoreCase(ReportUtil.EXCEL)){
            resource = FileUtility.getFile( ReportUtil.excelReportPath);
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + ReportUtil.excelReportName);
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(UNKNOWN_REPORT_TYPE);
        }
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(header)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

}
