package org.dcsa.api.validator.restassured.extension.Impl;

import io.restassured.response.Response;
import lombok.Data;
import org.dcsa.api.validator.constant.StatusCode;
import org.dcsa.api.validator.constant.ValidationCode;
import org.dcsa.api.validator.constant.ValidationType;
import org.dcsa.api.validator.model.TestContext;
import org.dcsa.api.validator.model.ValidationResult;
import org.dcsa.api.validator.restassured.extension.ValidatableResponseExtension;
import org.dcsa.api.validator.util.FileUtility;
import org.dcsa.api.validator.util.JsonUtility;
import org.dcsa.api.validator.util.TestUtility;
import org.testng.Assert;

import java.util.*;

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

        if (statusCode == StatusCode.OK) {
            try {
                response.then()
                        .assertThat().statusCode(201);
                addValidation(ValidationType.HTTPCODE, "Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: in (201) but received " + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
            try {
                response.then()
                        .assertThat().body("size()", greaterThanOrEqualTo(1));
                addValidation(ValidationType.RESPONSEBODY, "Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.RESPONSEBODY, "Failed, Reason:No Response body");
                testContext.setReasonOfFailure("Empty Response body");
                Assert.fail("Empty Response Body");
            }

        } else {
            try {
                response.then()
                        .assertThat().statusCode(not(anyOf(is(201), is(202), is(204))));
                addValidation(ValidationType.HTTPCODE, "Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
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
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: in (200) but received " + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
            try {
                response.then()
                        .assertThat().body("size()", greaterThanOrEqualTo(1));
                addValidation(ValidationType.HTTPCODE, "Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Empty Response body");
                Assert.fail("Empty Response Body");
            }
        } else {
            try {
                response.then()
                        .assertThat().statusCode(not(anyOf(is(200), is(202), is(204))));
                addValidation(ValidationType.HTTPCODE, "Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: not in (200,202,204) but received " + response.getStatusCode());
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
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: in (200,204) but received " + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
        } else {
            try {
                response.then()
                        .assertThat().statusCode(not(anyOf(is(200), is(202), is(204))));
                addValidation(ValidationType.HTTPCODE, "Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: not in (200,202,204) but received " + response.getStatusCode());
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
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: in (200) but received " + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
            try {
                response.then()
                        .assertThat().body("size()", greaterThanOrEqualTo(1));
                addValidation(ValidationType.RESPONSEBODY, "Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.RESPONSEBODY, "Failed, Reason:No Response body");
                testContext.setReasonOfFailure("Empty Response Body");
                Assert.fail("Empty Response body");
            }
        } else {
            try {
                response.then()
                        .assertThat()
                        .statusCode(not(anyOf(is(200), is(201), is(202), is(204))));
                addValidation(ValidationType.HTTPCODE, "Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: not in (200,201,202,204) but received " + response.getStatusCode());
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
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: in (200) but received " + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
            try {
                response.then().assertThat().body(TestUtility.getIdentifierAttribute(testContext.getApiName()) + ".size()", greaterThanOrEqualTo(1));
                addValidation(ValidationType.RESPONSEBODY, "Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.RESPONSEBODY, "Failed, Reason:Empty Response Body");
                testContext.setReasonOfFailure("Empty Response Body");
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
                    } else {
                        addValidation(ValidationType.LIMIT, "Failed, Reason:number of records more than limit");
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
                            Assert.fail("Value mismatch for query parameter " + entry.getKey() + " in response");
                            break;
                        }
                    }
                    if (isValid) {
                        addValidation(ValidationType.FILTER, "Passed");
                    }
                }
            }
        } else {
            try {
                response.then()
                        .statusCode(anyOf(is(404)));
                addValidation(ValidationType.HTTPCODE, "Passed");
            } catch (AssertionError e) {
                addValidation(ValidationType.HTTPCODE, "Failed");
                testContext.setReasonOfFailure("Expected status code: in (404) but received " + response.getStatusCode());
                Assert.fail(e.getMessage());
            }
        }
        return this;
    }

    @Override
    public ValidatableResponseExtension header(List<String> headers) {
        Response response = getResponse();
        if (headers.size() > 0) {
            for (String header : headers) {
                String headerValue=response.getHeader(header);
                if(headerValue==null || (header.equals("Content-Type")&&!headerValue.contains("json")))
                {
                    addValidation(ValidationType.HTTPHEADER, "Failed, Reason:"+header+" is missing in header");
                    Assert.fail(header+" is missing in header");
                }
            }
        }
        addValidation(ValidationType.HTTPHEADER, "Passed");
        return this;
    }

    @Override
    public ValidatableResponseExtension schema(ValidationCode validationCode) {
        Response response = getResponse();
        String jsonString = response.getBody().asString();
        String schemaString = FileUtility.loadFileAsString(TestUtility.getResponseSchema(testContext.getApiName()));
        try {
            boolean isValid = JsonUtility.validateSchema(schemaString, jsonString);
            if (!isValid) {
                addValidation(ValidationType.SCHEMA, "Failed");
                Assert.fail("Schema validation failed");
            }
            addValidation(ValidationType.SCHEMA, "Passed");
        } catch (Exception e) {
            addValidation(ValidationType.SCHEMA, "Failed");
            Assert.fail(e.getMessage());
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
