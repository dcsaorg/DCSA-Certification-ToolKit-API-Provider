package org.dcsa.api.validator.steps.common;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.dcsa.api.validator.constant.StatusCode;
import org.dcsa.api.validator.constant.ValidationCode;
import org.dcsa.api.validator.hook.TestSetup;
import org.dcsa.api.validator.model.TestCase;
import org.dcsa.api.validator.model.TestContext;
import org.dcsa.api.validator.restassured.extension.Impl.RestAssuredExtensionImpl;
import org.dcsa.api.validator.restassured.extension.RestAssuredExtension;
import org.dcsa.api.validator.util.FileUtility;
import org.dcsa.api.validator.util.JsonUtility;
import org.dcsa.api.validator.util.TestUtility;
import org.dcsa.api.validator.webservice.init.AppProperty;
import org.testng.Assert;

import java.util.*;

public class CommonSteps {

    private RestAssuredExtension restAssuredExtension;
    private Scenario scenario;
    private List<String> headers;

    @Before(order = 1)
    public void testPreparation(Scenario scenario) {
        this.scenario = scenario;
        if (TestSetup.TestContexts.get(scenario.getId()) == null)
            TestSetup.TestContexts.put(scenario.getId(), new TestContext());
        restAssuredExtension = new RestAssuredExtensionImpl(TestSetup.TestContexts.get(scenario.getId()));
        restAssuredExtension.getTestContext().setScenario(scenario);
        headers = new ArrayList<>();
        headers.add("Content-Type");
    }

    @Given("API End point {string} for {string}")
    public void ApiEndPointForAPi(String endPoint, String apiName) {
        restAssuredExtension
                .given(endPoint, apiName);

    }

    @When("Set request for POST")
    public void setRequestBodyWithTestData() {
        String apiName = restAssuredExtension.getTestContext().getApiName();
        TestCase testCase = restAssuredExtension.getTestContext().getTestCase();
        String body = TestUtility.getTestBody(apiName, "", testCase);
        restAssuredExtension
                .body(body);
    }

    @When("Send a POST http request")
    public void createForGivenTestData() {
        restAssuredExtension
                .post();
        if(AppProperty.EVENT_SUBSCRIPTION_SIMULATION){
            doHeadRequest();
        }
    }

    @When("Send a HEAD http request")
    public void doHeadRequest() {
        restAssuredExtension
                .head();
    }


    @When("Send GET http request")
    public void getAll() {
        restAssuredExtension
                .get();
    }

    @When("Set request for DELETE")
    public void setDataForDelete() {
        Map<String, String> pathVariables = new HashMap<>();
        List<Map<String, String>> pathVariableChain = restAssuredExtension.getTestContext().getPathVariableChain();
        if (pathVariableChain.size() > 0)
            pathVariables.putAll(pathVariableChain.get(pathVariableChain.size() - 1));
        TestCase testcase = restAssuredExtension.getTestContext().getTestCase();
        if (testcase.getRequest().getPathVariables() != null && testcase.getRequest().getPathVariables().size() > 0)
            pathVariables.putAll(testcase.getRequest().getPathVariables());
        restAssuredExtension
                .pathParams(pathVariables);

    }

    @When("Set request for PUT")
    public void setDataForPut() {
        String body = null;
        Map<String, String> pathVariables = new HashMap<>();
        String apiName = restAssuredExtension.getTestContext().getApiName();
        List<Response> responsesChain = restAssuredExtension.getTestContext().getResponseChain();
        if (!responsesChain.isEmpty()) {
            List<Map<String, String>> pathVariableChain = restAssuredExtension.getTestContext().getPathVariableChain();
            if (pathVariableChain.size() > 0)
                pathVariables.putAll(pathVariableChain.get(pathVariableChain.size() - 1));
        }

        TestCase testcase = restAssuredExtension.getTestContext().getTestCase();
        body = TestUtility.getTestBody(apiName, body, testcase);
        if (testcase.getRequest().getPathVariables() != null && testcase.getRequest().getPathVariables().size() > 0)
            pathVariables.putAll(testcase.getRequest().getPathVariables());

        restAssuredExtension
                .pathParams(pathVariables)
                .body(body);
    }

    @When("Set request for PUT with test case {string}")
    public void setDataForPutWithTestData(String testName) {
        String body = null;
        Map<String, String> pathVariables = new HashMap<>();
        String apiName = restAssuredExtension.getTestContext().getApiName();
        List<Response> responsesChain = restAssuredExtension.getTestContext().getResponseChain();
        if (!responsesChain.isEmpty()) {
            Response response = responsesChain.get(responsesChain.size() - 1);
            body = JsonUtility.getStringFormat(response.jsonPath().get());
            List<Map<String, String>> pathVariableChain = restAssuredExtension.getTestContext().getPathVariableChain();
            if (pathVariableChain.size() > 0)
                pathVariables.putAll(pathVariableChain.get(pathVariableChain.size() - 1));
        }
        TestCase testCase = TestUtility.getTestCase(apiName, testName);
        if (testCase.getRequest().getTemplateFile() != null) {
            body = TestUtility.getTestBody(apiName, "", testCase);
        }

        if (testCase.getRequest().getPathVariables() != null)
            if (testCase.getRequest().getPathVariables().size() > 0)
                pathVariables.putAll(testCase.getRequest().getPathVariables());
        if (pathVariables != null && !(pathVariables.isEmpty())) {
        } else {
            List<String> dynamicPathVariables = testCase.getRequest().getDynamicPathVariables();
            if (dynamicPathVariables != null && !(dynamicPathVariables.isEmpty())) {
                Map<String, String> dynamicPathVariablesMap = TestUtility.getAttributeFromGetTestData(dynamicPathVariables, apiName);
                if (dynamicPathVariablesMap != null && dynamicPathVariablesMap.size() > 0)
                    pathVariables.putAll(dynamicPathVariablesMap);
            }
        }
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
        Map<String, String> queryParametersMap = TestUtility.getAttributeFromGetTestData(queryParametersList, restAssuredExtension.getTestContext().getApiName());
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
        Map<String, String> queryParametersMap = TestUtility.getAttributeFromGetTestData(queryParametersList, restAssuredExtension.getTestContext().getApiName());
        if (queryParametersMap == null || (queryParametersMap.size() != queryParametersList.size())) {
            TestSetup.TestContexts.get(scenario.getId()).setReasonOfFailure("Query Parameter setup missing for " + queryParameters);
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
        Map<String, String> queryParametersMap = TestUtility.getAttributeFromGetTestData(pathParametersList, restAssuredExtension.getTestContext().getApiName());
        restAssuredExtension
                .pathParams(queryParametersMap);
    }

    @And("path parameters {string}")
    public void belowPathParametersFromTestData(String pathParameters) {
        List<String> pathParametersList = Arrays.asList(pathParameters.split(","));
        Map<String, String> pathParametersMap = TestUtility.getAttributeFromGetTestData(pathParametersList, restAssuredExtension.getTestContext().getApiName());
        if (pathParametersMap.size() != pathParametersList.size()) {
            TestSetup.TestContexts.get(scenario.getId()).setReasonOfFailure("Path parameter setup missing for " + pathParameters);
            Assert.fail("Path parameter setup missing for " + pathParameters);
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
                .header(headers);
    }


    @Then("Receive valid response for POST")
    public void shouldBeCreatedSuccessfully() {
        restAssuredExtension
                .then()
                .assertThat()
                .created(StatusCode.OK)
                .header(headers);
    }

    @Then("Receive invalid response for POST")
    public void invalidHttpResponseForPost() {
        restAssuredExtension
                .then()
                .assertThat()
                .created(StatusCode.NOK);
    }


    @Then("Validated against schema")
    public void validateResponseAgainstSchema() {
        restAssuredExtension
                .then()
                .assertThat()
                .schema(ValidationCode.VALID);
    }


    @Then("Receive valid response for DELETE")
    public void shouldBeDeletedSuccessfully() {
        restAssuredExtension
                .then()
                .assertThat()
                .deleted(StatusCode.OK)
                .header(headers);
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
                .header(headers);
    }

    @Then("Receive valid response for GET all")
    public void receiveAllRecordsSuccessfully() {
        restAssuredExtension
                .then()
                .assertThat()
                .foundAll(StatusCode.OK)
                .header(headers)
        ;
    }

/*    @Then("Receive paging attribute in header")
    public void receivePagingAttributeInHeader() {
        headers.add("Current-Page");
        restAssuredExtension
                .then()
                .assertThat()
                .foundAll(StatusCode.OK)
                .header(headers)
        ;
    }*/

    @Then("Receive attribute in header")


    @Then("Receive invalid response for PUT")
    public void modificationShouldGetFailed() {
        restAssuredExtension
                .then()
                .assertThat()
                .modified(StatusCode.NOK);
    }

    @When("Set request for POST with test case {string}")
    public void setRequestBodyWithTestData(String testName) {
        String body;
        String apiName = restAssuredExtension.getTestContext().getApiName();
        TestCase testCase = TestUtility.getTestCase(apiName, testName);
        if (restAssuredExtension.getTestContext().getCallbackURL() != null) {
            Map<String, Object> placeholders = new HashMap<>();
            placeholders.put("callbackUrl", restAssuredExtension.getTestContext().getCallbackURL());
            testCase.getRequest().getPlaceHolders().putAll(placeholders);
        }
        if(AppProperty.EVENT_SUBSCRIPTION_SIMULATION){
            restAssuredExtension.setCallbackUri(restAssuredExtension.getTestContext().getCallbackURL());
        }
        body = TestUtility.getTestBody(apiName, "", testCase);
        restAssuredExtension
                .body(body);
    }

    @And("Attributes to be removed {string}")
    public void belowPlaceholdersToBeRemoved(String attributes) {
        List<String> attributesList = Arrays.asList(attributes.split(","));
        restAssuredExtension
                .removeAttribute(attributesList);
    }


    @When("Set request for GET")
    public void setRequestForGET() {
        Map<String, String> pathVariables = restAssuredExtension.getTestContext().getTestCase().getRequest().getPathVariables();
        Map<String, String> queryParameters = restAssuredExtension.getTestContext().getTestCase().getRequest().getQueryParameters();
        restAssuredExtension
                .pathParams(pathVariables)
                .queryParams(queryParameters);
    }

    @When("Set request empty body")
    public void setRequestEmptyBody() {
        restAssuredExtension
                .body("");
    }

    @When("Set request for GET with test case {string}")
    public void setRequestForGetWithTestData(String testName) {


    }

    @When("Set request for DELETE with test case {string}")
    public void setRequestForDeleteWithTestData(String testName) {


    }

    @And("Path parameters {string}")
    public void pathParameters(String parameters) {
        List<String> parametersList = Arrays.asList(parameters.split(","));
        Map<String, String> parametersMap = TestUtility.getAttributeFromGetTestData(parametersList, restAssuredExtension.getTestContext().getApiName());
        if (parametersMap == null || (parametersMap.size() != parametersList.size())) {
            TestSetup.TestContexts.get(scenario.getId()).setReasonOfFailure("Path parameter setup missing for " + parameters);
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
        String apiName = restAssuredExtension.getTestContext().getApiName();
        TestCase testCase = TestUtility.getTestCase(apiName, testName);
        if (testCase.getRequest().getTemplateFile() != null) {
            body = FileUtility.loadFileAsString(testCase.getRequest().getTemplateFile());
        }
        if (body != null)
            body = TestUtility.getTestBody(apiName, body, testCase);
        Map<String, String> pathVariables = testCase.getRequest().getPathVariables();
        if (pathVariables != null && !(pathVariables.isEmpty())) {
        } else if (testCase.getRequest().getDynamicPathVariables() != null && !(testCase.getRequest().getDynamicPathVariables().isEmpty())) {
            pathVariables = TestUtility.getAttributeFromGetTestData(testCase.getRequest().getDynamicPathVariables(), apiName);
        }
        Map<String, String> queryParameters = testCase.getRequest().getQueryParameters();
        if (queryParameters != null && !(queryParameters.isEmpty())) {
        } else if (testCase.getRequest().getDynamicQueryParameters() != null && !(testCase.getRequest().getDynamicQueryParameters().isEmpty())) {
            pathVariables = TestUtility.getAttributeFromGetTestData(testCase.getRequest().getDynamicQueryParameters(), apiName);
        }
        if (body != null && !body.isEmpty())
            restAssuredExtension
                    .body(body);
        if (pathVariables != null && !pathVariables.isEmpty())
            restAssuredExtension
                    .pathParams(pathVariables);
        if (queryParameters != null && !queryParameters.isEmpty())
            restAssuredExtension
                    .queryParams(pathVariables);
    }


    @After(order = 99)
    public void cleanUp(Scenario s) {
        TestSetup.TestContexts.get(s.getId()).setTestCaseName(s.getName());
        TestSetup.TestContexts.get(s.getId()).setStatus(s.getStatus().toString());
    }

    @Then("Receive response code {string}")
    public void receiveResponseCode(String expectedHttpCode) {
        restAssuredExtension
                .then()
                .assertThat()
                .statusCode(Integer.parseInt(expectedHttpCode));

    }


    @Then("Receive headers in response")
    public void receiveBelowHeadersInResponse(DataTable headerList) {
        List<String> newHeaders = headerList.asList();
        if (newHeaders != null)
            headers.addAll(newHeaders);
        restAssuredExtension
                .then()
                .assertThat()
                .header(headers)
        ;
    }
}
