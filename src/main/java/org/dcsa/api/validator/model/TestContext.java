package org.dcsa.api.validator.model;

import io.cucumber.java.Scenario;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class TestContext {
    private String apiName;
    private Scenario scenario;
    private String callbackURL;
    private TestCase testCase;
    private List<Response> responseChain = new ArrayList<>();
    private List<RequestSpecification> requestChain = new ArrayList<>();
    private List<Map<String, String>> pathVariableChain = new ArrayList<>();

    public TestContext() {
        init();
    }

    public void init() {
        apiName = null;
        scenario = null;
        callbackURL = null;
        testCase = new TestCase();
        responseChain = new ArrayList<>();
        requestChain = new ArrayList<>();
        pathVariableChain = new ArrayList<>();
    }

}
