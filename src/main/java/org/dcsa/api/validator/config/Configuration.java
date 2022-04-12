package org.dcsa.api.validator.config;

import org.dcsa.api.validator.webservice.init.AppProperty;

public class Configuration {
    public static String ROOT_URI;
    public static String API_VERSION;
    public static String configData;
    public static String testData;
    public static String CALLBACK_URI;
    public static Integer CALLBACK_PORT;
    public static Integer CALLBACK_WAIT;
    public static String accessToken;
    public static String client_secret;
    public static String client_id;
    public static String audience;
    public static String CALLBACK_PATH;


    public static void init() throws Exception {
        client_secret = System.getenv("client_secret");
        client_id = System.getenv("client_id");
        audience = System.getenv("audience");
        API_VERSION = "2.2.0";
        CALLBACK_PATH="/v"+Configuration.API_VERSION.split("\\.")[0]+"/notification-endpoints/receive";

        String evnApiRootUri = System.getenv("API_ROOT_URI");
        String evnConfigData = System.getenv("CONFIG_DATA");
        String evnTestData = System.getenv("TEST_DATA");
        String evnCallBackUri = System.getenv("CALLBACK_URI");
        String evnCallBackPort = System.getenv("CALLBACK_PORT");
        String evnCallBackWait = System.getenv("CALLBACK_WAIT");

        if (AppProperty.API_ROOT_URI != null  || evnApiRootUri != null) {
            if(evnApiRootUri != null){
                AppProperty.API_ROOT_URI = evnApiRootUri;
            }
            ROOT_URI = AppProperty.API_ROOT_URI;
        }
        else {
            throw new Exception("API_ROOT_URI is not set as environment variable, please follow the setup document");
        }
        if ( AppProperty.CONFIG_DATA != null || evnConfigData != null ) {
            if (evnConfigData != null) {
                AppProperty.CONFIG_DATA = evnConfigData;
            }
            configData = AppProperty.CONFIG_DATA;
        }
        else {
            configData="config/v2/config.json";
        }
        if ( AppProperty.TEST_DATA != null || evnTestData != null ) {
            if (evnTestData != null) {
                AppProperty.TEST_DATA = evnTestData;
            }
            testData = AppProperty.TEST_DATA;
        }
        else {
            testData="config/v2/testdata.json";
        }
        if ( AppProperty.CALLBACK_URI != null || evnCallBackUri != null ) {
            if(evnCallBackUri != null){
                AppProperty.CALLBACK_URI = evnCallBackUri;
            }
            CALLBACK_URI = AppProperty.CALLBACK_URI;
        }
        else {
            CALLBACK_URI="http://localhost:9092";
        }
        if ( AppProperty.CALLBACK_PORT != null || evnCallBackPort != null ) {
            if(evnCallBackPort != null){
                AppProperty.CALLBACK_PORT = evnCallBackPort;
            }
            CALLBACK_PORT = Integer.parseInt(AppProperty.CALLBACK_PORT.trim());
        }
        else {
            CALLBACK_PORT = 9092;
        }
        if ( AppProperty.CALLBACK_WAIT != null || evnCallBackWait != null ) {
            if(evnCallBackWait != null){
                AppProperty.CALLBACK_WAIT = evnCallBackWait;
            }
            CALLBACK_WAIT = Integer.parseInt(AppProperty.CALLBACK_WAIT.trim());
        }
        else {
            CALLBACK_WAIT = 3600000;
        }
    }
}
