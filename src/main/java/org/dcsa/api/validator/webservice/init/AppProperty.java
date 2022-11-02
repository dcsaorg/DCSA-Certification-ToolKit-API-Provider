package org.dcsa.api.validator.webservice.init;

import lombok.Data;
import lombok.extern.java.Log;
import org.dcsa.api.validator.webservice.uploader.exception.StorageException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@Log
@Configuration
@ConfigurationProperties( prefix = "spring")
public class AppProperty {

    public static String RESOURCE_FILENAME = "application.properties";
    // TNT API keys
    private static final String API_ROOT_URI_KEY = "spring.api_root_uri";
    private static final String CONFIG_DATA_KEY = "spring.config_data";
    private static final String TEST_DATA_KEY = "spring.test_data";
    private static final String CALL_BACK_URI_KEY = "spring.callback_uri";

    private static String EVENT_PATH_KEY ="spring.event_path";
    private static final String CALL_BACK_PORT_KEY = "spring.callback_port";
    private static final String CALL_BACK_WAIT_KEY = "spring.callback_wait";
    private static final String TEST_SUITE_NAME_KEY = "spring.test_suite_name";
    private static final String UPLOAD_CONFIG_PATH_NAME_KEY = "spring.upload_config_path";
    // HTML report key
    private static final String REPORT_AUTHOR_KEY = "spring.report_author";
    private static final String REPORT_COMPANY_KEY = "spring.report_company";
    private static final String REPORT_DIRECTORY_KEY = "spring.report_directory";
    private static final String REPORT_NAME_KEY = "spring.report_name";
    private static final String REPORT_TITLE_KEY = "spring.report_title";
    private static final String REPORT_THEME_KEY = "spring.report_theme";
    private static final String REPORT_TIME_FORMAT_KEY = "spring.report_time_format";
    private static final String REPORT_TIMELINE_KEY = "spring.report_timeline";
    private static final String DATABASE_NAME_KEY = "spring.db_name";
    private static final String DATABASE_USERNAME_KEY = "spring.username";
    private static final String DATABASE_PASSWORD_KEY = "spring.password";
    private static final String DATABASE_URL_KEY = "spring.url";
    private static final String DATABASE_SCHEMA_KEY = "spring.schema";
    private static final String DATABASE_IP_KEY = "spring.db_ip";

    // TNT service static config
    public static String API_ROOT_URI;
    public static String CONFIG_DATA;
    public static String TEST_DATA;
    public static String CALLBACK_URI;
    public static int CALLBACK_PORT;
    public static int CALLBACK_WAIT;
    public static String TEST_SUITE_NAME;
    public static String UPLOAD_CONFIG_PATH;

    // HTML report static config
    public static String REPORT_AUTHOR;
    public static String REPORT_COMPANY;
    public static String REPORT_DIRECTORY;
    public static String REPORT_NAME;
    public static String REPORT_TITLE;
    public static String REPORT_THEME;
    public static String REPORT_TIME_FORMAT;
    public static String REPORT_TIMELINE;

    public static String DATABASE_URL;
    public static String DATABASE_USER_NAME;
    public static String DATABASE_PASSWORD;
    public static String EVENT_PATH;

    public static String DATABASE_IP;
    public static String API_VERSION = "2.2.0";
/*    public static String CONFIG_DATA;
    public static String TEST_DATA;
    public static String CALLBACK_URI;
    public static Integer CALLBACK_PORT;
    public static Integer CALLBACK_WAIT;*/
    public static String ACCESS_TOKEN;
    public static String CLIENT_SECRET;
    public static String CLIENT_ID;
    public static String AUDIENCE;
    public static String CALLBACK_PATH;

    public static String SUBSCRIPTION_ID;

    private String url;
    private String password;
    private String username;
    private String db_name;
    private String schema;
    private String db_ip;

    private String event_path;


    // TNT service config
    private String api_root_uri;
    private String config_data;
    private String test_data;
    private String callback_uri;
    private int callback_port;
    private int callback_wait;
    private String test_suite_name;
    private String upload_config_path;

    // HTML report config
    private String report_author;
    private String report_company;
    private String report_directory;
    private String report_name;
    private String report_title;
    private String report_theme;
    private String report_time_format;
    private String report_timeline;

    public static Path uploadPath;
    public static boolean isAppDataUploaded = false;

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
        String evnDbHostIp = System.getenv("DB_HOST_IP");

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
            AppProperty.CALLBACK_PORT = Integer.parseInt(evnCallBackPort.trim());
        } else if(!PropertyLoader.getProperty(CALL_BACK_PORT_KEY).isBlank()){
            AppProperty.CALLBACK_PORT = Integer.parseInt(PropertyLoader.getProperty(CALL_BACK_PORT_KEY).trim());
        }else{
            AppProperty.CALLBACK_PORT = callback_port;
        }

        if(evnCallBackWait != null){
            AppProperty.CALLBACK_WAIT = Integer.parseInt(evnCallBackWait.trim());
        } else if(!PropertyLoader.getProperty(CALL_BACK_WAIT_KEY).isBlank()){
            AppProperty.CALLBACK_WAIT = Integer.parseInt(PropertyLoader.getProperty(CALL_BACK_WAIT_KEY).trim());
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

        if(evnDbHostIp != null){
            AppProperty.DATABASE_IP = evnDbHostIp;
        } else if(!PropertyLoader.getProperty(DATABASE_IP_KEY).isBlank()){
            AppProperty.DATABASE_IP = PropertyLoader.getProperty(DATABASE_IP_KEY);
        }else{
            AppProperty.DATABASE_IP = db_ip;
        }
        // Report config
        if(!PropertyLoader.getProperty(REPORT_AUTHOR_KEY).isBlank()){
            AppProperty.REPORT_AUTHOR = PropertyLoader.getProperty(REPORT_AUTHOR_KEY);
        }else{
            AppProperty.REPORT_AUTHOR = report_author;
        }

        if(!PropertyLoader.getProperty(REPORT_COMPANY_KEY).isBlank()){
            AppProperty.REPORT_COMPANY = PropertyLoader.getProperty(REPORT_COMPANY_KEY);
        }else{
            AppProperty.REPORT_COMPANY = report_company;
        }

        if(!PropertyLoader.getProperty(REPORT_DIRECTORY_KEY).isBlank()){
            AppProperty.REPORT_DIRECTORY = PropertyLoader.getProperty(REPORT_DIRECTORY_KEY);
        }else{
            AppProperty.REPORT_DIRECTORY = report_directory;
        }

        if(!PropertyLoader.getProperty(REPORT_NAME_KEY).isBlank()){
            AppProperty.REPORT_NAME = PropertyLoader.getProperty(REPORT_NAME_KEY);
        }else{
            AppProperty.REPORT_NAME = report_name;
        }

        if(!PropertyLoader.getProperty(REPORT_TITLE_KEY).isBlank()){
            AppProperty.REPORT_TITLE = PropertyLoader.getProperty(REPORT_TITLE_KEY);
        }else{
            AppProperty.REPORT_TITLE = report_title;
        }

        if(!PropertyLoader.getProperty(REPORT_THEME_KEY).isBlank()){
            AppProperty.REPORT_THEME = PropertyLoader.getProperty(REPORT_THEME_KEY);
        }else{
            AppProperty.REPORT_THEME = report_theme;
        }

        if(!PropertyLoader.getProperty(REPORT_TIMELINE_KEY).isBlank()){
            AppProperty.REPORT_TIMELINE = PropertyLoader.getProperty(REPORT_TIMELINE_KEY);
        }else{
            AppProperty.REPORT_TIMELINE = report_timeline;
        }

        if(!PropertyLoader.getProperty(REPORT_TIME_FORMAT_KEY).isBlank()){
            AppProperty.REPORT_TIME_FORMAT = PropertyLoader.getProperty(REPORT_TIME_FORMAT_KEY);
        }else{
            AppProperty.REPORT_TIME_FORMAT = report_time_format;
        }
        initDatabaseProperties();
        makeUploadPath();
        isAppDataUploaded = true;
    }

    private static void initDatabaseProperties(){
        String evnDbRootUri = System.getenv("DB_HOST_IP");
        if(evnDbRootUri != null){
            AppProperty.DATABASE_URL = PropertyLoader.getProperty(DATABASE_URL_KEY).replace("localhost", evnDbRootUri) ;
        }else{
            AppProperty.DATABASE_URL = PropertyLoader.getProperty(DATABASE_URL_KEY);
        }
        AppProperty.DATABASE_USER_NAME = PropertyLoader.getProperty(DATABASE_USERNAME_KEY);
        AppProperty.DATABASE_PASSWORD = PropertyLoader.getProperty(DATABASE_PASSWORD_KEY);
    }


    public static void initByPropertyFile(){
        if(!isAppDataUploaded) {
            AppProperty.API_ROOT_URI = PropertyLoader.getProperty(API_ROOT_URI_KEY);
            AppProperty.CONFIG_DATA = PropertyLoader.getProperty(CONFIG_DATA_KEY);
            AppProperty.TEST_DATA = PropertyLoader.getProperty(TEST_DATA_KEY);
            AppProperty.CALLBACK_URI = PropertyLoader.getProperty(CALL_BACK_URI_KEY);
            AppProperty.CALLBACK_PORT = Integer.parseInt(PropertyLoader.getProperty(CALL_BACK_PORT_KEY).trim());
            AppProperty.CALLBACK_WAIT = Integer.parseInt(PropertyLoader.getProperty(CALL_BACK_WAIT_KEY).trim());
            AppProperty.EVENT_PATH = PropertyLoader.getProperty(EVENT_PATH_KEY);
            AppProperty.TEST_SUITE_NAME = PropertyLoader.getProperty(TEST_SUITE_NAME_KEY);
            AppProperty.UPLOAD_CONFIG_PATH = PropertyLoader.getProperty(UPLOAD_CONFIG_PATH_NAME_KEY);

            AppProperty.REPORT_AUTHOR = PropertyLoader.getProperty(REPORT_AUTHOR_KEY);
            AppProperty.REPORT_COMPANY = PropertyLoader.getProperty(REPORT_COMPANY_KEY);
            AppProperty.REPORT_DIRECTORY = PropertyLoader.getProperty(REPORT_DIRECTORY_KEY);
            AppProperty.REPORT_NAME = PropertyLoader.getProperty(REPORT_NAME_KEY);
            AppProperty.REPORT_TITLE = PropertyLoader.getProperty(REPORT_TITLE_KEY);
            AppProperty.REPORT_THEME = PropertyLoader.getProperty(REPORT_THEME_KEY);
            AppProperty.REPORT_TIME_FORMAT = PropertyLoader.getProperty(REPORT_TIME_FORMAT_KEY);
            AppProperty.REPORT_TIMELINE = PropertyLoader.getProperty(REPORT_TIMELINE_KEY);

            initDatabaseProperties();
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
