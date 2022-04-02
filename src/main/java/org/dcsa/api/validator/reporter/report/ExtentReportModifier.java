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
    private static final String htmlStrStartKey = "<div class=\"row\">";
    private static final String htmlStrStartVal = "<!--";
    private static final String htmlStrEndVal = "--> <div class=\"row\">";

    public static void modifyFile(String reportPath) {
        Path path = Paths.get(reportPath);
        Charset charset = StandardCharsets.UTF_8;

        String content = null;
        try {
            content = Files.readString(path, charset);
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        // comment the pass chart
        content = replaceSubStringAtIndex(1, content,htmlStrStartKey, htmlStrStartVal );
        // comment the pai chart
        content = replaceSubStringAtIndex(1, content,htmlStrStartKey, htmlStrEndVal );

        try {
            Files.write(path, content.getBytes(charset));
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
