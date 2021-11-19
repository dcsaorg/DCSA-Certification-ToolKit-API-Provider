package org.dcsa.api.validator.restassured.extension;

import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import org.dcsa.api.validator.config.Configuration;
import org.dcsa.api.validator.model.TestCase;
import org.dcsa.api.validator.model.TestRequest;
import org.dcsa.api.validator.util.TestUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class RestAssuredExtensionImpl implements RestAssuredExtension {
    private RequestSpecBuilder builder;
    private Response response;
    private ValidatableResponseExtensionImpl validatableResponseExtensionImpl;
    private TestCase testContext;
    private String apiName;
    private List<Response> helperResponse;
    private Scenario scenario;

    public RestAssuredExtensionImpl() {
        initialize();
    }

    public void initialize() {
        this.builder = new RequestSpecBuilder();
        this.helperResponse = new ArrayList<>();
        this.validatableResponseExtensionImpl = new ValidatableResponseExtensionImpl();
        testContext = new TestCase();
        testContext.setRequest(new TestRequest());
    }

    @Override
    public void given(String endPoint, String apiName) {
        this.apiName = apiName;
        this.builder.setBasePath(endPoint);
        this.builder.addHeader("API-Version", Configuration.API_VERSION);
        this.builder.setBaseUri(Configuration.ROOT_URI + "v" + Configuration.API_VERSION);
        this.builder.setContentType(ContentType.JSON);
    }

    private RequestSpecification buildRequest() {
        return RestAssured
                .given()
                .auth()
                .oauth2(Configuration.accessToken)
                .filter(new RestAssuredRequestFilter(scenario)).spec(builder.build());
    }

    @Override
    public void post() {
        response = buildRequest().post();
    }

    @Override
    public void get() {
       /* if (TestUtility.getTestRequest(testContext) != null) {
            if (TestUtility.getTestRequest(testContext).getPathVariables() != null)
                if (!testContext.getRequest().getPathVariables().isEmpty())
                    builder.addPathParams(testContext.getRequest().getPathVariables());
        }
        if (testContext.getRequest().getQueryParameters() != null)
            if (testContext.getRequest().getQueryParameters().getQueryParameters() != null)
                builder.addQueryParams(testContext.getRequest().getQueryParameters().getQueryParameters());*/
        response = buildRequest().get();
    }

    @Override
    public void delete() {
/*        if (TestUtility.getTestRequest(testContext) != null) {
            if (TestUtility.getTestRequest(testContext).getPathVariables() != null)
                if (!testContext.getRequest().getPathVariables().isEmpty())
                    builder.addPathParams(testContext.getRequest().getPathVariables());
        }*/
        response = buildRequest().delete();
    }

    @Override
    public void put() {
      /*  if (TestUtility.getTestRequest(testContext) != null) {
            if (TestUtility.getTestRequest(testContext).getPathVariables() != null)
                if (!testContext.getRequest().getPathVariables().isEmpty())
                    builder.addPathParams(testContext.getRequest().getPathVariables());
        }*/
        response = buildRequest().put();
    }

    @Override
    public RestAssuredExtensionImpl body(String body) {
        if (body != null) {
           /* if (testContext.getRequest().getPlaceHolders() != null)
                body = JsonUtility.replacePlaceHolders(body, testContext.getRequest().getPlaceHolders());
            if (testContext.getRequest().getRemovalAttributes() != null)
                body = JsonUtility.removeAttributes(body, testContext.getRequest().getRemovalAttributes());*/
            builder.setBody(body);
        }
        return this;
    }

    @Override
    public RestAssuredExtensionImpl queryParams(Map<String, String> queryParameters) {
        if (queryParameters != null && queryParameters.size() > 0) {
            if (testContext.getRequest().getQueryParameters()!= null)
                testContext.getRequest().getQueryParameters().putAll(queryParameters);
            else
                testContext.getRequest().setQueryParameters(queryParameters);
            Map<String, String> tQueryParameters = testContext.getRequest().getQueryParameters();
            for (String queryParam : tQueryParameters.keySet())
                builder.removeQueryParam(queryParam);
            builder.addQueryParams(tQueryParameters);
        }
        return this;
    }

    @Override
    public RestAssuredExtension removeAttribute(List<String> attributes) {
        testContext.getRequest().setRemovalAttributes(attributes);
        return this;
    }

    @Override
    public RestAssuredExtension placeHolders(Map<String, Object> placeHolders) {
/*        Map<String, Object> placeHolders = new HashMap<>();
        for (Map<String, Object> placeholder : placeholderMap) {
            placeHolders.put((String) placeholder.get("Attribute"), placeholder.get("Value"));
        }*/
        if(placeHolders!=null && placeHolders.size()>0) {
            if (testContext.getRequest().getPlaceHolders() != null)
                testContext.getRequest().getPlaceHolders().putAll(placeHolders);
            else
                testContext.getRequest().setPlaceHolders(placeHolders);
        }
        return this;
    }

    @Override
    public RestAssuredExtension create() {
        return create(apiName);
    }

    @Override
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
            helperResponse.add(response);
            String value = response.jsonPath().get(TestUtility.getIdentifierAttribute(resource));
            Map<String, String> pathVariables = new HashMap<>();
            pathVariables.put(TestUtility.getIdentifierAttribute(resource), value);
            if (testContext.getRequest().getPathVariables() != null)
                testContext.getRequest().getPathVariables().putIfAbsent(TestUtility.getIdentifierAttribute(resource), value);
            else
                testContext.getRequest().setPathVariables(pathVariables);

        } catch (AssertionError e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return this;
    }

    @Override
    public RestAssuredExtensionImpl pathParams(Map<String, String> pathVariables) {
        if (pathVariables != null && pathVariables.size() > 0) {
            if (TestUtility.getTestRequest(testContext).getPathVariables() != null) {
               // for (String key : pathVariables.keySet())
                  //  testContext.getRequest().getPathVariables().putIfAbsent(key, pathVariables.get(key));
                testContext.getRequest().getPathVariables().putAll(pathVariables);
            } else
                testContext.getRequest().setPathVariables(pathVariables);
            Map<String, String> tPathVariables = testContext.getRequest().getPathVariables();
            for (String pathParam : tPathVariables.keySet())
                builder.removePathParam(pathParam);
            builder.addPathParams(tPathVariables);
        }
        return this;
    }

    @Override
    public ValidatableResponseExtension then() {
        validatableResponseExtensionImpl.setResponse(response);
        validatableResponseExtensionImpl.setApiName(apiName);
        validatableResponseExtensionImpl.setTestContext(testContext);
        return validatableResponseExtensionImpl;
    }


}
