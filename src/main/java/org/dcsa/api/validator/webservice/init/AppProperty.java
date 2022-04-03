package org.dcsa.api.validator.webservice.init;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties( prefix = "app")
public class AppProperty {
    // TNT service static config
    public static String API_ROOT_URI;
    public static String TEST_DATA;
    public static String CALLBACK_URI;
    public static String CALLBACK_PORT;
    public static String CALLBACK_WAIT;
    public static String TEST_SUITE_NAME;
    // HTML report static config
    public static String REPORT_AUTHOR;
    public static String REPORT_COMPANY;
    public static String REPORT_DIRECTORY;
    public static String REPORT_NAME;
    public static String REPORT_TITLE;
    public static String REPORT_THEME;
    public static String REPORT_TIME_FORMAT;
    public static String REPORT_TIMELINE;
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

    public void init(){
        // TNT service config
        AppProperty.API_ROOT_URI = api_root_uri;
        AppProperty.TEST_DATA = test_data;
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
    }

}
