package org.dcsa.api.validator.webservice.service.impl;

import lombok.extern.java.Log;
import org.dcsa.api.validator.util.ReportUtil;
import org.dcsa.api.validator.util.FileUtility;
import org.dcsa.api.validator.webservice.service.DownloadService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;

@Service
@Log
public class DownloadServiceImpl implements DownloadService {
    @Override
    public ResponseEntity<byte[]> downloadHtmlReport(HttpServletResponse response, String reportPath , String errorMsg) {
        if(!errorMsg.isBlank()){
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<>(errorMsg.getBytes(), headers, HttpStatus.BAD_REQUEST);
        }

        String headerValues = "attachment;filename="+getFileName(reportPath);
        response.setContentType("text/html");
        response.setHeader("Content-Disposition", "attachment; filename="+getFileName(reportPath));
        try(OutputStream outputStream = response.getOutputStream()) {
            FileSystemResource fileSystemResource = new FileSystemResource(reportPath);
            outputStream.write(reportPath.getBytes(), 0, reportPath.getBytes().length);
            StreamUtils.copy(fileSystemResource.getInputStream(), outputStream);
            if(!reportPath.isBlank()){
                displayReportPath();
            }
            outputStream.flush();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.writeTo(outputStream);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValues)
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(byteArrayOutputStream.toByteArray().length)
                    .body(byteArrayOutputStream.toByteArray());

        } catch (IOException e) {
            log.log(Level.WARNING, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String getFileName(String reportPath){
       reportPath = FileUtility.winPathToUnixPth(reportPath);
       String[] tokens = reportPath.split("/");
       if(tokens.length > 0){
           return tokens[tokens.length-1];
       }
       return "report.html";
    }
    private void displayReportPath(){
        if(ReportUtil.htmlReportPath != null){
            System.out.println("Please check the HTML report at "+ReportUtil.htmlReportPath);
            System.out.println("The report should be downloaded when execution is done successfully :)");
        }
        System.out.println("Optionally you may call GET /download/report for HTML");
    }
}
