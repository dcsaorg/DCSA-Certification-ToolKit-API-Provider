package org.dcsa.api.validator.model;

import java.util.Arrays;

public enum UploadType {
    TestData("testData"),
    ConfigData("configData"),
    AppData("appData");

    private String value;

    private UploadType(String value) {
        this.value = value;
    }

    public static UploadType fromValue(String value) {
        for (UploadType uploadType : values()) {
            if (uploadType.value.equalsIgnoreCase(value)) {
                return uploadType;
            }
        }
        throw new IllegalArgumentException(
                "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
    }
}
