package org.dcsa.api.validator.model.enums;

public enum PostmanCollectionType {
    TNT("tnt"),
    OVS("ovs"),
    EDOC("edoc"),
    EBL("ebl"),
    BOOKING("booking"),
    UNKNOWN("unknown");
    private String type;
    PostmanCollectionType(String value) {
        this.type = value;
    }
    public static PostmanCollectionType fromName(String value) {
        for (PostmanCollectionType osType : values()) {
            if (osType.type.equalsIgnoreCase(value)) {
                return osType;
            }
        }
        return UNKNOWN;
    }
}
