package org.dcsa.api.provider.ctk.init;

import lombok.Data;

import org.dcsa.api.provider.ctk.exception.StorageException;
import org.dcsa.api.provider.ctk.util.TestUtility;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@Configuration
@ConfigurationProperties("app")
public class AppProperty {
    public static String RESOURCE_FILENAME = "application.properties";
    // TNT API keys
    private static final String API_ROOT_URI_KEY = "app.api_root_uri";
    private static final String CONFIG_DATA_KEY = "app.config_data";
    private static final String TEST_DATA_KEY = "app.test_data";
    private static final String CALL_BACK_URI_KEY = "app.callback_uri";
    private static String EVENT_PATH_KEY ="app.event_path";
    private static final String CALL_BACK_PORT_KEY = "app.callback_port";
    private static final String CALL_BACK_WAIT_KEY = "app.callback_wait";
    private static final String TEST_SUITE_NAME_KEY = "app.test_suite_name";

    private static final String UPLOAD_PATH_KEY = "app.upload_config_path";

    private static final String BOOKING_DELAY_KEY = "app.booking_delay";
    private static final String DATABASE_NAME_KEY = "app.db_name";
    private static final String DATABASE_USERNAME_KEY = "app.username";
    private static final String DATABASE_PASSWORD_KEY = "app.password";
    private static final String DATABASE_URL_KEY = "app.url";
    private static final String DATABASE_SCHEMA_KEY = "app.schema";
    private static final String DATABASE_IP_KEY = "app.db_ip";
    // TNT service static config
    public static String API_ROOT_URI;
    public static String CONFIG_DATA;
    public static String TEST_DATA;
    public static String CALLBACK_URI;
    public static String CALLBACK_PORT;
    public static String CALLBACK_WAIT;
    public static String TEST_SUITE_NAME;
    public static String UPLOAD_CONFIG_PATH;
    public static int BOOKING_DELAY;

    public static String DATABASE_URL;
    public static String DATABASE_USER_NAME;
    public static String DATABASE_PASSWORD;
    public static String EVENT_PATH;

    public static String DATABASE_IP;
    public static String API_VERSION = "2.2.0";

    public static String ACCESS_TOKEN;
    public static String CLIENT_SECRET;
    public static String CLIENT_ID;
    public static String AUDIENCE;
    public static String CALLBACK_PATH;

    public static String CALLBACK_URL;
    public static String SUBSCRIPTION_ID;

    private String upload_config_path;
    public static Path uploadPath;
    public static boolean isAppDataUploaded = false;

    public static String ROOT_URI;
    public static String testSuite;
    public static String testData;

    public static String accessToken;
    public static String client_secret;
    public static String client_id;
    public static String audience;
    public static String OUT_PUT_DIR;

    private String event_path;
    private String booking_delay;

    public void init() throws Exception {
        // TNT service config
        if(!PropertyLoader.getProperty(UPLOAD_PATH_KEY).isBlank()){
            AppProperty.UPLOAD_CONFIG_PATH = PropertyLoader.getProperty(UPLOAD_PATH_KEY);
        }else{
            AppProperty.UPLOAD_CONFIG_PATH = upload_config_path;
        }

        if( !PropertyLoader.getProperty(EVENT_PATH_KEY).isBlank()){
            AppProperty.EVENT_PATH = PropertyLoader.getProperty(EVENT_PATH_KEY);
        }else{
            AppProperty.EVENT_PATH = event_path;
        }

        if(Integer.parseInt(PropertyLoader.getProperty(BOOKING_DELAY_KEY)) == 0){
            AppProperty.BOOKING_DELAY = Integer.parseInt(PropertyLoader.getProperty(BOOKING_DELAY_KEY));
        }else{
            AppProperty.BOOKING_DELAY = Integer.parseInt(booking_delay);
        }

        makeUploadPath();
        isAppDataUploaded = true;
    }
    private static void makeUploadPath(){
        uploadPath = Paths.get(AppProperty.UPLOAD_CONFIG_PATH);
        try {
            Files.createDirectories(uploadPath);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

}
