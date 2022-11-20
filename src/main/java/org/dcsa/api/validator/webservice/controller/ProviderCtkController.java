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

   private final AppProperty appProperty;

    public ProviderCtkController(AppProperty appProperty) {
        this.appProperty = appProperty;
        appProperty.init();
    }

   @GetMapping(value = "/start" )
  String startTestNg(){
      TestNG testng = new TestNG();
      final String suitePath = System.getProperty("user.dir")+"\\suitexmls\\"+AppProperty.TEST_SUITE_NAME;
      List<String> xmlList = new ArrayList<>();
      xmlList.add(suitePath);
      testng.setTestSuites(xmlList);
      testng.run();
      return  null;
  }

    @GetMapping(value = "/download/report/{reportType}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Object> downloadReport(@PathVariable Optional<String> reportType) throws IOException {
        String defaultReportType = ReportUtil.HTML;
        if(reportType.isPresent()){
            defaultReportType = reportType.get();
        }
        HttpHeaders header = new HttpHeaders();
        ByteArrayResource resource = null;
        if(defaultReportType.equalsIgnoreCase(ReportUtil.HTML)){
            resource = FileUtility.getFile( ReportUtil.htmlReportPath);
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + ReportUtil.htmlReportName);

        }else if(defaultReportType.equalsIgnoreCase(ReportUtil.EXCEL)){
            resource = FileUtility.getFile( ReportUtil.excelReportPath);
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + ReportUtil.excelReportName);
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Unknown report type. Only html or excel report type is supported.");
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
