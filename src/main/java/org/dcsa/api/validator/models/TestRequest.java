package org.dcsa.api.validator.models;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TestRequest {
    String templateFile;
    String responseSchema;
    Map<String, Object>  payload;
    Map<String, String> pathVariables=new HashMap<>();
    Map<String,String> queryParameters=new HashMap<>();;
    Map<String, Object> placeHolders=new HashMap<>();;
    List<String> removalAttributes;
    List<String> dynamicPathVariables;
    List<String> dynamicQueryParameters;
    List<String> dynamicPlaceHolders;
}
