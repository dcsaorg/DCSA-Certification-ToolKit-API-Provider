package org.dcsa.api.validator.model;

import io.cucumber.java.hu.De;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TestRequest {
    String templateFile;
    String responseSchema;
    Map<String, Object>  payload;
    Map<String, String> pathVariables;
    Map<String,String> queryParameters;
    Map<String, Object> placeHolders;
    List<String> removalAttributes;
    List<String> dynamicPathVariables;
    List<String> dynamicQueryParameters;
    List<String> dynamicPlaceHolders;
}
