package org.dcsa.api.provider.ctk.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.dcsa.api.provider.ctk.init.AppProperty;
import org.dcsa.api.provider.ctk.model.enums.PostmanCollectionType;
import org.dcsa.api.provider.ctk.service.DownloadService;
import org.dcsa.api.provider.ctk.service.NewmanReportModifier;
import org.dcsa.api.provider.ctk.service.ScriptExecutor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.dcsa.api.provider.ctk.model.enums.PostmanCollectionType.UNKNOWN;

@RestController
public class ProviderCtkController {
   private final AppProperty appProperty;
   private final DownloadService downloadService;

    public ProviderCtkController(AppProperty appProperty, DownloadService downloadService) throws Exception {
        this.appProperty = appProperty;
        this.downloadService = downloadService;
        appProperty.init();
    }

    @GetMapping(value = "/{collectionType}/{official}")
    ResponseEntity<Resource> runNewman(HttpServletResponse response, @PathVariable String collectionType, @PathVariable String official) throws Exception {
        PostmanCollectionType collectionTypeEnum = PostmanCollectionType.fromName(collectionType);
        boolean isOfficial = official.equalsIgnoreCase("official");

        String errorMsg = "";
        if(collectionTypeEnum.name().equalsIgnoreCase(UNKNOWN.name())){
            errorMsg = "Unknown API type";
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


}
