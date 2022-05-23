package org.dcsa.api.validator.util;


import org.apache.commons.codec.binary.Hex;
import org.dcsa.api.validator.model.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TestUtility {
    private static TestDB testDB;
    private static TestDataSet testDataSet;

    public static TestDB loadConfigDataFromResource(String testSuiteJson) {
        String jsonString = FileUtility.loadResourceAsString(testSuiteJson);
        testDB = JsonUtility.getObjectFromJson(TestDB.class, jsonString);
        for (Map.Entry<String, TestSuite> entry : testDB.getTestSuites().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().getResponseSchema());
        }
        return testDB;
    }

    public static TestDB loadConfigData(String testSuiteJson) {
        testDB = JsonUtility.getObjectFromJson(TestDB.class, testSuiteJson);
        for (Map.Entry<String, TestSuite> entry : testDB.getTestSuites().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().getResponseSchema());
        }
        return testDB;
    }

    public static TestDB loadTestDataFromResource(String testSuiteJson) {
        String jsonString = FileUtility.loadResourceAsString(testSuiteJson);
        testDataSet = JsonUtility.getObjectFromJson(TestDataSet.class, jsonString);
        return testDB;
    }

    public static TestDB loadTestData(String testData) {
        testDataSet = JsonUtility.getObjectFromJson(TestDataSet.class, testData);
        return testDB;
    }

    public static TestCase getTestCase(String apiName, String testName) {
        TestCase testCase = testDB.getTestSuites().get(apiName).getTestCases().get(testName);
        return testCase;
    }

    public static Map<String, TestCase> getTestCases(String apiName) {
        Map<String, TestCase> testCases = testDB.getTestSuites().get(apiName).getTestCases();
        return testCases;
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
        List<String> attributesWithoutPostFix = new ArrayList<>();
        for (String query : attributes) {
            List<String> tokens = Arrays.asList(query.split(":"));
            if (tokens.size() > 1) {
                query = tokens.get(0);
            }
                attributesWithoutPostFix.add(query);
        }

        for (Map<String, Object> getTestData : getTestDataSet) {
            if (getTestData.keySet().containsAll(attributesWithoutPostFix)) {
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


    public static String getTestBody(String apiName, String body, TestCase testContext) {
        if(body==null || body=="")
        {
            if (testContext.getRequest() != null && testContext.getRequest().getTemplateFile() != null)
                body = FileUtility.loadResourceAsString(testContext.getRequest().getTemplateFile());
            else
                body = FileUtility.loadResourceAsString(TestUtility.getTemplateFile(apiName));
        }
        return getUpdateDBody(apiName, body, testContext);
    }


    public static String getUpdateDBody(String apiName, String body, TestCase testContext) {
        if (!body.isEmpty()) {
            body = JsonUtility.replacePlaceHolders(body, testContext.getRequest().getPlaceHolders());
            List<String> testAttributes = testContext.getRequest().getDynamicPlaceHolders();
            if (testAttributes != null && !(testAttributes.isEmpty())) {
                List<String> testAttributesWithOutJsonPath = new ArrayList<>();
                for (int i = 0; i < testAttributes.size(); i++) {
                    String[] tokens = testAttributes.get(i).split("\\.");
                    testAttributesWithOutJsonPath.add(tokens[tokens.length - 1]);
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
            Map<String, Object> testData = new HashMap<>();
            for (Map.Entry<String, Object> entry : logicalPostTestDataSet.get(0).entrySet()) {
                if (attributes.contains(entry.getKey()))
                    testData.put(entry.getKey(), entry.getValue());
            }
            return testData;
        } else {
            System.out.println("No Data Set found matching test attributes! Please check test case setup");
        }
        return null;
    }

    public static String getResponseSchema(String apiName) {
        String responseSchema = testDB.getTestSuites().get(apiName).getResponseSchema();
        return responseSchema;
    }

    public static byte[] computeSignature(byte[] secretKey, byte[] payload) throws
            Exception {
        final String javaAlgorithmName = "HmacSHA256";
        Mac mac = Mac.getInstance(javaAlgorithmName);
        mac.init(new SecretKeySpec(secretKey, javaAlgorithmName));
        return mac.doFinal(payload);
    }

    public static String getSignature(String encodedKey, String payload)  {
        String notificationSignature="sha256=";
        byte[] key = Base64.getDecoder().decode(encodedKey.getBytes(StandardCharsets.UTF_8));
        byte[] payloadByteArray = payload.getBytes(StandardCharsets.UTF_8);
        byte[] signature;
        try {
            signature = computeSignature(key, payloadByteArray);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        notificationSignature+=Hex.encodeHexString(signature);
        System.out.println("Notification-Signature:"+ notificationSignature);
        System.out.println("< --- PAY LOAD (" + payloadByteArray.length + " bytes) --->");
        System.out.println(payload);
        return notificationSignature;
    }

    public static TestDB getTestDB() {
        System.out.println("stop");
        return testDB;
    }

    public static TestDataSet getTestDataSet() {
        System.out.println("stop");
        return testDataSet;
    }
}
