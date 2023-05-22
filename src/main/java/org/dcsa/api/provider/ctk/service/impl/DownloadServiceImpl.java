package org.dcsa.api.provider.ctk.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.dcsa.api.provider.ctk.service.DownloadService;
import org.dcsa.api.provider.ctk.util.FileUtility;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Log
public class DownloadServiceImpl implements DownloadService {
    @Override
    public ResponseEntity<Resource> downloadHtmlReport(HttpServletResponse response, String reportPath , String errorMsg) throws IOException {
        if(!errorMsg.isBlank()){
            ByteArrayResource resource = new ByteArrayResource(errorMsg.getBytes());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.parseMediaType("text/html"))
                    .body(resource);
        }

        File file = new File(reportPath);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+getFileName(reportPath));
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    private String getFileName(String reportPath){
       reportPath = FileUtility.winPathToUnixPth(reportPath);
       String[] tokens = reportPath.split("/");
       if(tokens.length > 0){
           return tokens[tokens.length-1];
       }
       return "report.html";
    }

    private void displayReportPath(String reportPath ){
        if(reportPath != null){
            System.out.println("Please check the HTML report at "+reportPath);
            System.out.println("The report should be downloaded when execution is done successfully :)");
        }
        System.out.println("Optionally you may call GET /download/report for HTML");
    }
}
