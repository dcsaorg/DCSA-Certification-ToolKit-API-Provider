package org.dcsa.api.validator.reporter.report;

import lombok.extern.java.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Level;

@Log
public class ExtentReportModifier {
    // to comment the pai chart
    private static final String htmlStrStart = "<div class=\"row\">";
    private static final String htmlStartReplaceStr = "<!--";
    private static final String htmlStrEnd = "<div class=\"row\">";
    private static final String htmlEndReplaceStr = "--> <div class=\"row\">";

    public static void modifyFile(String reportPath) {
        Path path = Paths.get(reportPath);
        Charset charset = StandardCharsets.UTF_8;

        String content = null;
        try {
            content = Files.readString(path, charset);
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        // comment the pai chart
        int indexOfHtmlStart = indexOfSubstringAt(Objects.requireNonNull(content), htmlStrStart,1);
        content = replaceStringAtIndex(indexOfHtmlStart, htmlStrStart.length()+indexOfHtmlStart, content, htmlStartReplaceStr);
        int indexOfHtmlEnd = indexOfSubstringAt(content, htmlStrEnd, 1);
        content = replaceStringAtIndex(indexOfHtmlEnd, htmlStrEnd.length()+indexOfHtmlEnd, content, htmlEndReplaceStr);

        try {
            Files.write(path, content.getBytes(charset));
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    public static int indexOfSubstringAt(String inputStr, String subStr, int n) {
        int pos = -1;
        do {
            pos = inputStr.indexOf(subStr, pos + 1);
        } while (n-- > 0 && pos != -1);
        return pos;
    }

    public static String replaceStringAtIndex(int start, int end, String inputStr,  String replaceStr){
        StringBuilder sb = new StringBuilder(inputStr);
        sb.replace(start, end, replaceStr);
        return  sb.toString();
    }
}
