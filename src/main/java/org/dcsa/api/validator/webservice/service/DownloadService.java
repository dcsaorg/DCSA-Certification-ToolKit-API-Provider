package org.dcsa.api.validator.webservice.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface DownloadService {
    ResponseEntity<Resource> downloadHtmlReport(HttpServletResponse response, String reportPath, String errorMsg) throws IOException;
}
