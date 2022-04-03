package org.dcsa.api.validator.config;

import org.dcsa.api.validator.webservice.init.AppProperty;

public class Configuration {
    public static String ROOT_URI;
    public static String API_VERSION;
    public static String testSuite;
    public static String testData;
    public static String CALLBACK_URI;
    public static Integer CALLBACK_PORT;
    public static Integer CALLBACK_WAIT;
    public static String accessToken;
    public static String client_secret;
    public static String client_id;
    public static String audience;
    public static String OUT_PUT_DIR;
    public static String CALLBACK_PATH;


    public static void init() throws Exception {
        client_secret = System.getenv("client_secret");
        client_id = System.getenv("client_id");
        audience = System.getenv("audience");
        testSuite = "config/v2/config.json";
        OUT_PUT_DIR = "reports";
        API_VERSION = "2.2.0";
        CALLBACK_PATH="/v"+Configuration.API_VERSION.split("\\.")[0]+"/notification-endpoints/receive";

        if ( AppProperty.API_ROOT_URI != null )
            ROOT_URI = AppProperty.API_ROOT_URI;
        else
            throw new Exception("API_ROOT_URI is not set as environment variable, please follow the setup document");
        if ( AppProperty.TEST_DATA != null )
            testData = AppProperty.TEST_DATA;
        else {
            testData="testdata.json";
        }
        if ( AppProperty.CALLBACK_URI != null )
            CALLBACK_URI = AppProperty.CALLBACK_URI;
        else {
            CALLBACK_URI="http://localhost:9092";
        }
        if ( AppProperty.CALLBACK_PORT != null )
            CALLBACK_PORT = Integer.parseInt(AppProperty.CALLBACK_PORT);
        else {
            CALLBACK_PORT = 9092;
        }
        if ( AppProperty.CALLBACK_WAIT != null )
            CALLBACK_WAIT = Integer.parseInt(AppProperty.CALLBACK_WAIT);
        else {
            CALLBACK_WAIT = 3600000;
        }
    }
}
