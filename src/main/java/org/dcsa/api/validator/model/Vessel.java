package org.dcsa.api.validator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class Vessel {
    @JsonProperty("vesselIMONumber")
    private String vesselIMONumber;

    @JsonProperty("vesselName")
    private String vesselName;

    @JsonProperty("vesselFlag")
    private String vesselFlag;

    @JsonProperty("vesselCallSignNumber")
    private String vesselCallSign;

    @JsonProperty("vesselOperatorCarrierCode")
    private UUID vesselOperatorCarrierID;

    @JsonProperty("vesselOperatorCarrierCodeListProvider")
    private String vesselOperatorCarrierCodeListProvider;

    @JsonProperty("vessel_call_sign_number")
    String vesselCallSignNumber;

}

