package org.dcsa.api.validator.model.enums;

import java.util.Arrays;

public enum ReportType {
    HTML("html"),
    NEWMAN("newman"),
    UNKNOWN("unknown");

    private String type;
    private ReportType(String value) {
        this.type = value;
    }

    public static ReportType fromName(String value) {
        for (ReportType osType : values()) {
            if (osType.type.equalsIgnoreCase(value)) {
                return osType;
            }
        }
        return UNKNOWN;
        /*throw new IllegalArgumentException(
                "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));*/
    }
}
