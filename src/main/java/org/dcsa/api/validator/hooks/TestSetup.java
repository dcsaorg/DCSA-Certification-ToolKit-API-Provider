package org.dcsa.api.validator.hooks;

import io.restassured.http.ContentType;
import org.dcsa.api.validator.config.Configuration;
import org.dcsa.api.validator.model.TestContext;
import org.dcsa.api.validator.util.TestUtility;
import org.testng.ITestContext;
import org.testng.TestRunner;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TestSetup {

    public static Map<String, TestContext> TestContexts=new HashMap<>();

    @BeforeSuite(alwaysRun = true)
    public void suiteSetUp() throws Exception {
        try {
            Configuration.init();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        TestUtility.loadTestSuite(Configuration.testSuite);
        TestUtility.loadTestData(Configuration.testData);
        if (Configuration.client_secret == null || Configuration.client_id == null || Configuration.audience == null)
            Configuration.accessToken = "AuthDisabled"; //We set the accessToken to this string, so the tests don't complain about an empty accessToken. This is OK, if auth is disabled, it doesn't matter that the accessToken is bogus
        else {
            Configuration.accessToken = given().with()
                    .contentType(ContentType.URLENC.withCharset("UTF-8"))
                    .formParam("client_secret", Configuration.client_secret)
                    .formParam("client_id", Configuration.client_id)
                    .formParam("grant_type", "client_credentials")
                    .formParam("audience", Configuration.audience)
                    .urlEncodingEnabled(true)
                    .when()
                    .post(System.getenv("OAuthTokenUri")).jsonPath().getString("access_token");
        }
    }

    @BeforeTest
    public void setup(ITestContext ctx) {
        TestRunner runner = (TestRunner) ctx;
        runner.setOutputDirectory(Configuration.OUT_PUT_DIR);
    }

}
