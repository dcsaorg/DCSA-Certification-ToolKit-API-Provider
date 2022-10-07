package org.dcsa.api.validator.webservice.init;

import lombok.Data;
import org.dcsa.api.validator.webservice.uploader.exception.StorageException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@Configuration
@ConfigurationProperties( prefix = "spring")
public class AppProperty {
    // TNT API keys
    private static final String API_ROOT_URI_KEY = "spring.api_root_uri";
    private static final String CONFIG_DATA_KEY = "spring.config_data";
    private static final String TEST_DATA_KEY = "spring.test_data";
    private static final String CALL_BACK_URI_KEY = "spring.callback_uri";

    private static String EVENT_PATH_KEY ="spring.event_type";
    private static final String CALL_BACK_PORT_KEY = "spring.callback_port";
    private static final String CALL_BACK_WAIT_KEY = "spring.callback_wait";
    private static final String TEST_SUITE_NAME_KEY = "spring.test_suite_name";
    private static final String UPLOAD_CONFIG_PATH_NAME_KEY = "spring.upload_config_path";
    private static final String EVENT_SUBSCRIPTION_SIMULATION_KEY = "spring.event_subscription_simulation";
    // HTML report key
    private static final String REPORT_AUTHOR_KEY = "spring.report_author";
    private static final String REPORT_COMPANY_KEY = "spring.report_company";
    private static final String REPORT_DIRECTORY_KEY = "spring.report_directory";
    private static final String REPORT_NAME_KEY = "spring.report_name";
    private static final String REPORT_TITLE_KEY = "spring.report_title";
    private static final String REPORT_THEME_KEY = "spring.report_theme";
    private static final String REPORT_TIME_FORMAT_KEY = "spring.report_time_format";
    private static final String REPORT_TIMELINE_KEY = "spring.report_timeline";


    // TNT service static config
    public static String API_ROOT_URI;
    public static String CONFIG_DATA;
    public static String TEST_DATA;
    public static String CALLBACK_URI;
    public static String CALLBACK_PORT;
    public static String CALLBACK_WAIT;
    public static String TEST_SUITE_NAME;
    public static String UPLOAD_CONFIG_PATH;
    public static boolean EVENT_SUBSCRIPTION_SIMULATION;

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

    private String url;
    private String password;
    private String username;
    private String db_name;
    private String schema;

    private String event_path;


    // TNT service config
    private String api_root_uri;
    private String config_data;
    private String test_data;
    private String callback_uri;
    private String callback_port;
    private String callback_wait;
    private String test_suite_name;
    private String upload_config_path;
    private String event_subscription_simulation;

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

    public void init(){
        // TNT service config
        AppProperty.API_ROOT_URI = api_root_uri;
        AppProperty.CONFIG_DATA = config_data;
        AppProperty.TEST_DATA = test_data;
        AppProperty.CALLBACK_URI = callback_uri;
        AppProperty.CALLBACK_PORT = callback_port;
        AppProperty.CALLBACK_WAIT = callback_wait;
        AppProperty.TEST_SUITE_NAME = test_suite_name;
        AppProperty.UPLOAD_CONFIG_PATH = upload_config_path;
        AppProperty.EVENT_SUBSCRIPTION_SIMULATION = Boolean.parseBoolean(event_subscription_simulation);
        AppProperty.EVENT_PATH = event_path;
        // HTML report config
        AppProperty.REPORT_AUTHOR = report_author;
        AppProperty.REPORT_COMPANY = report_company;
        AppProperty.REPORT_DIRECTORY = report_directory;
        AppProperty.REPORT_NAME = report_name;
        AppProperty.REPORT_TITLE = report_title;
        AppProperty.REPORT_THEME = report_theme;
        AppProperty.REPORT_TIME_FORMAT = report_time_format;
        AppProperty.REPORT_TIMELINE = report_timeline;

        String evnDbRootUri = System.getenv("DB_HOST_IP");
        if(evnDbRootUri != null){
            AppProperty.DATABASE_URL = url.replace("localhost", evnDbRootUri) ;
        }else{
            AppProperty.DATABASE_URL = url;
        }
        AppProperty.DATABASE_URL = AppProperty.DATABASE_URL+"/"+db_name+"?currentSchema="+schema;
        AppProperty.DATABASE_USER_NAME = username;
        AppProperty.DATABASE_PASSWORD = password;

        makeUploadPath();
        isAppDataUploaded = true;
    }

    public static void initByPropertyFile(){
        AppProperty.API_ROOT_URI = PropertyLoader.getProperty(API_ROOT_URI_KEY);
        AppProperty.CONFIG_DATA = PropertyLoader.getProperty(CONFIG_DATA_KEY);
        AppProperty.TEST_DATA = PropertyLoader.getProperty(TEST_DATA_KEY);
        AppProperty.CALLBACK_URI = PropertyLoader.getProperty(CALL_BACK_URI_KEY);
        AppProperty.CALLBACK_PORT = PropertyLoader.getProperty(CALL_BACK_PORT_KEY);
        AppProperty.CALLBACK_WAIT = PropertyLoader.getProperty(CALL_BACK_WAIT_KEY);
        AppProperty.EVENT_PATH = PropertyLoader.getProperty(EVENT_PATH_KEY);
        AppProperty.TEST_SUITE_NAME = PropertyLoader.getProperty(TEST_SUITE_NAME_KEY);
        AppProperty.UPLOAD_CONFIG_PATH = PropertyLoader.getProperty(UPLOAD_CONFIG_PATH_NAME_KEY);
        AppProperty.EVENT_SUBSCRIPTION_SIMULATION = Boolean.parseBoolean(PropertyLoader.getProperty(EVENT_SUBSCRIPTION_SIMULATION_KEY));

        AppProperty.REPORT_AUTHOR = PropertyLoader.getProperty(REPORT_AUTHOR_KEY);
        AppProperty.REPORT_COMPANY = PropertyLoader.getProperty(REPORT_COMPANY_KEY);
        AppProperty.REPORT_DIRECTORY = PropertyLoader.getProperty(REPORT_DIRECTORY_KEY);
        AppProperty.REPORT_NAME = PropertyLoader.getProperty(REPORT_NAME_KEY);
        AppProperty.REPORT_TITLE = PropertyLoader.getProperty(REPORT_TITLE_KEY);
        AppProperty.REPORT_THEME = PropertyLoader.getProperty(REPORT_THEME_KEY);
        AppProperty.REPORT_TIME_FORMAT = PropertyLoader.getProperty(REPORT_TIME_FORMAT_KEY);
        AppProperty.REPORT_TIMELINE = PropertyLoader.getProperty(REPORT_TIMELINE_KEY);
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

}
