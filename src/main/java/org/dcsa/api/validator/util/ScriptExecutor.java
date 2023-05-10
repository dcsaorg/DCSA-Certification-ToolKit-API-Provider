package org.dcsa.api.validator.util;

import org.dcsa.api.validator.model.TestFolderName;
import org.dcsa.api.validator.model.enums.OsType;
import org.dcsa.api.validator.model.enums.PostmanCollectionType;
import org.dcsa.api.validator.model.enums.ReportType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.dcsa.api.validator.model.enums.PostmanCollectionType.*;
import static org.dcsa.api.validator.model.enums.ReportType.HTML;
import static org.dcsa.api.validator.model.enums.ReportType.NEWMAN;

public class ScriptExecutor {

    private static final String WINDOWS_SCRIPT = "win_newman.bat";

    private static final String NIX_SCRIPT = "nix_newman.sh";

    private static final String TNT_POSTMAN_FOLDER = File.separator+ "requirement" +File.separator+"TntPostmanFolder.json";

    private static final String OVS_POSTMAN_FOLDER = File.separator+ "requirement" +File.separator+"OvsPostmanFolder.json";

    private static final String EDOC_POSTMAN_FOLDER = File.separator+ "requirement" +File.separator+"EDocumentationPostmanFolder.json";

    public static OsType osType;
    public static String runNewman(PostmanCollectionType postmanCollectionType, ReportType reportType, boolean isOfficial) {
        osType = TestUtility.getOperatingSystem();
        ReportUtil.setOsType(ScriptExecutor.osType);

        String scriptPath = "";
        String reportPath = "";
        if (osType == OsType.WINDOWS) {
            scriptPath = FileUtility.getScriptPath(WINDOWS_SCRIPT);
        } else if (osType == OsType.LINUX || osType == OsType.DOCKER) {
            scriptPath = FileUtility.getScriptPath(NIX_SCRIPT);
        }
        String collectionName = getScriptParameter(postmanCollectionType);
        String scriptParameter = collectionName;
        if(isOfficial){
            List<TestFolderName> testFolderNames = getTestFolderNames(postmanCollectionType);
            String scriptFolderName = makeFoldrNameParameter(Objects.requireNonNull(testFolderNames));
            scriptParameter = collectionName + " "+scriptFolderName;
        }
        System.out.println(collectionName);
        System.out.println(scriptParameter);
        if(reportType == NEWMAN){
            executeScripForNewmanReport(scriptPath, scriptParameter, isOfficial);
            reportPath = FileUtility.getNewmanReport(postmanCollectionType.name());
            System.out.println(reportPath+"it will be updated");
        }else if(reportType == HTML){
            executeScripForHtmlReport(scriptPath);
        }
        return reportPath;
    }
    private static void executeScripForNewmanReport(String scriptPath, String scriptParameter, boolean isOfficial) {
        try {
            String[] cmdArray = new String[3];
            // first argument is the script we want to execute
            cmdArray[0] = "cmd.exe";
            // second argument is parameter of the script
            cmdArray[1] = "/c";

            cmdArray[2] = scriptPath + " " + scriptParameter;
            System.out.println("***** Script execution Starts *****");
            System.out.println("Executing script "+scriptPath+" with parameter "+scriptParameter);

            // the execution directory for the script
            File executionDir = new File(FileUtility.getScriptPath(File.separator));
            // create a process and execute cmdArray and parameter
            Process process = Runtime.getRuntime().exec(cmdArray,null, executionDir);
            // Just to hold the process to finish it's execution
            process.waitFor();
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

            Path path = Paths.get(filePath);
            try (Stream<String> stream = Files.lines(path)) {
                stream.forEach(line -> {
                    if (!line.isEmpty()) {
                        System.out.println(line);
                        ReportUtil.fillHtmlReportTNT(line);
                    }
                });
                Files.deleteIfExists(path);
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
        }else if(postmanCollectionType == EDOC){
            return FileUtility.getPostmanCollectionName(postmanCollectionType.name());
        }
        return "";
    }

    private static List<TestFolderName> getTestFolderNames(PostmanCollectionType postmanCollectionType){
        List<TestFolderName> testFolderNames;
        if(postmanCollectionType == TNT){
            testFolderNames = JsonUtility.getTestFolderNames(TNT_POSTMAN_FOLDER);
            return testFolderNames;
        }else if(postmanCollectionType == OVS){
            testFolderNames = JsonUtility.getTestFolderNames(OVS_POSTMAN_FOLDER);
            return testFolderNames;
        }else if(postmanCollectionType == EDOC){
            testFolderNames = JsonUtility.getTestFolderNames(EDOC_POSTMAN_FOLDER);
            return testFolderNames;
        }
        return null;
    }

    private static String makeFoldrNameParameter(List<TestFolderName> testFolderNames){
        String folderParameter = "--folder";
        String doubleQuot = "\"";
        StringBuilder sb = new StringBuilder();
        testFolderNames.forEach(item -> {
            sb.append(folderParameter).append(" ").append(doubleQuot).append(item.getName()).append(doubleQuot).append(" ");
        });
        return sb.toString();
    }

}
