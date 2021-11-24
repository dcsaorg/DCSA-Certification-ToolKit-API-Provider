package org.dcsa.api.validator.config;

public class Configuration {

    public final static String ROOT_URI = "http://localhost:9090/" ;
    public final static String API_VERSION = "2" ;
    public final static String testSuite="testdata/v2/tnt/testdb/config.json";
    public final static String testData="testdata/v2/tnt/testdb/testdata.json";
    public final static String CALLBACK_URI = "http://localhost:8085" ;
    public final static Integer CALLBACK_PORT = 8085 ;
    public static String accessToken;
    public static String client_secret= System.getenv("client_secret");
    public static String client_id= System.getenv("client_id");
    public static String audience= System.getenv("audience");
}
