package org.dcsa.api.validator.model;

import lombok.Data;

import java.util.Map;

@Data
public class TestSuite {
    String endPoint;
    String resourceIdentifier;
    String templateFile;
    String responseSchema;
    Map<String, TestCase> testCases;
}
