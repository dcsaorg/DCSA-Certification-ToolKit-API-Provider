package org.dcsa.api.validator.util;

import lombok.extern.java.Log;
import org.dcsa.api.validator.webservice.init.AppProperty;
import org.springframework.core.io.ByteArrayResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.PortUnreachableException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Level;

@Log
public class FileUtility {

        public static String loadResourceAsString(String resource) {
            try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                log.log(Level.SEVERE, e.getMessage());
            }
            return "";
        }

    public static String loadFileAsString(String resource) {
        String fileData = null;
        try{
            Path filePath = Path.of(resource);
            fileData =  Files.readString(filePath.toAbsolutePath());

        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return fileData;
    }

    public static InputStream getInputStream(String resource){
        InputStream inputStream = null;
        try{
            Path filePath = Path.of(resource);
            inputStream = Files.newInputStream(filePath.toAbsolutePath());
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return inputStream;
    }

    public static ByteArrayResource getFile(String resource) throws IOException {
        ByteArrayResource byteArrayResource;
        try {
            File file = new File(resource);
            Path path = Paths.get(file.getAbsolutePath());
            byteArrayResource = new ByteArrayResource(Files.readAllBytes(path));
        } catch (Exception e)
        {
            throw new IllegalStateException("Cannot find file " + resource);
        }
        return  byteArrayResource;
    }
    public static String winPathToUnixPth(String path) {
        return path.indexOf('\\') < 0 ? path : path.replace('\\', '/');
    }
    public static String getResourcePath(String resourceName){
        String suiteDir = "suitexmls/";
        ClassLoader classLoader = FileUtility.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(suiteDir+resourceName)).getFile());
            return file.getAbsolutePath();
    }

    public static String getTestSuitePath(){
        final String suitePath = "/config/suitexmls/"+AppProperty.TEST_SUITE_NAME;
        final String absolutePath = System.getProperty("user.dir") + Path.of(suitePath);
        return absolutePath;
    }

    public static String getExternalConfigPath(){
       return System.getProperty("user.dir")+"/config/application.properties";
    }

}
