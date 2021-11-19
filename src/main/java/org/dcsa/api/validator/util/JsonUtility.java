package org.dcsa.api.validator.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import io.restassured.path.json.exception.JsonPathException;
import net.minidev.json.JSONArray;

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
                Object jsonValue = JsonPath.parse(jsonString).read(attribute);
                if (jsonValue instanceof JSONArray) {
                    String[] valueArray = value.split(",");
                    isFound = ((JSONArray) jsonValue).containsAll(Arrays.asList(valueArray));
                } else if (jsonValue instanceof String) {
                    isFound = jsonValue.equals(value);
                } else if (jsonValue instanceof Boolean) {
                    isFound = jsonValue.equals(value == "true" ? true : false);
                } else if (jsonValue instanceof Double) {
                    isFound = jsonValue.equals(Double.valueOf(value));
                } else if (jsonValue instanceof Integer) {
                    isFound = jsonValue.equals(Integer.valueOf(value));
                } else if (jsonValue instanceof Long) {
                    isFound = jsonValue.equals(Long.valueOf(value));
                } else if (jsonValue instanceof Float) {
                    isFound = jsonValue.equals(Float.valueOf(value));
                } else if (jsonValue instanceof Short) {
                    isFound = jsonValue.equals(Short.valueOf(value));
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
        if (attribute != null & value != null && responseList != null) {
            int sizeOfList = 0;

            for (Map<String, Object> entry : responseList) {
                if (sizeOfList >= 10)//check only 10 responses to avoid long response scan
                    break;
                sizeOfList++;
                String jsonString = JsonPath.parse(entry).jsonString();
                if (!findAttributeWithValue(jsonString, attribute, value))
                isValidated = false;
            }
        }
        return isValidated;
    }

    public static <T> String replacePlaceHolders(String jsonString, Map<String, T> placeHolders) {
        if (placeHolders == null)
            return jsonString;
        else if (placeHolders.isEmpty())
            return jsonString;
        for (Map.Entry<String, T> placeholder : placeHolders.entrySet()) {
            if (placeholder.getValue() instanceof String) {
                String stringValue = placeholder.getValue().toString();
                if (stringValue.charAt(0) == '[' && stringValue.charAt(stringValue.length() - 1) == ']') {
                    String[] enumValues = (stringValue.substring(1, stringValue.length() - 1)).split(",");
                    jsonString = addJsonElement(jsonString, placeholder.getKey(), enumValues);
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


}
