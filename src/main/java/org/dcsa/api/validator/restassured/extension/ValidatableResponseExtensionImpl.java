package org.dcsa.api.validator.restassured.extension;

import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import io.restassured.response.Response;
import lombok.Data;
import org.dcsa.api.validator.constants.StatusCode;
import org.dcsa.api.validator.constants.ValidationCode;
import org.dcsa.api.validator.model.TestCase;
import org.dcsa.api.validator.util.JsonUtility;
import org.dcsa.api.validator.util.TestUtility;
import org.testng.Assert;
import org.testng.Reporter;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@Data
public class ValidatableResponseExtensionImpl implements ValidatableResponseExtension {
    private Response response;
    private TestCase testContext;
    private String apiName;

    @Override
    public ValidatableResponseExtension created(StatusCode statusCode) {
        if (statusCode == StatusCode.OK) {
            header(StatusCode.OK);
            try {
                response.then()
                        .assertThat().statusCode(201);
                Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
            try {
                response.then()
                        .assertThat().body("size()", greaterThanOrEqualTo(1));
                Reporter.log("Validation Step-Response body validation:Passed");
            } catch (AssertionError e) {
                Reporter.log("Validation Step-Response body validation:Failed, Reason:No Response body");
                Assert.fail("No Response body");
            }
/*            try {
                response.then()
                        .assertThat().body(TestUtility.getIdentifierAttribute(apiName), notNullValue());
                Reporter.log("Validation Step-Mandatory Attributes in response:Passed");
            } catch (AssertionError e) {
                Reporter.log("Validation Step-Mandatory Attributes in response:Failed, Reason:Missing Mandatory attribute=" + TestUtility.getIdentifierAttribute(apiName));
                Assert.fail("Missing Mandatory attribute=" + TestUtility.getIdentifierAttribute(apiName));
            }*/

        } else {
            try {
                response.then()
                        .assertThat().statusCode(not(anyOf(is(201), is(202), is(204))));
                Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                // Assert.fail("Invalid Http code=" + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
        }
        return this;
    }

    @Override
    public ValidatableResponseExtension found(StatusCode statusCode) {
        if (statusCode == StatusCode.OK) {
            try {
                response.then()
                        .assertThat().statusCode(anyOf(is(200)));
                Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
            try {
                response.then()
                        .assertThat().body("size()", greaterThanOrEqualTo(1));
                Reporter.log("Validation Step-Response Size:Passed");
            } catch (AssertionError e) {
                Reporter.log("Validation Step-Response Size:Failed, Reason:No Response body");
                Assert.fail("No Response body");
            }
        } else {
            try {
                response.then()
                        .assertThat().statusCode(not(anyOf(is(200), is(202), is(204))));
                Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                // Assert.fail("Invalid Http code=" + response.getStatusCode());
                Assert.fail(e.getMessage());
            }

        }
        return this;
    }

    @Override
    public ValidatableResponseExtension deleted(StatusCode statusCode) {
        if (statusCode == StatusCode.OK) {
            try {
                response.then()
                        .assertThat().statusCode(anyOf(is(200), is(204)));
                Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
        } else {
            try {
                response.then()
                        .assertThat().statusCode(not(anyOf(is(200), is(202), is(204))));
                Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
        }
        return this;
    }

    @Override
    public ValidatableResponseExtension modified(StatusCode statusCode) {
        if (statusCode == StatusCode.OK) {

            try {
                response.then()
                        .assertThat()
                        .statusCode(anyOf(is(200)));
                Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                Assert.fail("Invalid Http code=" + response.getStatusCode());
            }
            try {
                response.then()
                        .assertThat().body("size()", greaterThanOrEqualTo(1));
                Reporter.log("Validation Step-Response body validation:Passed");
            } catch (AssertionError e) {
                Reporter.log("Validation Step-Response body validation:Failed, Reason:No Response body");
                Assert.fail("No Response body");
            }
        } else {
            try {
                response.then()
                        .assertThat()
                        .statusCode(not(anyOf(is(200), is(201), is(202), is(204))));
                Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                Assert.fail("Invalid Http code=" + response.getStatusCode());
            }
        }
        return this;
    }

    public ValidatableResponseExtension foundAll(StatusCode statusCode) {

        if (statusCode == StatusCode.OK) {
            try {
                response.then().assertThat()
                        .statusCode(anyOf(is(200)));
                Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
            try {
                response.then().assertThat().body(TestUtility.getIdentifierAttribute(apiName) + ".size()", greaterThanOrEqualTo(1));
                Reporter.log("Validation Step-Response body validation:Passed");
            } catch (AssertionError e) {
                Reporter.log("Validation Step-Response body validation:Failed, Reason:No Response body");
                Assert.fail(e.getMessage());
            }
            Map<String, String> queryParameters = null;
            List<Map<String, Object>> responseList = response.body().path("$");
            if (testContext.getRequest().getQueryParameters() != null)
                queryParameters = testContext.getRequest().getQueryParameters();
            if (queryParameters != null) {
                if (queryParameters.containsKey("limit")) {
                    if (responseList.size() <= Integer.valueOf(queryParameters.get("limit"))) {
                        Reporter.log("Validation Step-Limit validation:Passed");
                    } else {
                        Reporter.log("Validation Step-Limit validation:Failed, Reason:number of records more than limit");
                        Assert.fail("Validation Step-Limit validation:Failed, Reason:number of records more than limit");
                    }
                }
                queryParameters.remove("limit");
                queryParameters.remove("cursor");
                queryParameters.remove("sort");
                if (queryParameters.size() > 0) {

                    Boolean isValid = true;
                    for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
                        isValid = JsonUtility.validateAttributeValue(responseList, entry.getKey(), entry.getValue());
                        if (!isValid) {
                            Reporter.log("Validation Step-Filter validation:Failed, Reason:Value mismatch for " + entry.getKey());
                            Assert.fail("Value mismatch for query parameter " + entry.getKey() + " in response");
                            break;
                        }
                    }
                    if (isValid)
                        Reporter.log("Validation Step-Filter validation:Passed");
                }
            }
        } else {
            try {
                response.then()
                        .statusCode(anyOf(is(404)));
                Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
        }
        return this;
    }

    @Override
    public ValidatableResponseExtension header(StatusCode statusCode) {
        if (statusCode == StatusCode.OK) {
            String apiVersion = response.getHeader("API-Version");
            String contentType = response.header("Content-Type");
            String contentEncoding = response.header("Content-Encoding");
            if (contentType == null || !contentType.contains("json"))
                Reporter.log("Validation Step-Header validation(Optional):Failed, Reason:Content-Type is not valid");
            else if (apiVersion == null)
                Reporter.log("Validation Step-Header validation(Optional):Failed, Reason:API-Version is missing");
                // else if (!contentEncoding.contains("UTF"))
                //   Reporter.log("Validation Step-Header validation(Optional):Failed, Reason:Content-Encoding is not valid");
            else
                Reporter.log("Validation Step-Header validation(Optional):Passed");
        }
        return this;
    }

    @Override
    public ValidatableResponseExtension schema(ValidationCode validationCode) {
        if (validationCode == ValidationCode.VALID) {
            JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.newBuilder().setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV4).freeze()).freeze();
            if (TestUtility.getResponseSchema(apiName) != null) {
                try {
                    response.then().assertThat().body(matchesJsonSchemaInClasspath(TestUtility.getResponseSchema(apiName)).
                            using(jsonSchemaFactory));
                    Reporter.log("Validation Step-Schema validation(Optional):Passed");
                } catch (AssertionError e) {
                    Reporter.log("Validation Step-Schema validation(Optional):Failed");
                    if (validationCode == ValidationCode.VALID) {
                        //Make it fail in case it is mandatory
                        //Assert.fail("Schema validation failed");
                    }
                }
            }
        }
        return this;
    }

    @Override
    public ValidatableResponseExtension assertThat() {
        return this;
    }

    @Override
    public ValidatableResponseExtension statusCode(int expectedHttpCode) {
        try {
            Assert.assertEquals(response.getStatusCode(),expectedHttpCode);
            Reporter.log("Validation Step-Http Code Validation:Passed");
        } catch (AssertionError e) {
            Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
            Assert.fail("Invalid Http code=" + response.getStatusCode());

        }
        return this;
    }

}
