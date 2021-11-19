package org.dcsa.api.validator.hooks;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.http.ContentType;
import org.dcsa.api.validator.config.Configuration;
import org.dcsa.api.validator.model.TestDB;
import org.dcsa.api.validator.util.FileUtility;
import org.dcsa.api.validator.util.JsonParser;
import org.dcsa.api.validator.util.JsonUtility;
import org.dcsa.api.validator.util.TestUtility;
import org.testng.annotations.BeforeSuite;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TestSetup {
    @BeforeSuite(alwaysRun = true)
    public void suiteSetUp() {
        try {
            ObjectMapper mapper = new ObjectMapper(new JsonFactory());
            String jsonString = FileUtility.loadFileAsString("testdata/v2/tnt/template/ValidEventSubscriptionSample.json");
            JsonNode rootNode = mapper.readTree(jsonString);
           JsonParser.process("", rootNode);
           // Map<String,String > data=new HashMap<>();
           // data.put("equipmentReference", "ABC709951");
           // JsonUtility.validateAttributeValueMap(jsonString,data);
            //JsonUtility.addJsonElement(jsonString,"commodities.[0].commodityType","deepak");
        } catch (Exception e) {
            e.printStackTrace();
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

    @Before
    public static void testPreparation(Scenario scenario) {

    }

    @After
    public static void testCleanUp(Scenario scenario) {

    }
}
