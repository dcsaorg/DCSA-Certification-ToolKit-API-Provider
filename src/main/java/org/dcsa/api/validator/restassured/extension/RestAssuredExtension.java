package org.dcsa.api.validator.restassured.extension;

import org.dcsa.api.validator.models.TestContext;
import org.dcsa.api.validator.restassured.extension.Impl.RestAssuredExtensionImpl;

import java.util.List;
import java.util.Map;

public interface RestAssuredExtension {
    void get();

    void delete();

    void put();

    void post();

    void given(String endPoint, String apiName);

    ValidatableResponseExtension then();

    RestAssuredExtension body(String body);

    RestAssuredExtension placeHolders(Map<String, Object> placeholderMap);

    RestAssuredExtension removeAttribute(List<String> attributes);

    RestAssuredExtension pathParams(Map<String, String> pathVariables);

    RestAssuredExtensionImpl queryParams(Map<String, String> queryParameters);

    TestContext getTestContext();

}
