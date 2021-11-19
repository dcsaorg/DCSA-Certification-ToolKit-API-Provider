package org.dcsa.api.validator.restassured.extension;

import io.cucumber.java.Scenario;
import io.restassured.response.Response;
import org.dcsa.api.validator.model.TestCase;

import java.util.List;
import java.util.Map;

public interface RestAssuredExtension {
    void get();

    void delete();

    void put();

    void post();

    //void header(String header, String value);

    void given(String endPoint, String apiName);

    ValidatableResponseExtension then();

    RestAssuredExtension create();

    RestAssuredExtension create(String resource);

    RestAssuredExtension body(String body);

    RestAssuredExtension placeHolders(Map<String, Object> placeholderMap);

    RestAssuredExtension removeAttribute(List<String> attributes);

    RestAssuredExtension pathParams(Map<String, String> pathVariables);

    RestAssuredExtensionImpl queryParams(Map<String, String> queryParameters);

    void setScenario(Scenario scenario);

    String getApiName();

    List<Response> getHelperResponse();

    TestCase getTestContext();

}
