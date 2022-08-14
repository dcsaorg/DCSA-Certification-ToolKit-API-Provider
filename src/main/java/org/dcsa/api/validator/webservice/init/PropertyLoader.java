package org.dcsa.api.validator.webservice.init;

import lombok.extern.java.Log;
import org.dcsa.api.validator.util.FileUtility;

import javax.print.attribute.standard.Fidelity;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

@Log
public class PropertyLoader {

    private static String RESOURCE_FILENAME = "application.properties";
    private static PropertyLoader instance;
    private Properties properties;


    private PropertyLoader() {
        if(AppProperty.isAppDataUploaded) {
            try (InputStream inputStream = FileUtility.getInputStream(AppProperty.uploadPath.toAbsolutePath() + File.separator + RESOURCE_FILENAME)) {
                properties = new Properties();
                properties.load(inputStream);
            } catch (IOException e) {
                log.log(Level.SEVERE, e.getMessage());
            }
        }else {
            try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(RESOURCE_FILENAME)) {
                properties = new Properties();
                properties.load(inputStream);
            } catch (IOException e) {
                log.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    public static synchronized PropertyLoader getInstance() {
        if (instance == null) {
            instance = new PropertyLoader();
        }
        return instance;
    }

    public static String getProperty(String key) {
        return getInstance().properties.getProperty(key);
    }

    public static void setResourceFilename(String resourceFilename){
        RESOURCE_FILENAME = resourceFilename;
    }
}
