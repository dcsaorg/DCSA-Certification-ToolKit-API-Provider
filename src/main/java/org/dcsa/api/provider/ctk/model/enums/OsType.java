package org.dcsa.api.provider.ctk.model.enums;

import java.util.Arrays;

public enum OsType {
    WINDOWS("Windows"),
    LINUX("Linux"),
    DOCKER("Docker");
    private String name;
    private OsType(String value) {
        this.name = value;
    }

    public static OsType fromName(String value) {
        for (OsType osType : values()) {
            if (osType.name.equalsIgnoreCase(value)) {
                return osType;
            }
        }
        throw new IllegalArgumentException(
                "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
    }
}
