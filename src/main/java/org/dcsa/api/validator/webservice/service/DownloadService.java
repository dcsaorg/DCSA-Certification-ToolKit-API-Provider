package org.dcsa.api.validator.webservice.service;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

public interface DownloadService {
    ResponseEntity<byte[]> downloadHtmlReport(HttpServletResponse response, String reportPath, String errorMsg);
}
