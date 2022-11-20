package org.dcsa.api.validator.webservice.service.impl;

import lombok.extern.java.Log;
import org.dcsa.api.validator.reporter.util.ReportUtil;
import org.dcsa.api.validator.util.FileUtility;
import org.dcsa.api.validator.webservice.service.DownloadService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;

@Service
@Log
public class DownloadServiceImpl implements DownloadService {
    @Override
    public void downloadHtmlReport(HttpServletResponse response, String reportPath) {
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
        } catch (IOException e) {
            log.log(Level.WARNING, e.getMessage());
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
        }
        System.out.println("Or please call GET /download/report/html for HTML");
    }
}
