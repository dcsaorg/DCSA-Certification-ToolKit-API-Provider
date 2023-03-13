package org.dcsa.api.validator.webservice.controller;


import org.dcsa.api.validator.hook.TestSetup;
import org.dcsa.api.validator.model.enums.UploadType;
import org.dcsa.api.validator.reporter.report.ExtentReportManager;
import org.dcsa.api.validator.util.ReportUtil;
import org.dcsa.api.validator.util.FileUtility;
import org.dcsa.api.validator.util.ScriptExecutor;
import org.dcsa.api.validator.util.TestUtility;
import org.dcsa.api.validator.webservice.init.AppProperty;
import org.dcsa.api.validator.webservice.service.DownloadService;
import org.dcsa.api.validator.webservice.service.UploadService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.testng.TestNG;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProviderCtkController {
    private static final String NO_REPORT_ERROR = "No report was found. Please run the compatibility tool by GET /run to generate reports.";
   private final AppProperty appProperty;
   private final DownloadService downloadService;
   private final UploadService uploadService;

    public ProviderCtkController(AppProperty appProperty, DownloadService downloadService, UploadService uploadService) throws Exception {
        this.appProperty = appProperty;
        this.downloadService = downloadService;
        this.uploadService = uploadService;
        appProperty.init();
    }

   @GetMapping(value = "/run" )
   void runTestNg(HttpServletResponse response) {
        TestUtility.removeTestOutputDirectory();
        TestNG testng = new TestNG();
        final String absolutePath = FileUtility.getTestSuitePath(AppProperty.TEST_SUITE_NAME);
        List<String> xmlList = new ArrayList<>();
        xmlList.add(absolutePath);
        testng.setTestSuites(xmlList);
        testng.run();
        downloadService.downloadHtmlReport(response, ReportUtil.getReports());
   }

    @GetMapping(value = "/run-newman" )
    void runNewman(HttpServletResponse response) throws Exception {
        ScriptExecutor.runNewman();
        ReportUtil.writeReport();
        ExtentReportManager.resetExtentTestReport();
        //AppProperty.stropSparkWebHook();
        TestSetup.tearDown();
        downloadService.downloadHtmlReport(response, ReportUtil.getReports());
    }

        @GetMapping(value = "/", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
   public String home() {
        return "provider ctk home";
   }

   @GetMapping(value = "/download/report", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
   public ResponseEntity<Object> downloadReport() throws IOException {
       HttpHeaders header = new HttpHeaders();
       ByteArrayResource resource;

       if (ReportUtil.htmlReportPath == null) {
           return ResponseEntity
                   .status(HttpStatus.BAD_REQUEST)
                   .body(NO_REPORT_ERROR);
       }
       resource = FileUtility.getFile(ReportUtil.htmlReportPath);
       header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + ReportUtil.htmlReportName);
       header.add("Cache-Control", "no-cache, no-store, must-revalidate");
       header.add("Pragma", "no-cache");
       header.add("Expires", "0");
       return ResponseEntity.ok()
               .headers(header)
               .contentType(MediaType.parseMediaType("application/octet-stream"))
               .body(resource);
   }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, String uploadType) {
        String name = file.getOriginalFilename();
        uploadService.store(file, AppProperty.uploadPath, UploadType.fromValue(uploadType));
        return "uploaded "+file.getOriginalFilename();
    }

}
