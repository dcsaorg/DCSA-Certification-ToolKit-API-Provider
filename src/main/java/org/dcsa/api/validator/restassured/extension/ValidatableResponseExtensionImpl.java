package org.dcsa.api.validator.restassured.extension;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.filter.ValueNodes;
import io.restassured.response.Response;
import lombok.Data;
import org.dcsa.api.validator.constants.StatusCode;
import org.dcsa.api.validator.constants.ValidationCode;
import org.dcsa.api.validator.constants.ValidationType;
import org.dcsa.api.validator.model.TestContext;
import org.dcsa.api.validator.model.ValidationResult;
import org.dcsa.api.validator.util.FileUtility;
import org.dcsa.api.validator.util.JsonUtility;
import org.dcsa.api.validator.util.TestUtility;
import org.testng.Assert;
import org.testng.Reporter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Data
public class ValidatableResponseExtensionImpl implements ValidatableResponseExtension {
    private TestContext testContext;

    public ValidatableResponseExtensionImpl(TestContext testContext) {
        this.testContext = testContext;
    }

    @Override
    public ValidatableResponseExtension created(StatusCode statusCode) {
        Response response = getResponse();
        List<Map<ValidationType, String>> validations = new ArrayList<>();

        if (statusCode == StatusCode.OK) {
            header(StatusCode.OK);
            try {
                response.then()
                        .assertThat().statusCode(201);
                addValidation(ValidationType.HTTPCODE, "Passed");
                //  Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: in (201) but received " + response.getStatusCode());
                //Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
            try {
                response.then()
                        .assertThat().body("size()", greaterThanOrEqualTo(1));
                addValidation(ValidationType.RESPONSEBODY, "Passed");
                // Reporter.log("Validation Step-Response body validation:Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.RESPONSEBODY, "Failed, Reason:No Response body");
                testContext.setReasonOfFailure("Empty Response body");
                //  Reporter.log("Validation Step-Response body validation:Failed, Reason:No Response body");
                Assert.fail("Empty Response Body");
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
                addValidation(ValidationType.HTTPCODE, "Passed");
                //  Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                // Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                // Assert.fail("Invalid Http code=" + response.getStatusCode());
                //Expected status code: not (is <200> or is <202> or is <204>) but received <200>
                testContext.setReasonOfFailure("Expected status code: not in (201,202,204) but received " + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
        }
        return this;
    }

    @Override
    public ValidatableResponseExtension found(StatusCode statusCode) {
        Response response = getResponse();
        if (statusCode == StatusCode.OK) {
            try {
                response.then()
                        .assertThat().statusCode(anyOf(is(200)));
                addValidation(ValidationType.HTTPCODE, "Passed");
                // Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: in (200) but received " + response.getStatusCode());
                // Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
            try {
                response.then()
                        .assertThat().body("size()", greaterThanOrEqualTo(1));
                addValidation(ValidationType.HTTPCODE, "Passed");
                //   Reporter.log("Validation Step-Response Size:Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Empty Response body");
                // Reporter.log("Validation Step-Response Size:Failed, Reason:No Response body");
                Assert.fail("Empty Response Body");
            }
        } else {
            try {
                response.then()
                        .assertThat().statusCode(not(anyOf(is(200), is(202), is(204))));
                addValidation(ValidationType.HTTPCODE, "Passed");
                // Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: not in (200,202,204) but received " + response.getStatusCode());
                //  Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                Assert.fail(e.getMessage());
            }

        }
        return this;
    }

    @Override
    public ValidatableResponseExtension deleted(StatusCode statusCode) {
        Response response = getResponse();
        if (statusCode == StatusCode.OK) {
            try {
                response.then()
                        .assertThat().statusCode(anyOf(is(200), is(204)));
                addValidation(ValidationType.HTTPCODE, "Passed");
                // Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: in (200,204) but received " + response.getStatusCode());
                // Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
        } else {
            try {
                response.then()
                        .assertThat().statusCode(not(anyOf(is(200), is(202), is(204))));
                addValidation(ValidationType.HTTPCODE, "Passed");
                //Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: not in (200,202,204) but received " + response.getStatusCode());
                //  Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
        }
        return this;
    }

    @Override
    public ValidatableResponseExtension modified(StatusCode statusCode) {
        Response response = getResponse();
        if (statusCode == StatusCode.OK) {

            try {
                response.then()
                        .assertThat()
                        .statusCode(anyOf(is(200)));
                addValidation(ValidationType.HTTPCODE, "Passed");
                // Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: in (200) but received " + response.getStatusCode());
                //  Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
            try {
                response.then()
                        .assertThat().body("size()", greaterThanOrEqualTo(1));
                addValidation(ValidationType.RESPONSEBODY, "Passed");
                //Reporter.log("Validation Step-Response body validation:Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.RESPONSEBODY, "Failed, Reason:No Response body");
                //  Reporter.log("Validation Step-Response body validation:Failed, Reason:No Response body");
                testContext.setReasonOfFailure("Empty Response Body");
                Assert.fail("Empty Response body");
            }
        } else {
            try {
                response.then()
                        .assertThat()
                        .statusCode(not(anyOf(is(200), is(201), is(202), is(204))));
                addValidation(ValidationType.HTTPCODE, "Passed");
                //Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: not in (200,201,202,204) but received " + response.getStatusCode());
                //  Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
        }
        return this;
    }

    public ValidatableResponseExtension foundAll(StatusCode statusCode) {
        Response response = getResponse();
        if (statusCode == StatusCode.OK) {
            try {
                response.then().assertThat()
                        .statusCode(anyOf(is(200)));
                addValidation(ValidationType.HTTPCODE, "Passed");
                //  Reporter.log("Validation Step-Http Code Validation:Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: in (200) but received " + response.getStatusCode());
                // Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
            try {
                response.then().assertThat().body(TestUtility.getIdentifierAttribute(testContext.getApiName()) + ".size()", greaterThanOrEqualTo(1));
                addValidation(ValidationType.RESPONSEBODY, "Passed");
                // Reporter.log("Validation Step-Response body validation:Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.RESPONSEBODY, "Failed, Reason:Empty Response Body");
                testContext.setReasonOfFailure("Empty Response Body");
                //  Reporter.log("Validation Step-Response body validation:Failed, Reason:No Response body");
                Assert.fail(e.getMessage());
            }
            Map<String, String> queryParameters = null;
            List<Map<String, Object>> responseList = response.body().path("$");
            if (testContext.getTestCase().getRequest().getQueryParameters() != null)
                queryParameters = testContext.getTestCase().getRequest().getQueryParameters();
            if (queryParameters != null) {
                if (queryParameters.containsKey("limit")) {
                    if (responseList.size() <= Integer.valueOf(queryParameters.get("limit"))) {
                        addValidation(ValidationType.LIMIT, "Passed");
                        // Reporter.log("Validation Step-Limit validation:Passed");
                    } else {
                        addValidation(ValidationType.LIMIT, "Failed, Reason:number of records more than limit");
                        //   Reporter.log("Validation Step-Limit validation:Failed, Reason:number of records more than limit");
                        testContext.setReasonOfFailure("number of records more than limit:" + Integer.valueOf(queryParameters.get("limit")));
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
                            addValidation(ValidationType.FILTER, "Failed, Reason:Value mismatch for " + entry.getKey());
                            testContext.setReasonOfFailure("Value mismatch query parameter : " + entry.getKey());
                            // Reporter.log("Validation Step-Filter validation:Failed, Reason:Value mismatch for " + entry.getKey());
                            Assert.fail("Value mismatch for query parameter " + entry.getKey() + " in response");
                            break;
                        }
                    }
                    if (isValid) {
                        addValidation(ValidationType.FILTER, "Passed");
                        //  Reporter.log("Validation Step-Filter validation:Passed");
                    }
                }
            }
        } else {
            try {
                response.then()
                        .statusCode(anyOf(is(404)));
                //  Reporter.log("Validation Step-Http Code Validation:Passed");
                addValidation(ValidationType.HTTPCODE, "Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: in (404) but received " + response.getStatusCode());
                //Reporter.log("Validation Step-Http Code Validation:Failed, Reason:Invalid Http code=" + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
        }
        return this;
    }

    @Override
    public ValidatableResponseExtension header(StatusCode statusCode) {
        Response response = getResponse();
        if (statusCode == StatusCode.OK) {
            String apiVersion = response.getHeader("API-Version");
            String contentType = response.header("Content-Type");
            String contentEncoding = response.header("Content-Encoding");
            if (contentType == null || !contentType.contains("json")) {
                addValidation(ValidationType.HTTPHEADER, "Failed, Reason:Content-Type is not valid");
                // Reporter.log("Validation Step-Header validation(Optional):Failed, Reason:Content-Type is not valid");
            } else if (apiVersion == null) {
                addValidation(ValidationType.HTTPHEADER, "Failed, Reason:API-Version is missing");
                // Reporter.log("Validation Step-Header validation(Optional):Failed, Reason:API-Version is missing");
            }
            // else if (!contentEncoding.contains("UTF"))
            //   Reporter.log("Validation Step-Header validation(Optional):Failed, Reason:Content-Encoding is not valid");
            else {
                addValidation(ValidationType.HTTPHEADER, "Passed");
                //   Reporter.log("Validation Step-Header validation(Optional):Passed");
            }
        }
        return this;
    }

    @Override
    public ValidatableResponseExtension schema(ValidationCode validationCode) {
        Response response = getResponse();
        String jsonString=response.getBody().asString();
        String schemaString= FileUtility.loadFileAsString(TestUtility.getResponseSchema(testContext.getApiName()));
        try {
            JsonUtility.validateSchema(schemaString,jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    @Override
    public ValidatableResponseExtension assertThat() {
        return this;
    }

    @Override
    public ValidatableResponseExtension statusCode(int expectedHttpCode) {
        Response response = getResponse();
        try {
            Assert.assertEquals(response.getStatusCode(), expectedHttpCode);
            addValidation(ValidationType.HTTPCODE, "Passed");
        } catch (AssertionError e) {
            addValidation(ValidationType.HTTPCODE, "Failed");
            testContext.setReasonOfFailure("Expected status code: in (" + expectedHttpCode + ") but received " + response.getStatusCode());
            Assert.fail("Invalid Http code=" + response.getStatusCode());

        }
        return this;
    }

    private Response getResponse() {
        int size = testContext.getResponseChain().size();
        if (size > 0)
            return testContext.getResponseChain().get(size - 1);
        return null;
    }

    private void addValidation(ValidationType validationType, String Message) {
        Map<ValidationType, String> validation = new HashMap<>();
        validation.put(validationType, Message);
        int size = testContext.getValidationResults().size();
        if (size == 0)
            testContext.getValidationResults().add(new ValidationResult());
        testContext.getValidationResults().get(size - 1).getValidations().add(validation);
    }
}
