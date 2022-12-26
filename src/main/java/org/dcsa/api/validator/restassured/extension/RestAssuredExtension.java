package org.dcsa.api.validator.restassured.extension;

import org.dcsa.api.validator.model.TestContext;
import org.dcsa.api.validator.restassured.extension.Impl.RestAssuredExtensionImpl;

import java.util.List;
import java.util.Map;

public interface RestAssuredExtension {
    void head(String apiName);
    void head();
    void get();

    void delete();

    void put();

    void post();

    void given(String endPoint, String apiName);

    void configGiven();

    ValidatableResponseExtension then();

    RestAssuredExtension body(String body);

    RestAssuredExtension placeHolders(Map<String, Object> placeholderMap);

    RestAssuredExtension removeAttribute(List<String> attributes);

    RestAssuredExtension pathParams(Map<String, String> pathVariables);

    RestAssuredExtensionImpl queryParams(Map<String, String> queryParameters);

    TestContext getTestContext();

}
