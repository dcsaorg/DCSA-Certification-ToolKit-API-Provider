package org.dcsa.api.validator.webservice.init;

import lombok.Data;
import org.dcsa.api.validator.webservice.exception.StorageException;
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
    // TNT service static config
    public static String API_ROOT_URI;
    public static String TEST_DATA;

    public static String CONFIG_DATA;
    public static String CALLBACK_URI;
    public static String CALLBACK_PORT;
    public static String CALLBACK_WAIT;
    public static String TEST_SUITE_NAME;

    public static String EVENT_PATH;
    // HTML report static config
    public static String REPORT_AUTHOR;
    public static String REPORT_COMPANY;
    public static String REPORT_DIRECTORY;
    public static String REPORT_NAME;
    public static String REPORT_TITLE;
    public static String REPORT_THEME;
    public static String REPORT_TIME_FORMAT;
    public static String REPORT_TIMELINE;
    public static String UPLOAD_CONFIG_PATH;
    // TNT service config
    private String api_root_uri;
    private String test_data;
    private String callback_uri;
    private String callback_port;
    private String callback_wait;
    private String test_suite_name;
    // HTML report config
    private String report_author;
    private String report_company;
    private String report_directory;
    private String report_name;
    private String report_title;
    private String report_theme;
    private String report_time_format;
    private String report_timeline;

    private String config_data;
    private String event_path;

    private String upload_config_path;

    public static Path uploadPath;
    public static boolean isAppDataUploaded = false;
    public void init(){
        // TNT service config
        AppProperty.API_ROOT_URI = api_root_uri;
        AppProperty.TEST_DATA = test_data;
        AppProperty.CONFIG_DATA = config_data;
        AppProperty.CALLBACK_URI = callback_uri;
        AppProperty.CALLBACK_PORT = callback_port;
        AppProperty.CALLBACK_WAIT = callback_wait;
        AppProperty.TEST_SUITE_NAME = test_suite_name;
        // HTML report config
        AppProperty.REPORT_AUTHOR = report_author;
        AppProperty.REPORT_COMPANY = report_company;
        AppProperty.REPORT_DIRECTORY = report_directory;
        AppProperty.REPORT_NAME = report_name;
        AppProperty.REPORT_TITLE = report_title;
        AppProperty.REPORT_THEME = report_theme;
        AppProperty.REPORT_TIME_FORMAT = report_time_format;
        AppProperty.REPORT_TIMELINE = report_timeline;
        AppProperty.UPLOAD_CONFIG_PATH = upload_config_path;
        AppProperty.EVENT_PATH = event_path;
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

}
