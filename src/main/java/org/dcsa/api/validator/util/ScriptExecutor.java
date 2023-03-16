package org.dcsa.api.validator.util;

import org.dcsa.api.validator.model.enums.OsType;
import org.dcsa.api.validator.model.enums.PostmanCollectionType;
import org.dcsa.api.validator.model.enums.ReportType;
import org.dcsa.api.validator.reporter.report.NewmanReportModifier;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.dcsa.api.validator.model.enums.PostmanCollectionType.OVS;
import static org.dcsa.api.validator.model.enums.PostmanCollectionType.TNT;
import static org.dcsa.api.validator.model.enums.ReportType.HTML;
import static org.dcsa.api.validator.model.enums.ReportType.NEWMAN;

public class ScriptExecutor {

    private static final String WINDOWS_SCRIPT = "win_newman.bat";

    private static final String NIX_SCRIPT = "nix_newman.sh";

    public static OsType osType;
    public static String runNewman(PostmanCollectionType postmanCollectionType, ReportType reportType) {
        osType = TestUtility.getOperatingSystem();
        ReportUtil.setOsType(ScriptExecutor.osType);

        String scriptPath = "";
        String reportPath = "";
        if (osType == OsType.WINDOWS) {
            scriptPath = FileUtility.getScriptPath(WINDOWS_SCRIPT);
        } else if (osType == OsType.LINUX || osType == OsType.DOCKER) {
            scriptPath = FileUtility.getScriptPath(NIX_SCRIPT);
        }

        String scriptParameter = getScriptParameter(postmanCollectionType);
        System.out.println(scriptParameter);
        if(reportType == NEWMAN){
            executeScripForNewmanReport(scriptPath, scriptParameter);
            reportPath = FileUtility.getNewmanReport(postmanCollectionType.name());
            System.out.println(reportPath+"it will be updated");
        }else if(reportType == HTML){
            executeScripForHtmlReport(scriptPath);
        }
        return reportPath;
    }
    private static void executeScripForNewmanReport(String scriptPath, String scriptParameter) {
        try {
            String[] cmdArray = new String[2];
            // first argument is the script we want to execute
            cmdArray[0] = scriptPath;
            // second argument is parameter of the script
            cmdArray[1] = scriptParameter;
            System.out.println("***** Script execution Starts *****");
            System.out.println("Executing script "+scriptPath+" with parameter "+scriptParameter );

            // the execution directory for the script
            File executionDir = new File(FileUtility.getScriptPath(File.separator));
            // create a process and execute cmdArray and parameter
            Process process = Runtime.getRuntime().exec(cmdArray,null, executionDir);
           process.waitFor();

            // Just to hold the process to finish it's execution
        //    new BufferedReader(new InputStreamReader(process.getInputStream()));
            // print another message
            System.out.println("Script execution is done!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void executeScripForHtmlReport(String scriptPath) {
        try {
            // the working directory for the process
            File dir = new File(FileUtility.getScriptPath(File.separator));
            Process process = Runtime.getRuntime().exec(scriptPath, null, dir);
            System.out.println("***** Script execution Starts *****");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String filePath = FileUtility.makeFile(bufferedReader);
            System.out.println(filePath);

            try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
                stream.forEach(line -> {
                    if (!line.isEmpty()) {
                        System.out.println(line);
                        ReportUtil.fillHtmlReportTNT(line);
                    }
                });
                Files.deleteIfExists(Path.of(filePath));
                System.out.println("***** Script executed successfully *****");
            } catch (IOException e) {
                System.out.println("Error during script execution " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static String getScriptParameter(PostmanCollectionType postmanCollectionType){
        if(postmanCollectionType == TNT){
            return FileUtility.getPostmanCollectionName(postmanCollectionType.name());
        }else if(postmanCollectionType == OVS){
            return FileUtility.getPostmanCollectionName(postmanCollectionType.name());
        }
        return "";
    }

}
