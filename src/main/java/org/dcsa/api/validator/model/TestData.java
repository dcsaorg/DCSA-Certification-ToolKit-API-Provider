package org.dcsa.api.validator.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TestData {
    List<Map<String,Object>> post;
    List<Map<String,Object>> get;
}
