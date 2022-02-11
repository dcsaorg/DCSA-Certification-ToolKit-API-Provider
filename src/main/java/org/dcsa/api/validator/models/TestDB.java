package org.dcsa.api.validator.models;

import lombok.Data;

import java.util.Map;

@Data
public class TestDB {
    Map<String, TestSuite> TestSuites;
}
