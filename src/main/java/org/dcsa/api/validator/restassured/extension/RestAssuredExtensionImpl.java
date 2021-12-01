package org.dcsa.api.validator.restassured.extension;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import org.dcsa.api.validator.config.Configuration;
import org.dcsa.api.validator.model.TestContext;
import org.dcsa.api.validator.model.ValidationResult;
import org.dcsa.api.validator.util.TestUtility;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class RestAssuredExtensionImpl implements RestAssuredExtension {
    private RequestSpecBuilder builder;
    private Response response;
    private ValidatableResponseExtensionImpl validatableResponseExtensionImpl;
    private TestContext testContext;

    public RestAssuredExtensionImpl() {
        testContext = new TestContext();
    }

    public RestAssuredExtensionImpl(TestContext testContext ) {
        this.testContext = testContext;
    }
    @Override
    public void given(String endPoint, String apiName) {
        testContext.setApiName(apiName);
        this.builder = new RequestSpecBuilder();
        this.builder.setBasePath(endPoint);
        this.builder.addHeader("API-Version", Configuration.API_VERSION);
        this.builder.setBaseUri(Configuration.ROOT_URI + "/v" + Configuration.API_VERSION.split("\\.")[0]);
        this.builder.setContentType(ContentType.JSON);
    }

    private RequestSpecification buildRequest() {
        RequestSpecification request= RestAssured
                .given()
                .config(RestAssured.config().sslConfig(
                        new SSLConfig().allowAllHostnames()))
                .auth()
                .oauth2(Configuration.accessToken)
                .filter(new RestAssuredRequestFilter(testContext.getScenario())).spec(builder.build());
        testContext.getRequestChain().add((FilterableRequestSpecification) request);
        return request;
    }

    @Override
    public void post() {
        response = buildRequest().post();
        if (response.getStatusCode() == 201) {
            String value = response.jsonPath().get(TestUtility.getIdentifierAttribute(testContext.getApiName()));
            Map<String, String> pathVariables = new HashMap<>();
            pathVariables.put(TestUtility.getIdentifierAttribute(testContext.getApiName()), value);
            testContext.getPathVariableChain().add(pathVariables);
        }
        testContext.getResponseChain().add(response);
    }

    @Override
    public void get() {
        response = buildRequest().get();
        testContext.getResponseChain().add(response);
    }

    @Override
    public void delete() {
        response = buildRequest().delete();
        testContext.getResponseChain().add(response);
    }

    @Override
    public void put() {
        response = buildRequest().put();
        testContext.getResponseChain().add(response);

    }

    @Override
    public RestAssuredExtensionImpl body(String body) {
        if (body != null) {
            builder.setBody(body);
        }
        return this;
    }

    @Override
    public RestAssuredExtensionImpl queryParams(Map<String, String> queryParameters) {
        if (queryParameters != null && queryParameters.size() > 0) {
            if (testContext.getTestCase().getRequest().getQueryParameters() != null)
                testContext.getTestCase().getRequest().getQueryParameters().putAll(queryParameters);
            else
                testContext.getTestCase().getRequest().setQueryParameters(queryParameters);
            Map<String, String> tQueryParameters = testContext.getTestCase().getRequest().getQueryParameters();
            for (String queryParam : tQueryParameters.keySet())
                builder.removeQueryParam(queryParam);
            builder.addQueryParams(tQueryParameters);
        }
        return this;
    }

    @Override
    public RestAssuredExtension removeAttribute(List<String> attributes) {
        testContext.getTestCase().getRequest().setRemovalAttributes(attributes);
        return this;
    }

    @Override
    public RestAssuredExtension placeHolders(Map<String, Object> placeHolders) {
        if (placeHolders != null && placeHolders.size() > 0) {
            if (testContext.getTestCase().getRequest().getPlaceHolders() != null)
                testContext.getTestCase().getRequest().getPlaceHolders().putAll(placeHolders);
            else
                testContext.getTestCase().getRequest().setPlaceHolders(placeHolders);
        }
        return this;
    }

  /*  @Override
    public RestAssuredExtension create() {
        return create(testContext.getApiName());
    }
*/
/*    @Override
    public RestAssuredExtensionImpl create(String resource) {
        Response response = null;
        try {
            response = RestAssured.given()
                    .contentType("application/json")
                    .body(TestUtility.getBodyForCreate(resource))
                    .baseUri(Configuration.ROOT_URI + "v" + Configuration.API_VERSION + "")
                    .basePath(TestUtility.getEndPoint(resource))
                    .post();
            response.then().assertThat()
                    .statusCode(201);
            testContext.getResponseChain().add(response);
            String value = response.jsonPath().get(TestUtility.getIdentifierAttribute(resource));
            Map<String, String> pathVariables = new HashMap<>();
            pathVariables.put(TestUtility.getIdentifierAttribute(resource), value);
            if (testContext.getTestCase().getRequest().getPathVariables() != null)
                testContext.getTestCase().getRequest().getPathVariables().putIfAbsent(TestUtility.getIdentifierAttribute(resource), value);
            else
                testContext.getTestCase().getRequest().setPathVariables(pathVariables);

        } catch (AssertionError e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return this;
    }*/

    @Override
    public RestAssuredExtensionImpl pathParams(Map<String, String> pathVariables) {
        if (pathVariables != null && pathVariables.size() > 0) {
            if (TestUtility.getTestRequest(testContext.getTestCase()).getPathVariables() != null) {
                testContext.getTestCase().getRequest().getPathVariables().putAll(pathVariables);
            } else
                testContext.getTestCase().getRequest().setPathVariables(pathVariables);
            Map<String, String> tPathVariables = testContext.getTestCase().getRequest().getPathVariables();
            for (String pathParam : tPathVariables.keySet())
                builder.removePathParam(pathParam);
            builder.addPathParams(tPathVariables);
        }
        return this;
    }

    @Override
    public ValidatableResponseExtension then() {
       // validatableResponseExtensionImpl.setResponse(response);
      //  validatableResponseExtensionImpl.setApiName(testContext.getApiName());
        //validatableResponseExtensionImpl.setTestContext(testContext.getTestCase());
        testContext.getValidationResults().add(new ValidationResult());
        validatableResponseExtensionImpl=new ValidatableResponseExtensionImpl(testContext);
        return validatableResponseExtensionImpl;
    }


}
