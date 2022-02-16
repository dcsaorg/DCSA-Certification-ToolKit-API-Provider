package org.dcsa.api.validator.model;

import lombok.Data;

import java.util.Map;

@Data
public class TestDB {
    Map<String, TestSuite> TestSuites;
}
