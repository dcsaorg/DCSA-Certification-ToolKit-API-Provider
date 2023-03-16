package org.dcsa.api.validator.reporter.report;

import lombok.extern.java.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

@Log
public class NewmanReportModifier {
    private static final String BODY_TEST = "<body class=\"theme-dark\">";
    private static final String BODY_TEST_REPLACE = "<body class=\"theme-dark\"> <div style=\"text-align: center\"><img src=\" https://dcsa.org/wp-content/uploads/2021/05/logo-files.jpg\" alt=\"dcsa-logo-blue-bg\" width=\"80\", height=\"80\"></div> ";

    private static final String NEWMAN_DASHBOARD = "Newman Run Dashboard";

    private static final String DCSA_DASHBOARD = "DCSA Run Dashboard";

    private static String htmlContent;

    public static void modifyFile(String reportPath) {
        Path path = Paths.get(reportPath);
        Charset charset = StandardCharsets.UTF_8;

        try {
            htmlContent = Files.readString(path, charset);
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        htmlContent = htmlContent.replaceAll(BODY_TEST, BODY_TEST_REPLACE);
        htmlContent = htmlContent.replaceAll(NEWMAN_DASHBOARD, DCSA_DASHBOARD);

        try {
            Files.writeString(path, htmlContent, charset);
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }


}
