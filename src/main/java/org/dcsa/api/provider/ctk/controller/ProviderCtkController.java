package org.dcsa.api.provider.ctk.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.dcsa.api.provider.ctk.init.AppProperty;
import org.dcsa.api.provider.ctk.model.enums.PostmanCollectionType;
import org.dcsa.api.provider.ctk.model.enums.UploadType;
import org.dcsa.api.provider.ctk.service.DownloadService;
import org.dcsa.api.provider.ctk.service.NewmanReportModifier;
import org.dcsa.api.provider.ctk.service.UploadService;
import org.dcsa.api.provider.ctk.util.FileUtility;
import org.dcsa.api.provider.ctk.util.ReportUtil;
import org.dcsa.api.provider.ctk.service.ScriptExecutor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.dcsa.api.provider.ctk.model.enums.PostmanCollectionType.UNKNOWN;

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

    @GetMapping(value = "/{collectionType}/{official}")
    ResponseEntity<Resource> runNewman(HttpServletResponse response, @PathVariable String collectionType, @PathVariable String official) throws Exception {
        PostmanCollectionType collectionTypeEnum = PostmanCollectionType.fromName(collectionType);
        boolean isOfficial = official.equalsIgnoreCase("official");

        String errorMsg = "";
        if(collectionTypeEnum.name().equalsIgnoreCase(UNKNOWN.name())){
            errorMsg = "Unknown test collection type";
        }
        if(!errorMsg.isBlank()){
            return downloadService.downloadHtmlReport(response, "", errorMsg);
        }else{
            String reportPath = ScriptExecutor.runNewman(collectionTypeEnum, isOfficial);
            NewmanReportModifier.modifyFile(reportPath, collectionTypeEnum, isOfficial);
            return downloadService.downloadHtmlReport(response, reportPath, errorMsg);
        }
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
