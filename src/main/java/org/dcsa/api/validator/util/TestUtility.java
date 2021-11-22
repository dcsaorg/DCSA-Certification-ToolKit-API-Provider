package org.dcsa.api.validator.util;

import org.dcsa.api.validator.model.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class TestUtility {
    private static TestDB testDB;
    private static TestDataSet testDataSet;

    public static TestDB loadTestSuite(String testSuiteJson) {
        String jsonString = FileUtility.loadFileAsString(testSuiteJson);
        testDB = JsonUtility.getObjectFromJson(TestDB.class, jsonString);
        for (Map.Entry<String, TestSuite> entry : testDB.getTestSuites().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().getResponseSchema());
        }
        return testDB;
    }

    public static TestDB loadTestData(String testSuiteJson) {
        String jsonString = FileUtility.loadFileAsString(testSuiteJson);
        testDataSet = JsonUtility.getObjectFromJson(TestDataSet.class, jsonString);

        return testDB;
    }

    public static TestCase getTestCase(String apiName, String testName) {
        TestCase testCase = testDB.getTestSuites().get(apiName).getTestCases().get(testName);
        return testCase;
    }

    public static TestCase getTestCase(String apiName) {
        TestCase testCase = testDB.getTestSuites().get(apiName).getTestCases().get("EventTypeSHIPMENT");
        return testCase;
    }


    public static Map<String, TestCase> getTestCases(String apiName) {
        Map<String, TestCase> testCases = testDB.getTestSuites().get(apiName).getTestCases();
        return testCases;
    }

    public static Map<String, Object> getTestData(String apiName) {
        Map<String, Object> test = testDataSet.getTestDataSet().get(apiName).getPost().get(0);
        return test;
    }

    public static List<Map<String, Object>> getAllTestDataForGet(String apiName) {
        List<Map<String, Object>> test = testDataSet.getTestDataSet().get(apiName).getGet();
        return test;
    }

    public static List<Map<String, Object>> getAllTestDataForPost(String apiName) {
        List<Map<String, Object>> test = testDataSet.getTestDataSet().get(apiName).getPost();
        return test;
    }

    public static TestRequest getTestRequest(TestCase testCase) {
        TestRequest testRequest = testCase.getRequest();
        return testRequest;
    }

    public static String getTemplateFile(String apiName) {
        String templateFile = testDB.getTestSuites().get(apiName).getTemplateFile();
        return templateFile;
    }

    public static String getTemplateFile(TestCase testCase) {
        String templateFile = null;
        if (testCase.getRequest() != null)
            templateFile = testCase.getRequest().getTemplateFile();
        return templateFile;
    }

    public static String getIdentifierAttribute(String apiName) {
        String identifierAttribute = testDB.getTestSuites().get(apiName).getResourceIdentifier();
        return identifierAttribute;
    }

    public static String getEndPoint(String apiName) {
        String uri = testDB.getTestSuites().get(apiName).getEndPoint();
        return uri;
    }

    public static Map<String, Object> getPlaceholders(TestCase test) {
        Map<String, Object> placeHolders = null;
        if (TestUtility.getTestRequest(test) != null)
            placeHolders = TestUtility.getTestRequest(test).getPlaceHolders();
        return placeHolders;
    }

    public static Map<String, String> getAttributeFromGetTestData(List<String> attributes, String apiName) {

        List<Map<String, Object>> getTestDataSet = TestUtility.getAllTestDataForGet(apiName);
        List<Map<String, Object>> logicalPostTestDataSet = new ArrayList<>();
        for (Map<String, Object> getTestData : getTestDataSet) {
            if (getTestData.keySet().containsAll(attributes)) {
                logicalPostTestDataSet.add(getTestData);
            }
        }
        if (logicalPostTestDataSet.size() <= 0) {
            System.out.println("No Data Set found matching test attributes! Please check test case setup");
            return null;
        }


        Map<String, Object> testData = logicalPostTestDataSet.get(0);
        Map<String, String> newQueryParameters = new HashMap<>();
        for (String query : attributes) {
            List<String> tokens = Arrays.asList(query.split(":"));
            String prefix = null;
            if (tokens.size() > 1) {
                query = tokens.get(0);
                prefix = tokens.get(1);
            }
            if (testData.get(query) != null && testData.get(query) != "") {
                if (prefix != null)
                    newQueryParameters.put(query + ":" + prefix, (String) testData.get(query));
                    //need to do more handing for data type
                else
                    newQueryParameters.put(query, (String) testData.get(query));
            }
        }
        return newQueryParameters;
    }

    public static String getBodyForCreate(String resource) {
        String body = FileUtility.loadFileAsString(TestUtility.getTemplateFile(resource));
        if (!body.isEmpty()) {
            body = JsonUtility.replacePlaceHolders(body, TestUtility.getTestData(resource));
        }
        return body;
    }

    public static String getTestBody(String apiName, String testName) {
        TestCase testContext = TestUtility.getTestCase(apiName, testName);
        return getTestBody(apiName, testContext);
    }

    public static String getTestBody(String apiName, String body, TestCase testContext) {
        return getUpdateDBody(apiName, body, testContext);
    }

    public static String getTestBody(String apiName, String body, String testName) {
        TestCase testContext = TestUtility.getTestCase(apiName, testName);
        return getUpdateDBody(apiName, body, testContext);
    }

    public static String getTestBody(String apiName, TestCase testContext) {
        String body = null;
        if (testContext.getRequest() != null && testContext.getRequest().getTemplateFile() != null)
            body = FileUtility.loadFileAsString(testContext.getRequest().getTemplateFile());
        else
            body = FileUtility.loadFileAsString(TestUtility.getTemplateFile(apiName));

        return getUpdateDBody(apiName, body, testContext);
    }

    public static String getUpdateDBody(String apiName, String body, TestCase testContext) {
        if (!body.isEmpty()) {
            body = JsonUtility.replacePlaceHolders(body, testContext.getRequest().getPlaceHolders());
            List<String> testAttributes = testContext.getRequest().getDynamicPlaceHolders();
            if (testAttributes != null && !(testAttributes.isEmpty())) {
                List<String> testAttributesWithOutJsonPath = new ArrayList<>();
                for (int i = 0; i < testAttributes.size(); i++) {
                    String[] tokens=testAttributes.get(i).split("\\.");
                    testAttributesWithOutJsonPath.add(tokens[tokens.length-1]);
                }
                Map<String, Object> testAttributeMap = getAttributeFromPOSTTestData(apiName, testAttributesWithOutJsonPath);
                body = JsonUtility.replacePlaceHolders(body, testAttributeMap);
            }
            if (testContext.getRequest().getRemovalAttributes() != null)
                body = JsonUtility.removeAttributes(body, testContext.getRequest().getRemovalAttributes());
        }
        return body;
    }

    public static Map<String, Object> getAttributeFromPOSTTestData(String apiName, List<String> attributes) {
        List<Map<String, Object>> postTestDataSet = TestUtility.getAllTestDataForPost(apiName);
        List<Map<String, Object>> logicalPostTestDataSet = new ArrayList<>();
        for (Map<String, Object> postTestData : postTestDataSet) {
            if (postTestData.keySet().containsAll(attributes)) {
                logicalPostTestDataSet.add(postTestData);
            }
        }
        if (logicalPostTestDataSet.size() > 0) {
            return logicalPostTestDataSet.get(0);
        } else {
            System.out.println("No Data Set found matching test attributes! Please check test case setup");
        }
        return null;
    }

    public static String getResponseSchema(String apiName) {
        String responseSchema = testDB.getTestSuites().get(apiName).getResponseSchema();
        return responseSchema;
    }

    public static String getResponseSchema(TestCase test) {
        String responseSchema = getTestRequest(test).getResponseSchema();
        return responseSchema;
    }

}
