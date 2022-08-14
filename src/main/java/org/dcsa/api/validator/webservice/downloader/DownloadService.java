package org.dcsa.api.validator.webservice.downloader;

import javax.servlet.http.HttpServletResponse;

public interface DownloadService {
    void downloadHtmlReport(HttpServletResponse response, String reportPath);
}
