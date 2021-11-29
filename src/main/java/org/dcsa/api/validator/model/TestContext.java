package org.dcsa.api.validator.model;

import io.cucumber.java.Scenario;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TestContext {
    private String apiName;
    private Scenario scenario;
    private String callbackURL;
    private TestCase testCase;
    private String testCaseName;
    private String status;
    private String reasonOfFailure;
    private List<Response> responseChain = new ArrayList<>();
    private List<FilterableRequestSpecification> requestChain = new ArrayList<>();
    private List<Map<String, String>> pathVariableChain = new ArrayList<>();
    private List<String> message=new ArrayList<>();
    private List<ValidationResult> validationResults;

    public TestContext() {
        init();
    }

    public void init() {
        apiName = null;
        scenario = null;
        callbackURL = null;
        reasonOfFailure=null;
        testCase = new TestCase();
        responseChain = new ArrayList<>();
        requestChain = new ArrayList<>();
        pathVariableChain = new ArrayList<>();
        validationResults=new ArrayList<>();
    }

}
