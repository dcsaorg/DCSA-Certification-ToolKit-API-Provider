package org.dcsa.api.validator.constant;

public enum TestStatusCode {
    FAILED("FAILED"),
    PASSED("PASSED");

    private final String name;

    TestStatusCode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}