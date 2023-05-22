package org.dcsa.api.provider.ctk.util;

import org.dcsa.api.provider.ctk.model.enums.OsType;

import java.util.ArrayList;

public class ReportUtil {

    private static boolean assertionTestContinue = false;
    public static String htmlReportPath;
    public static String htmlReportName;
    public static final String REQUEST_RESPONSE_SPLIT = "##";
    public static final String REQUEST_REPLACE = "[INFO] Request:";
    public static final String REQUEST_REPLACE_BOLD = "<b>[INFO] Request:</b>";

    public static final String RESPONSE_REPLACE = "[INFO] Response:";
    public static final String RESPONSE_REPLACE_BOLD = "<b>[[INFO] Response:</b>";
    public static final String HTML_NEWLINE = "</br>";
    public static final String HTML_BOLD_START = "<b>";
    public static final String HTML_BOLD_END = "</b>";


    private static final String TNT_3_0_REQUIREMENT_ID = "TNT.3.0-REQUIREMENT-ID";
    private static final String NEWMAN_TEST_TITLE = "└";
    private static final String REQUEST_INFO = "[INFO] Request:";
    private static final String RESPONSE_INFO = "[INFO] Response:";


    public static String getReports() {
        if (htmlReportPath != null) {
            return htmlReportPath;
        } else {
            return "";
        }
    }



}
