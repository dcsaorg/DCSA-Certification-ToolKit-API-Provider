package org.dcsa.api.validator.webservice.downloader.impl;

import lombok.extern.java.Log;
import org.dcsa.api.validator.reporter.util.ReportUtil;
import org.dcsa.api.validator.webservice.downloader.DownloadService;
import org.dcsa.api.validator.webservice.init.AppProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Log
public class DownloadServiceImpl implements DownloadService {

    @Override
    public void downloadZipFile(HttpServletResponse response, List<String> listOfFileNames) {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=reports.zip");
        try(ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
            for(String fileName : listOfFileNames) {
                FileSystemResource fileSystemResource = new FileSystemResource(fileName);
                ZipEntry zipEntry = new ZipEntry(Objects.requireNonNull(fileSystemResource.getFilename()));
                zipEntry.setSize(fileSystemResource.contentLength());
                zipEntry.setTime(System.currentTimeMillis());
                zipOutputStream.putNextEntry(zipEntry);
                StreamUtils.copy(fileSystemResource.getInputStream(), zipOutputStream);
                zipOutputStream.closeEntry();
            }
            if(listOfFileNames.size() > 0){
                displayReportPath();
            }
            zipOutputStream.finish();
        } catch (IOException e) {
            log.log(Level.WARNING, e.getMessage());
        }
    }

    private void displayReportPath(){
        if(ReportUtil.htmlReportPath != null){
            System.out.println("Please check the HTML report at "+ReportUtil.htmlReportPath);
        }
        if(ReportUtil.excelReportPath != null){
            System.out.println("Please check the EXCEL report at "+ReportUtil.excelReportPath);
        }
        System.out.println("Or please call GET /download/report/html for HTML\n" +
                "Or please call GET /download/report/excel for excel by any browser");
    }
}
