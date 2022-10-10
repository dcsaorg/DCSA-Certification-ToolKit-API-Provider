package org.dcsa.api.validator.restassured.extension.Impl;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import org.dcsa.api.validator.config.Configuration;
import org.dcsa.api.validator.model.TNTEventSubscriptionTO;
import org.dcsa.api.validator.model.TestContext;
import org.dcsa.api.validator.model.ValidationResult;
import org.dcsa.api.validator.restassured.extension.RestAssuredExtension;
import org.dcsa.api.validator.restassured.extension.ValidatableResponseExtension;
import org.dcsa.api.validator.restassured.extension.filter.RestAssuredRequestFilter;
import org.dcsa.api.validator.util.TestUtility;
import org.dcsa.api.validator.webservice.init.AppProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class RestAssuredExtensionImpl implements RestAssuredExtension {
    private RequestSpecBuilder builder;
    private Response response;
    private ValidatableResponseExtensionImpl validatableResponseExtensionImpl;
    private TestContext testContext;
    private String callbackUri;

    public RestAssuredExtensionImpl(TestContext testContext ) {
        this.testContext = testContext;
    }
    @Override
    public void given(String endPoint, String apiName) {
        TNTEventSubscriptionTO tntEventSubscriptionTO = TestUtility.getConfigTNTEventSubscriptionTO();
        testContext.setApiName(apiName);
        testContext.setCallbackURL(tntEventSubscriptionTO.getCallbackUrl());
        this.builder = new RequestSpecBuilder();
        this.builder.addHeader("API-Version", Configuration.API_VERSION.split("\\.")[0]);
        this.builder.setBaseUri(Configuration.ROOT_URI+"/v"+Configuration.API_VERSION.split("\\.")[0]);
        this.builder.setContentType(ContentType.JSON);
    }

    private RequestSpecification buildRequest() {
        RequestSpecification request= RestAssured
                .given()
                .header( new Header("Notification-Signature", "sha256=05ca1872139f56e039040228f4e55c51550b37c50d905b44314325d171299de7"))
                .relaxedHTTPSValidation()
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
    public void head() {
        response = buildRequest().head();
        if (response.getStatusCode() == 201) {
            Map<String, String> pathVariables = new HashMap<>();
            pathVariables.put(TestUtility.getIdentifierAttribute(testContext.getApiName()), "");
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
        testContext.getValidationResults().add(new ValidationResult());
        validatableResponseExtensionImpl=new ValidatableResponseExtensionImpl(testContext);
        return validatableResponseExtensionImpl;
    }

    @Override
    public void setCallbackUri(String callbackUri) {
        this.callbackUri = callbackUri;
        String basePath = callbackUri.substring(callbackUri.lastIndexOf('/') + 1);
        this.builder.setBaseUri(Configuration.CALLBACK_URI+ ":" + Configuration.CALLBACK_PORT);
        this.builder.setBasePath(Configuration.CALLBACK_PATH+"/"+basePath);
    }
}
