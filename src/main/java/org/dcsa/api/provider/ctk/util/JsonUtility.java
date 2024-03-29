package org.dcsa.api.provider.ctk.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.PathNotFoundException;
import org.dcsa.api.provider.ctk.model.Requirement;
import org.dcsa.api.provider.ctk.model.RequirementListWrapper;
import org.dcsa.api.provider.ctk.model.PostManFolderName;
import org.dcsa.api.provider.ctk.model.PostmanFolderNameWrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JsonUtility {

    public static <T> String getStringFormat(T t) {
        ObjectMapper mapper = new ObjectMapper();

        String inputJson = "";
        if (t != null) {
            try {
                inputJson = mapper.writeValueAsString(t);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return inputJson;
    }

    public static <T> T getObjectFromJson(Class<T> t1, String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        T t = null;
        try {
            t = (T) mapper.readValue(jsonString, t1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static String removeAttributes(String jsonRequest, List<String> attributes) {
        if (attributes == null)
            return jsonRequest;
        else if (attributes.isEmpty())
            return jsonRequest;
        for (String str : attributes) {
            jsonRequest = deleteJsonElement(jsonRequest, str);
        }
        return jsonRequest;
    }

    public static boolean findAttributeWithValue(String jsonString, String attribute, String value) {
        boolean isFound = false;
        if (jsonString != null && attribute != null && value != null) {
            try {
                List<String> tokens = Arrays.asList(attribute.split(":"));
                String operator = null;
                if (tokens.size() > 1) {
                    attribute = tokens.get(0);
                    operator = tokens.get(1);
                }
                Map<String, Object> jsonPaths = getJsonValues(jsonString);
                JsonNode jsonValue = (JsonNode) jsonPaths.get(attribute);
                if (jsonValue == null)
                    isFound = false;
                else if (operator != null) {
                    String operators[] = new String[]{"gte", "gt", "lte", "lt", "eq"};
                    List<String> operatorList = Arrays.asList(operators);
                    if (operatorList.contains(operator)) {
                        int result = ((jsonValue).asText()).compareToIgnoreCase(value);
                        if (result >= 0 && operator.equals("gte"))
                            isFound = true;
                        else if (result > 0 && operator.equals("gt"))
                            isFound = true;
                        else if (result <= 0 && operator.equals("lte"))
                            isFound = true;
                        else if (result < 0 && operator.equals("lt"))
                            isFound = true;
                        else if (result == 0 && operator.equals("eq"))
                            isFound = true;
                        else
                            isFound = false;
                    }
                } else if (jsonValue.isArray()) {
                    List<String> enums = new ArrayList<>();
                    for (JsonNode n : jsonValue) {
                        enums.add(n.asText());
                    }
                    String[] valueArray = value.split(",");
                    if (enums.size() == valueArray.length)
                        isFound = (enums).containsAll(Arrays.asList(valueArray));
                } else if (jsonValue.isTextual()) {
                    isFound = ((jsonValue).asText()).equals(value);
                } else if (jsonValue.isBoolean()) {
                    boolean tmpValue = (value == "true");
                    isFound = (jsonValue).asBoolean() == tmpValue;
                } else if (jsonValue.isDouble()) {
                    isFound = (jsonValue).asDouble() == Double.parseDouble(value);
                } else if (jsonValue.isInt()) {
                    isFound = (jsonValue).asInt() == Integer.parseInt(value);
                } else if (jsonValue.isLong()) {
                    isFound = (jsonValue).asLong() == Long.parseLong(value);
                } else if (jsonValue.isFloat()) {
                    isFound = (jsonValue).floatValue() == Float.parseFloat(value);
                } else if (jsonValue.isShort()) {
                    isFound = (jsonValue).shortValue() == Short.parseShort(value);
                }
            } catch (JsonPathException e) {
                //not found
                e.printStackTrace();
            }
        }
        return isFound;
    }

    public static boolean validateAttributeValue(List<Map<String, Object>> responseList, String attribute, String value) {
        boolean isValidated = true;
        if (attribute != null && value != null && responseList != null) {
            int sizeOfList = 0;
            for (Map<String, Object> entry : responseList) {
                if (sizeOfList >= 10)//check only 10 responses to avoid long response scan
                    break;
                String jsonString = JsonPath.parse(entry).jsonString();
                if (!findAttributeWithValue(jsonString, attribute, value))
                    isValidated = false;
                sizeOfList++;
            }
        }
        return isValidated;
    }

    public static String extractAttributeValue(String json, String attributeJsonPath) {
        String value = null;
        if (attributeJsonPath != null && json != null) {
            try {
                value = JsonPath.parse(json).read(attributeJsonPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }


    public static <T> String replacePlaceHolders(String jsonString, Map<String, T> placeHolders) {
        if (placeHolders == null)
            return jsonString;
        else if (placeHolders.isEmpty())
            return jsonString;
        for (Map.Entry<String, T> placeholder : placeHolders.entrySet()) {
            if (placeholder.getValue() instanceof String) {
                String stringValue = placeholder.getValue().toString();
                if (stringValue != null && !(stringValue.isEmpty())) {
                    if (stringValue.charAt(0) == '[' && stringValue.charAt(stringValue.length() - 1) == ']') {
                        String[] enumValues = (stringValue.substring(1, stringValue.length() - 1)).split(",");
                        jsonString = addJsonElement(jsonString, placeholder.getKey(), enumValues);
                    } else
                        jsonString = addJsonElement(jsonString, placeholder.getKey(), placeholder.getValue());
                } else
                    jsonString = addJsonElement(jsonString, placeholder.getKey(), placeholder.getValue());

            } else
                jsonString = addJsonElement(jsonString, placeholder.getKey(), placeholder.getValue());
        }
        return jsonString;
    }


    public static String deleteJsonElement(String jsonString, String path) {
        try {
            jsonString = JsonPath.parse(jsonString).delete(path).jsonString();
        } catch (PathNotFoundException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public static String addJsonElement(String jsonString, String path, Object value) {
        String parentPath;
        String name;
        try {
            int index = path.lastIndexOf('.');
            if (index != -1) {
                parentPath = path.substring(0, index);
                name = path.substring(index + 1);
            } else {
                parentPath = "$";
                name = path;
            }
            jsonString = JsonPath.parse(jsonString).put(parentPath, name, value).jsonString();
        } catch (PathNotFoundException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public static Map<String, Object> getJsonValues(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper(new JsonFactory());
            JsonNode rootNode = mapper.readTree(json);
            JsonParser parser = new JsonParser();
            return parser.process("", rootNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isList(String json) {
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
        if (rootNode.isArray())
            return true;
        else
            return false;
    }

    public static List<Requirement> convertRequirementIdJson(String resourceName) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = FileUtility.loadResourceAsString(resourceName);
        try {
            RequirementListWrapper requirementListWrapper = mapper.readValue(jsonString, RequirementListWrapper.class);
            return requirementListWrapper.getRequirement();
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public static List<PostManFolderName> getTestFolderNames(String resourceName){
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = FileUtility.loadResourceAsString(resourceName);
        try {
            PostmanFolderNameWrapper folderNameWrapper = mapper.readValue(jsonString, PostmanFolderNameWrapper.class);
            return folderNameWrapper.getFolder();
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
