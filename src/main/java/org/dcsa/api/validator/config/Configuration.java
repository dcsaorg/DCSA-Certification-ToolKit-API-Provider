package org.dcsa.api.validator.config;

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

    public static void init() throws Exception {
        client_secret = System.getenv("client_secret");
        client_id = System.getenv("client_id");
        audience = System.getenv("audience");
        testSuite = "config/v2/config.json";
        OUT_PUT_DIR = "reports";
        API_VERSION = "2.2.0";
        if (System.getenv("API_ROOT_URI") != null)
            ROOT_URI = System.getenv("API_ROOT_URI");
        else
            throw new Exception("API_ROOT_URI is not set as environment variable, please follow the setup document");
        if (System.getenv("DATA_CONFIG") != null)
            testData = System.getenv("DATA_CONFIG");
        else {
            testData="dataconfig.json";
        }
        if (System.getenv("CALLBACK_URI") != null)
            CALLBACK_URI = System.getenv("CALLBACK_URI");
        else {
            CALLBACK_URI="http://localhost:8085";
            //throw new Exception("CALLBACK_URI is not set as environment variable, please follow the setup document");
        }
        if (System.getenv("CALLBACK_PORT") != null)
            CALLBACK_PORT = Integer.parseInt(System.getenv("CALLBACK_PORT"));
        else {
            CALLBACK_PORT = 8085;
            //throw new Exception("CALLBACK_PORT is not set as environment variable, please follow the setup document");
        }
        if (System.getenv("CALLBACK_WAIT") != null)
            CALLBACK_WAIT = Integer.parseInt(System.getenv("CALLBACK_WAIT"));
        else {
            CALLBACK_WAIT = 20000;
           // throw new Exception("CALLBACK_WAIT is not set as environment variable, please follow the setup document");
        }


    }
}
