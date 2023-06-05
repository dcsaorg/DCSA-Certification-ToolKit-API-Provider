package org.dcsa.api.provider.ctk.service;

import org.dcsa.api.provider.ctk.model.PostManFolderName;
import org.dcsa.api.provider.ctk.model.enums.OsType;
import org.dcsa.api.provider.ctk.model.enums.PostmanCollectionType;
import org.dcsa.api.provider.ctk.util.FileUtility;
import org.dcsa.api.provider.ctk.util.JsonUtility;
import org.dcsa.api.provider.ctk.util.TestUtility;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static org.dcsa.api.provider.ctk.model.enums.PostmanCollectionType.*;


public class ScriptExecutor {

    private static final String WINDOWS_SCRIPT = "win_newman.bat";

    private static final String NIX_SCRIPT = "nix_newman.sh";

    private static final String TNT_POSTMAN_FOLDER = File.separator+ "requirement" +File.separator+"TntPostmanFolder.json";

    private static final String OVS_POSTMAN_FOLDER = File.separator+ "requirement" +File.separator+"OvsPostmanFolder.json";

    private static final String EDOC_POSTMAN_FOLDER = File.separator+ "requirement" +File.separator+"EDocumentationPostmanFolder.json";

    private static final String BOOKING_POSTMAN_FOLDER = File.separator+ "requirement" +File.separator+"BookingPostmanFolder.json";

    private static final String EBL_POSTMAN_FOLDER = File.separator+ "requirement" +File.separator+"EblPostmanFolder.json";

    public static OsType osType;
    public static String runNewman(PostmanCollectionType postmanCollectionType, boolean isOfficial) {
        osType = TestUtility.getOperatingSystem();
        TestUtility.setOsType(ScriptExecutor.osType);

        String scriptPath = "";
        String reportPath = "";
        if (osType == OsType.WINDOWS) {
            scriptPath = FileUtility.getScriptPath(WINDOWS_SCRIPT);
        } else if (osType == OsType.LINUX || osType == OsType.DOCKER) {
            scriptPath = FileUtility.getScriptPath(NIX_SCRIPT);
        }
        String collectionName = getPostmanCollection(postmanCollectionType);
        String scriptParameter = collectionName;
        if(isOfficial){
            List<PostManFolderName> postManFolderNames = getTestFolderNames(postmanCollectionType);
            String scriptFolderName = makeFoldrNameParameter(Objects.requireNonNull(postManFolderNames));
            scriptParameter = collectionName + " "+scriptFolderName;
        }
        System.out.println(collectionName);
        System.out.println(scriptParameter);
        executeScripForNewmanReport(scriptPath, scriptParameter);
        reportPath = FileUtility.getNewmanReport(postmanCollectionType.name());
        System.out.println(reportPath+" report will be updated");
        return reportPath;
    }
    private static void executeScripForNewmanReport(String scriptPath, String scriptParameter) {
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
    private static String getPostmanCollection(PostmanCollectionType postmanCollectionType){
        if(postmanCollectionType == TNT){
            return FileUtility.getPostmanCollectionName(postmanCollectionType.name());
        }else if(postmanCollectionType == OVS){
            return FileUtility.getPostmanCollectionName(postmanCollectionType.name());
        }else if(postmanCollectionType == EDOC){
            return FileUtility.getPostmanCollectionName(postmanCollectionType.name());
        }else if(postmanCollectionType == BOOKING){
            return FileUtility.getPostmanCollectionName(postmanCollectionType.name());
        }else if(postmanCollectionType == EBL){
            return FileUtility.getPostmanCollectionName(postmanCollectionType.name());
        }
        return "";
    }

    private static List<PostManFolderName> getTestFolderNames(PostmanCollectionType postmanCollectionType){
        List<PostManFolderName> postManFolderNames = null;
        if(postmanCollectionType == TNT){
            postManFolderNames = JsonUtility.getTestFolderNames(TNT_POSTMAN_FOLDER);
        }else if(postmanCollectionType == OVS){
            postManFolderNames = JsonUtility.getTestFolderNames(OVS_POSTMAN_FOLDER);
        }else if(postmanCollectionType == EDOC){
            postManFolderNames = JsonUtility.getTestFolderNames(EDOC_POSTMAN_FOLDER);
        }else if(postmanCollectionType == BOOKING){
            postManFolderNames = JsonUtility.getTestFolderNames(BOOKING_POSTMAN_FOLDER);
        }else if(postmanCollectionType == EBL){
            postManFolderNames = JsonUtility.getTestFolderNames(EBL_POSTMAN_FOLDER);
        }
        return postManFolderNames;
    }

    private static String makeFoldrNameParameter(List<PostManFolderName> postManFolderNames){
        String folderParameter = "--folder";
        String doubleQuot = "\"";
        StringBuilder sb = new StringBuilder();
        postManFolderNames.forEach(item -> {
            sb.append(folderParameter).append(" ").append(doubleQuot).append(item.getName()).append(doubleQuot).append(" ");
        });
        return sb.toString();
    }

}
