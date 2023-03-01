package org.dcsa.api.validator.util;

import org.dcsa.api.validator.model.enums.OsType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ScriptExecutor {
    public static OsType osType;

    public static void runNewman() {
        osType = TestUtility.getOperatingSystem();
        String scriptPath = "";
        if (osType == OsType.WINDOWS) {
            scriptPath = FileUtility.getScriptPath("win_newman.bat");
        } else if (osType == OsType.LINUX || osType == OsType.DOCKER) {
            scriptPath = FileUtility.getScriptPath("nix_newman.sh");
        }
        ReportUtil.setOsType(ScriptExecutor.osType);
        executeScript(scriptPath);
    }

    private static void executeScript(String scriptPath) {
        try {
            // the working directory for the process is .\script
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
}
