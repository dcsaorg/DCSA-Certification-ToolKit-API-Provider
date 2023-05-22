package org.dcsa.api.provider.ctk.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface DownloadService {
    ResponseEntity<Resource> downloadHtmlReport(HttpServletResponse response, String reportPath, String errorMsg) throws IOException;
}
