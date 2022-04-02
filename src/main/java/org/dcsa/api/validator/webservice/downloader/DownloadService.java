package org.dcsa.api.validator.webservice.downloader;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DownloadService {
    void downloadZipFile(HttpServletResponse response, List<String> listOfFileNames);
}
