package org.dcsa.api.validator.model;

import groovy.util.logging.Slf4j;
import lombok.Data;

import lombok.extern.log4j.Log4j;

import java.util.Map;

@Data
public class TestSuite {
    String endPoint;
    String resourceIdentifier;
    String templateFile;
    String responseSchema;
    Map<String, TestCase> testCases;
}
