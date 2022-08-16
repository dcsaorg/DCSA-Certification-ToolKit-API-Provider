package org.dcsa.api.validator.webservice.controller;


import org.dcsa.api.validator.model.UploadType;
import org.dcsa.api.validator.reporter.util.ReportUtil;
import org.dcsa.api.validator.util.FileUtility;
import org.dcsa.api.validator.webservice.downloader.DownloadService;
import org.dcsa.api.validator.webservice.init.AppProperty;
import org.dcsa.api.validator.webservice.uploader.service.StorageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.testng.TestNG;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class ProviderCtkController {

    private static final String RUN_TEST_FAIL = "The compatibility tool failed to run. Please check all configurations and try again.";
    private static final String RUN_TEST_SUCCESS = "The compatibility tool was successfully executed. Please check the report.";
    private static final String NO_REPORT_ERROR = "No report was found. Please run the compatibility tool by GET /run to generate reports.";
    private static final String TEST_SUITE_DIR = "/suitexmls/";

    private final DownloadService downloadService;
    private final StorageService storageService;

    public ProviderCtkController(AppProperty appProperty, DownloadService downloadService, StorageService storageService) {
        this.downloadService = downloadService;
        this.storageService = storageService;
        appProperty.init();
    }

    @GetMapping(value = "/run")
    String runTestNg(HttpServletResponse response) {
        TestNG testng = new TestNG();
        final String suitePath = TEST_SUITE_DIR+AppProperty.TEST_SUITE_NAME;
        final String absolutePath = System.getProperty("user.dir") + Path.of(suitePath);
        List<String> xmlList = new ArrayList<>();
        xmlList.add(absolutePath);
        testng.setTestSuites(xmlList);
        testng.run();
        if (testng.getStatus() == 1) {
            downloadService.downloadHtmlReport(response, ReportUtil.getReports());
        } else {
            return RUN_TEST_FAIL;
        }
        return RUN_TEST_SUCCESS;
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
        storageService.store(file, AppProperty.uploadPath, UploadType.fromValue(uploadType));
        return "uploaded "+file.getOriginalFilename();
    }

}
