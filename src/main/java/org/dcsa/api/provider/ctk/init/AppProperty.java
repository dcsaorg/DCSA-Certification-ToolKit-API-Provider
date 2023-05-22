package org.dcsa.api.provider.ctk.init;

import lombok.Data;

import org.dcsa.api.provider.ctk.init.exception.StorageException;
import org.dcsa.api.provider.ctk.util.TestUtility;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
    private static final String UPLOAD_CONFIG_PATH_NAME_KEY = "app.upload_config_path";

    private static final String BOOKING_DELAY_KEY = "app.booking.delay";
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
    // TNT service config
    private String api_root_uri;
    private String test_data;
    private String callback_uri;
    private String callback_port;
    private String callback_wait;
    private String test_suite_name;
    private String config_data;
    private String event_path;
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
    public static int booking_delay;

    public void init() throws Exception {
        // TNT service config
        SUBSCRIPTION_ID = "";
        CLIENT_SECRET = System.getenv("client_secret");
        CLIENT_ID = System.getenv("client_id");
        AUDIENCE = System.getenv("audience");
        CALLBACK_PATH =  "/v"+ API_VERSION.split("\\.")[0]+ "/notification-endpoints/receive";
        String evnApiRootUri = System.getenv("API_ROOT_URI");
        String evnConfigData = System.getenv("CONFIG_DATA");
        String evnTestData = System.getenv("TEST_DATA");
        String evnCallBackUri = System.getenv("CALLBACK_URI");
        String evnCallBackPort = System.getenv("CALLBACK_PORT");
        String evnCallBackWait = System.getenv("CALLBACK_WAIT");
        String evnTestSuite = System.getenv("TEST_SUITE");

        if(evnApiRootUri != null){
            AppProperty.API_ROOT_URI = evnApiRootUri;
        } else if(!PropertyLoader.getProperty(API_ROOT_URI_KEY).isBlank()){
            AppProperty.API_ROOT_URI = PropertyLoader.getProperty(API_ROOT_URI_KEY);
        }else{
            AppProperty.API_ROOT_URI = api_root_uri;
        }

        if(evnConfigData != null){
            AppProperty.CONFIG_DATA = evnConfigData;
        } else if(!PropertyLoader.getProperty(CONFIG_DATA_KEY).isBlank()){
            AppProperty.CONFIG_DATA = PropertyLoader.getProperty(CONFIG_DATA_KEY);
        }else{
            AppProperty.CONFIG_DATA = config_data;
        }

        if(evnTestData != null){
            AppProperty.TEST_DATA = evnTestData;
        } else if(!PropertyLoader.getProperty(TEST_DATA_KEY).isBlank()){
            AppProperty.TEST_DATA = PropertyLoader.getProperty(TEST_DATA_KEY);
        }else{
            AppProperty.TEST_DATA = test_data;
        }

        if(evnCallBackUri != null){
            AppProperty.CALLBACK_URI = evnCallBackUri;
        } else if(!PropertyLoader.getProperty(CALL_BACK_URI_KEY).isBlank()){
            AppProperty.CALLBACK_URI = PropertyLoader.getProperty(CALL_BACK_URI_KEY);
        }else{
            AppProperty.CALLBACK_URI = callback_uri;
        }

        if(evnCallBackPort != null){
            AppProperty.CALLBACK_PORT = evnCallBackPort.trim();
        } else if(!PropertyLoader.getProperty(CALL_BACK_PORT_KEY).isBlank()){
            AppProperty.CALLBACK_PORT = PropertyLoader.getProperty(CALL_BACK_PORT_KEY).trim();
        }else{
            AppProperty.CALLBACK_PORT = callback_port;
        }

        if(evnCallBackWait != null){
            AppProperty.CALLBACK_WAIT = evnCallBackWait.trim();
        } else if(!PropertyLoader.getProperty(CALL_BACK_WAIT_KEY).isBlank()){
            AppProperty.CALLBACK_WAIT = PropertyLoader.getProperty(CALL_BACK_WAIT_KEY).trim();
        }else{
            AppProperty.CALLBACK_WAIT = callback_wait;
        }

        if(evnTestSuite != null){
            AppProperty.TEST_SUITE_NAME = evnTestSuite;
        } else if(!PropertyLoader.getProperty(TEST_SUITE_NAME_KEY).isBlank()){
            AppProperty.TEST_SUITE_NAME = PropertyLoader.getProperty(TEST_SUITE_NAME_KEY);
        }else{
            AppProperty.TEST_SUITE_NAME = test_suite_name;
        }

        if(!PropertyLoader.getProperty(EVENT_PATH_KEY).isBlank()){
            AppProperty.EVENT_PATH = PropertyLoader.getProperty(EVENT_PATH_KEY);
        }else{
            AppProperty.EVENT_PATH = event_path;
        }

        if(!PropertyLoader.getProperty(UPLOAD_CONFIG_PATH_NAME_KEY).isBlank()){
            AppProperty.UPLOAD_CONFIG_PATH = PropertyLoader.getProperty(UPLOAD_CONFIG_PATH_NAME_KEY);
        }else{
            AppProperty.UPLOAD_CONFIG_PATH = upload_config_path;
        }

        if(!PropertyLoader.getProperty(BOOKING_DELAY_KEY).isBlank()){
            AppProperty.BOOKING_DELAY = Integer.parseInt(PropertyLoader.getProperty(BOOKING_DELAY_KEY));
        }else{
            AppProperty.BOOKING_DELAY = booking_delay;
        }


        CALLBACK_URL = TestUtility.getConfigCallbackUrl();

        makeUploadPath();
        isAppDataUploaded = true;
    }

    public static void initByPropertyFile(){
        if(!isAppDataUploaded) {
            AppProperty.API_ROOT_URI = PropertyLoader.getProperty(API_ROOT_URI_KEY);
            AppProperty.CONFIG_DATA = PropertyLoader.getProperty(CONFIG_DATA_KEY);
            AppProperty.TEST_DATA = PropertyLoader.getProperty(TEST_DATA_KEY);
            AppProperty.CALLBACK_URI = PropertyLoader.getProperty(CALL_BACK_URI_KEY);
            AppProperty.CALLBACK_PORT = PropertyLoader.getProperty(CALL_BACK_PORT_KEY).trim();
            AppProperty.CALLBACK_WAIT = PropertyLoader.getProperty(CALL_BACK_WAIT_KEY).trim();
            AppProperty.EVENT_PATH = PropertyLoader.getProperty(EVENT_PATH_KEY);
            AppProperty.TEST_SUITE_NAME = PropertyLoader.getProperty(TEST_SUITE_NAME_KEY);
            AppProperty.UPLOAD_CONFIG_PATH = PropertyLoader.getProperty(UPLOAD_CONFIG_PATH_NAME_KEY);
            makeUploadPath();
            isAppDataUploaded = true;
        }
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

}
