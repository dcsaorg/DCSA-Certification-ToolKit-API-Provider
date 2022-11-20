package org.dcsa.api.validator.reporter.report;

import lombok.extern.java.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Log
public class ExtentReportModifier {
    private static final String htmlPieCharStartKey = "<div class=\"row\">";
    private static final String htmlPieCharStartVal = "<!-- 1";
    private static final String htmlPieCharEndKey = "--> <div class=\"row\">";
    private static final String htmlDefaultLogoStartKey = "<div class=\"nav-logo\">";
    private static final String htmlDefaultLogoStartVal = "<!-- 2";
    private  static final String htmlDefaultLogoEndKey = "</a>\n" + "</div>";
    private static final String htmlDefaultLogoEndVal = " -->";
    private static final String htmlLeftNavKey = "<ul class=\"nav-left\">";
    private static final String htmlLeftNavVal = "<ul class=\"nav-left\">\n" +
                                                        "<li class=\"m-r-10\">\n" +
                                                        "  <img src=\"https://dcsa.org/wp-content/uploads/2021/05/logo-files.jpg\" alt=\"dcsa-logo\" width=\"48\" height=\"48\" border=\"0\">\n" +
                                                        "</li>";

    private static final Map<String, String> htmlTagMap;
    private static String htmlContent;

    static {
        htmlTagMap = new HashMap<>();
        htmlTagMap.put(htmlPieCharStartKey, htmlPieCharStartVal);
        htmlTagMap.put(htmlDefaultLogoStartKey, htmlDefaultLogoStartVal);
        htmlTagMap.put(htmlDefaultLogoEndKey, htmlDefaultLogoEndVal);
        htmlTagMap.put(htmlLeftNavKey, htmlLeftNavVal);
    }

    public static void modifyFile(String reportPath) {
        Path path = Paths.get(reportPath);
        Charset charset = StandardCharsets.UTF_8;

        try {
            htmlContent = Files.readString(path, charset);
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        // replace key with value
        htmlTagMap.forEach((key, val) -> {
            if(key.equals(htmlPieCharStartKey)){
                htmlContent = replaceSubStringAtIndex(1, htmlContent, key, val);
                // comment the pai chart
                htmlContent = replaceSubStringAtIndex(1, htmlContent, key, htmlPieCharEndKey);
            }else {
                htmlContent = htmlContent.replaceAll(key, val);
            }
        });

        try {
            Files.write(path, htmlContent.getBytes(charset));
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    public static String replaceSubStringAtIndex(int index, String inputStr, String subStr, String replaceStr){
        int posAt =  indexOfSubstringAt(inputStr, subStr, index);
        StringBuilder sb = new StringBuilder(inputStr);
        sb.replace(posAt, subStr.length()+posAt, replaceStr);
        return  sb.toString();
    }

    public static int indexOfSubstringAt(String inputStr, String subStr, int n) {
        int pos = -1;
        do {
            pos = inputStr.indexOf(subStr, pos + 1);
        } while (n-- > 0 && pos != -1);
        return pos;
    }
}
