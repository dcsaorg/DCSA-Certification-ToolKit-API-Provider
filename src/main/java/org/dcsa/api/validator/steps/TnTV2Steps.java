package org.dcsa.api.validator.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.dcsa.api.validator.constants.StatusCode;
import org.dcsa.api.validator.model.TestCase;
import org.dcsa.api.validator.restassured.extension.RestAssuredExtension;
import org.dcsa.api.validator.restassured.extension.RestAssuredExtensionImpl;
import org.dcsa.api.validator.util.FileUtility;
import org.dcsa.api.validator.util.JsonUtility;
import org.dcsa.api.validator.util.TestUtility;
import org.testng.Assert;
import org.testng.Reporter;

import java.util.*;

public class TnTV2Steps {

    private RestAssuredExtension restAssuredExtension;
    private Scenario scenario;

    public TnTV2Steps() {
        restAssuredExtension = new RestAssuredExtensionImpl();
    }

    @Before
    public void testPreparation(Scenario scenario) {
        this.scenario = scenario;
        restAssuredExtension.setScenario(scenario);
    }

    @Given("API End point {string} for {string}")
    public void ApiEndPointForAPi(String endPoint, String apiName) {
        restAssuredExtension
                .given(endPoint, apiName);

    }

    @When("Set request for POST")
    public void setRequestBodyWithTestData() {
        String body = TestUtility.getTestBody(restAssuredExtension.getApiName(), restAssuredExtension.getTestContext());
        restAssuredExtension
                .body(body);
    }

    @When("Send a POST http request")
    public void createForGivenTestData() {
        restAssuredExtension
                .post();
    }

    @When("Send GET http request")
    public void getAll() {
        restAssuredExtension
                .get();
    }

    @When("Set request for DELETE")
    public void setDataForDelete() {
        restAssuredExtension
                .create();
        Map<String, String> pathVariables = restAssuredExtension.getTestContext().getRequest().getPathVariables();
        restAssuredExtension
                .pathParams(pathVariables);

    }

    @When("Set request for PUT")
    public void setDataForPut() {
        restAssuredExtension.create();
        String body = null;
        if (!restAssuredExtension.getHelperResponse().isEmpty()) {
            Response response = restAssuredExtension.getHelperResponse().get(0);
            body = JsonUtility.getStringFormat(response.jsonPath().get());
        }
        body = TestUtility.getTestBody(restAssuredExtension.getApiName(), body, restAssuredExtension.getTestContext());
        Map<String, String> pathVariables = restAssuredExtension.getTestContext().getRequest().getPathVariables();
        restAssuredExtension
                .pathParams(pathVariables)
                .body(body);
    }

    @When("Set request for PUT with test case {string}")
    public void setDataForPutWithTestData(String testName) {
        String body = null;
        restAssuredExtension.create();
        if (!restAssuredExtension.getHelperResponse().isEmpty()) {
            Response response = restAssuredExtension.getHelperResponse().get(0);
            body = JsonUtility.getStringFormat(response.jsonPath().get());
        }
        TestCase testContext = TestUtility.getTestCase(restAssuredExtension.getApiName(), testName);
        if (testContext.getRequest().getTemplateFile() != null) {
            body = FileUtility.loadFileAsString(testContext.getRequest().getTemplateFile());
        }
        body = TestUtility.getTestBody(restAssuredExtension.getApiName(), body, testName);
        Map<String, String> pathVariables = testContext.getRequest().getPathVariables();
        if (pathVariables != null && !(pathVariables.isEmpty())) {
        } else if (testContext.getRequest().getDynamicPathVariables() != null && !(testContext.getRequest().getDynamicPathVariables().isEmpty())) {
            pathVariables = TestUtility.getAttributeFromGetTestData(testContext.getRequest().getDynamicPathVariables(), restAssuredExtension.getApiName());
        } else
            pathVariables = restAssuredExtension.getTestContext().getRequest().getPathVariables();
        restAssuredExtension
                .pathParams(pathVariables)
                .body(body);
    }

    @When("Send a DELETE http request")
    public void createAndDelete() {
        restAssuredExtension
                .delete();
    }

    @When("Send a PUT http request")
    public void sendPutHttpRequest() {
        restAssuredExtension
                .put();
    }

    @And("Query parameters with values")
    public void belowQueryParameters(DataTable queryParameterTable) {
        List<Map<String, String>> queryParameterList = queryParameterTable.asMaps();
        Map<String, String> queryParameters = new HashMap<>();
        for (Map<String, String> queryParameter : queryParameterList) {
            queryParameters.put(queryParameter.get("parameter"), queryParameter.get("value"));
        }
        restAssuredExtension
                .queryParams(queryParameters);
    }

    @And("Query parameters")
    public void queryParameters(DataTable queryParameterTable) {
        List<String> queryParametersList = queryParameterTable.asList();
        Map<String, String> queryParametersMap = TestUtility.getAttributeFromGetTestData(queryParametersList, restAssuredExtension.getApiName());
        restAssuredExtension
                .queryParams(queryParametersMap);
    }


    @And("Query parameter {string} with value {string}")
    public void queryParametersWithValues(String queryParameter, String value) {
        Map<String, String> queryParametersMap = new HashMap<>();
        queryParametersMap.put(queryParameter, value);
        restAssuredExtension
                .queryParams(queryParametersMap);
    }

    @And("Query parameters {string}")
    public void belowQueryParametersFromTestData(String queryParameters) {
        List<String> queryParametersList = Arrays.asList(queryParameters.split(","));
        Map<String, String> queryParametersMap = TestUtility.getAttributeFromGetTestData(queryParametersList, restAssuredExtension.getApiName());
        if (queryParametersMap == null || (queryParametersMap.size() != queryParametersList.size())) {
            Reporter.log("Validation Step-Query Parameter setup missing for " + queryParameters );
            Assert.fail("Query Parameter setup missing for " + queryParameters);
        } else {
            restAssuredExtension
                    .queryParams(queryParametersMap);
        }
    }

    @When("Path parameters with values")
    public void belowPathParameters(DataTable pathParametersTable) {
        List<Map<String, String>> pathParametersList = pathParametersTable.asMaps();
        Map<String, String> pathParameters = new HashMap<>();
        for (Map<String, String> queryParameter : pathParametersList) {
            pathParameters.put(queryParameter.get("pathVariable"), queryParameter.get("value"));
        }
        restAssuredExtension
                .pathParams(pathParameters);
    }

    @And("Path parameters")
    public void belowPathParametersFromTestData(DataTable pathVariableTable) {
        List<String> pathParametersList = pathVariableTable.asList();
        Map<String, String> queryParametersMap = TestUtility.getAttributeFromGetTestData(pathParametersList, restAssuredExtension.getApiName());
        restAssuredExtension
                .pathParams(queryParametersMap);
    }

    @And("path parameters {string}")
    public void belowPathParametersFromTestData(String pathParameters) {
        List<String> pathParametersList = Arrays.asList(pathParameters.split(","));
        Map<String, String> pathParametersMap = TestUtility.getAttributeFromGetTestData(pathParametersList, restAssuredExtension.getApiName());
        if (pathParametersMap.size() != pathParametersList.size()) {
            Reporter.log("Validation Step-Path parameter setup missing for " + pathParameters );
             Assert.fail("Path parameter setup missing for "+pathParameters);
        } else {
            restAssuredExtension
                    .pathParams(pathParametersMap);
        }
    }

    @And("path parameters {string} with values {string}")
    public void pathParametersWithValues(String pathParameter, String value) {
        Map<String, String> queryParametersMap = new HashMap<>();
        queryParametersMap.put(pathParameter, value);
        restAssuredExtension
                .pathParams(queryParametersMap);
    }

    @When("Placeholders with values")
    public void belowPlaceholders(DataTable placeholdersTable) {
        List<Map<String, Object>> placeholderMap = placeholdersTable.asMaps(String.class, Object.class);
        Map<String, Object> placeHolders = new HashMap<>();
        for (Map<String, Object> placeholder : placeholderMap) {
            placeHolders.put((String) placeholder.get("placeholder"), placeholder.get("value"));
        }
        restAssuredExtension
                .placeHolders(placeHolders);
    }


    @Then("Receive invalid response for GET")
    public void receiveInvalidHttpResponseForGet() {
        restAssuredExtension
                .then()
                .assertThat()
                .found(StatusCode.NOK);
    }

    @Then("Receive valid response for GET")
    public void receiveValidHttpResponseForGet() {
        restAssuredExtension
                .then()
                .assertThat()
                .found(StatusCode.OK)
                .header(StatusCode.OK);
    }


    @Then("Receive valid response for POST")
    public void shouldBeCreatedSuccessfully() {
        restAssuredExtension
                .then()
                .assertThat()
                .created(StatusCode.OK)
                .header(StatusCode.OK);
    }

    @Then("Receive invalid response for POST")
    public void invalidHttpResponseForPost() {
        restAssuredExtension
                .then()
                .assertThat()
                .created(StatusCode.NOK);
    }

/*
    @Then("validate response against schema")
    public void validateResponseAgainstSchema() {
        restAssuredExtension
                .then()
                .assertThat()
                .schemaValidated();
    }
*/

    @Then("Receive valid response for DELETE")
    public void shouldBeDeletedSuccessfully() {
        restAssuredExtension
                .then()
                .assertThat()
                .deleted(StatusCode.OK)
                .header(StatusCode.OK);
    }


    @Then("Receive invalid response for DELETE")
    public void deleteShouldGetFailed() {
        restAssuredExtension
                .then()
                .assertThat()
                .deleted(StatusCode.NOK);
    }

    @Then("Receive valid response for PUT")
    public void shouldBeModifiedSuccessfully() {
        restAssuredExtension
                .then()
                .assertThat()
                .modified(StatusCode.OK)
                .header(StatusCode.OK);
    }

    @Then("Receive valid response for GET all")
    public void receiveAllRecordsSuccessfully() {
        restAssuredExtension
                .then()
                .assertThat()
                .foundAll(StatusCode.OK)
                .header(StatusCode.OK)
        ;
    }

    @Then("Receive invalid response for PUT")
    public void modificationShouldGetFailed() {
        restAssuredExtension
                .then()
                .assertThat()
                .modified(StatusCode.NOK);
    }


    @When("Set request for POST with test case {string}")
    public void setRequestBodyWithTestData(String testName) {
        String body = TestUtility.getTestBody(restAssuredExtension.getApiName(), testName);
        restAssuredExtension
                .body(body);
    }

    @And("Attributes to be removed {string}")
    public void belowPlaceholdersToBeRemoved(String attributes) {
        List<String> attributesList = Arrays.asList(attributes.split(","));
        restAssuredExtension
                .removeAttribute(attributesList);
    }


    @After
    public void cleanUp() {
        //s.log(restAssuredExtension.getMessage().getMessageText());
        // Reporter.log(restAssuredExtension.getMessage().getMessageText());
    }

    @When("Set request for GET")
    public void setRequestForGET() {
        Map<String, String> pathVariables = restAssuredExtension.getTestContext().getRequest().getPathVariables();
        Map<String, String> queryParameters = restAssuredExtension.getTestContext().getRequest().getQueryParameters();
        restAssuredExtension
                .pathParams(pathVariables)
                .queryParams(queryParameters);
    }

    @When("Set request for GET with test case {string}")
    public void setRequestForGetWithTestData(String testName) {


    }

    @When("Set request for DELETE with test case {string}")
    public void setRequestForDeleteWithTestData(String testName) {


    }

    @Then("Receive response code “{int}”")
    public void receiveResponseCode(int expectedHttpCode) {
        restAssuredExtension
                .then()
                .assertThat()
                .statusCode(expectedHttpCode);
    }

    @And("Path parameters {string}")
    public void pathParameters(String parameters) {
        List<String> parametersList = Arrays.asList(parameters.split(","));
        Map<String, String> parametersMap = TestUtility.getAttributeFromGetTestData(parametersList, restAssuredExtension.getApiName());
        if (parametersMap == null || (parametersMap.size() != parametersList.size())) {
            Reporter.log("Validation Step-Path parameter setup missing for " + parameters );
            Assert.fail("Path parameter setup missing for " + parameters);
        } else {
            restAssuredExtension
                    .pathParams(parametersMap);
        }
    }

    @And("Path parameter {string} with value {string}")
    public void pathParameterWithValue(String parameter, String value) {
        Map<String, String> queryParametersMap = new HashMap<>();
        queryParametersMap.put(parameter, value);
        restAssuredExtension
                .pathParams(queryParametersMap);
    }

    @And("Attributes to be removed")
    public void attributesToBeRemoved(DataTable attributes) {
        List<String> attributesList = attributes.asList();
        restAssuredExtension
                .removeAttribute(attributesList);
    }

    @And("Placeholder {string} with value {string}")
    public void placeholderWithValue(String placeholder, String value) {
        Map<String, Object> placeholderMap = new HashMap<>();
        placeholderMap.put(placeholder, value);
        restAssuredExtension
                .placeHolders(placeholderMap);
    }

    @When("Set request with test case {string}")
    public void setRequestWithTestCase(String testName) {
        String body = null;
        TestCase testContext = TestUtility.getTestCase(restAssuredExtension.getApiName(), testName);
        if (testContext.getRequest().getTemplateFile() != null) {
            body = FileUtility.loadFileAsString(testContext.getRequest().getTemplateFile());
        }
        if (body != null)
            body = TestUtility.getTestBody(restAssuredExtension.getApiName(), body, testName);
        Map<String, String> pathVariables = testContext.getRequest().getPathVariables();
        if (pathVariables != null && !(pathVariables.isEmpty())) {
        } else if (testContext.getRequest().getDynamicPathVariables() != null && !(testContext.getRequest().getDynamicPathVariables().isEmpty())) {
            pathVariables = TestUtility.getAttributeFromGetTestData(testContext.getRequest().getDynamicPathVariables(), restAssuredExtension.getApiName());
        }
        Map<String, String> queryParameters = testContext.getRequest().getQueryParameters();
        if (queryParameters != null && !(queryParameters.isEmpty())) {
        } else if (testContext.getRequest().getDynamicQueryParameters() != null && !(testContext.getRequest().getDynamicQueryParameters().isEmpty())) {
            pathVariables = TestUtility.getAttributeFromGetTestData(testContext.getRequest().getDynamicQueryParameters(), restAssuredExtension.getApiName());
        }
        if(body!=null && !body.isEmpty())
        restAssuredExtension
                .body(body);
        if(pathVariables!=null && !pathVariables.isEmpty())
            restAssuredExtension
                    .pathParams(pathVariables);
        if(queryParameters!=null && !queryParameters.isEmpty())
            restAssuredExtension
                    .queryParams(pathVariables);
    }
}
