package org.dcsa.api.validator.hook;

import io.restassured.http.ContentType;
import org.dcsa.api.validator.model.TestContext;
import org.dcsa.api.validator.util.TestUtility;
import org.dcsa.api.validator.webhook.SparkWebHook;
import org.dcsa.api.validator.webservice.init.AppProperty;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TestSetup {

    public static Map<String, TestContext> TestContexts = new HashMap<>();
    public static SparkWebHook sparkWebHook;

    @BeforeSuite(alwaysRun = true)
    public void suiteSetUp() throws Exception {
        try {
            sparkWebHook = new SparkWebHook();
            sparkWebHook.startServer();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        if(TestUtility.getTestDB() == null || TestUtility.getTestDataSet() == null ){
            TestUtility.loadConfigDataFromResource(AppProperty.CONFIG_DATA);
            TestUtility.loadTestDataFromResource(AppProperty.TEST_DATA);
        }
        if (AppProperty.CLIENT_SECRET == null || AppProperty.CLIENT_ID == null || AppProperty.AUDIENCE == null)
            AppProperty.ACCESS_TOKEN = "AuthDisabled"; //We set the accessToken to this string, so the tests don't complain about an empty accessToken. This is OK, if auth is disabled, it doesn't matter that the accessToken is bogus
        else {
            AppProperty.ACCESS_TOKEN = given().with()
                    .contentType(ContentType.URLENC.withCharset("UTF-8"))
                    .formParam("client_secret", AppProperty.CLIENT_SECRET)
                    .formParam("client_id", AppProperty.CLIENT_ID)
                    .formParam("grant_type", "client_credentials")
                    .formParam("audience", AppProperty.AUDIENCE)
                    .urlEncodingEnabled(true)
                    .when()
                    .post(System.getenv("OAuthTokenUri")).jsonPath().getString("access_token");
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() {
        sparkWebHook.stopServer();
        System.out.println("Please check the reports at "+AppProperty.REPORT_DIRECTORY);
    }

}
