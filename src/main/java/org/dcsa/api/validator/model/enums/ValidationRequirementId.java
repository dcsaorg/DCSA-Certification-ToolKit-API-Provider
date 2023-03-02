package org.dcsa.api.validator.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum ValidationRequirementId {
    TNT_22_DPY_PRV_2("TNT.2.2.DPY.PRV.2", "Major Version number must be present in URL"),
    TNT_2_2_API_PRV_1("TNT.2.2.API.PRV.1", "Full Version number present in response headers"),
    TNT_2_2_API_PRV_3("TNT.2.2.API.PRV.3", "HTTP verbs (GET, PUT, PATCH, POST, DELETE, HEAD) must be in request"),
    TNT_2_2_API_PRV_4("TNT.2.2.API.PRV.4", "URL must adhere to Kebab-case"),
    TNT_2_2_API_PRV_5("TNT.2.2.API.PRV.5", "Content type headers must be application/json"),
    TNT_2_2_API_PRV_6("TNT.2.2.API.PRV.6", "Links to current, previous, next, first and last page SHOULD be available in the response headers"),
    TNT_2_2_API_PRV_7("TNT.2.2.API.PRV.7", "Encoding of API must be in UTF8 format"),
    TNT_2_2_SUB_PRV_1("TNT.2.2.SUB.PRV.1", "GET /event-subscriptions returns all subscriptions without any query parameter"),
    TNT_2_2_SUB_PRV_2("TNT.2.2.SUB.PRV.2", "GET /event-subscriptions returns all subscriptions with limit parameter"),
    TNT_2_2_SUB_PRV_3("TNT.2.2.SUB.PRV.3", "POST /event-subscriptions creates a new subscription for different Event types"),
    TNT_2_2_SUB_PRV_4("TNT.2.2.SUB.PRV.4", "GET /event-subscriptions/{subscriptionID} Fetches a specific subscription with ID={subscriptionID} with valid id"),
    TNT_2_2_SUB_PRV_5("TNT.2.2.SUB.PRV.5", "PUT /event-subscriptions/{subscriptionID} Updates a specific subscription with ID={subscriptionID} with valid request"),
    TNT_2_2_SUB_PRV_6("TNT.2.2.SUB.PRV.6", "PUT /event-subscriptions/{subscriptionID} update secret receive invalid response"),
    TNT_2_2_SUB_PRV_7("TNT.2.2.SUB.PRV.7", "PUT /event-subscriptions/{subscriptionID} update secret"),
    TNT_2_2_SUB_PRV_8("TNT.2.2.SUB.PRV.8", "DELETE /event-subscriptions/{subscriptionID} with valid ID"),
    TNT_2_2_SUB_PRV_9("TNT.2.2.SUB.PRV.9","DELETE /event-subscriptions/{subscriptionID} delete with non existed ID fails with response 404"),
    TNT_2_2_SUB_PRV_10("TNT.2.2.SUB.PRV.10", "Subscription requested must be rejected if the secrets are not adequate for the signature algorithm"),
    TNT_2_2_SUB_PRV_11("TNT.2.2.SUB.PRV.11","Shared secret is not exposed in API endpoints"),
    TNT_2_2_EVT_PRV_1("TNT.2.2.EVT.PRV.1", "GET /events returns all events"),
    TNT_2_2_DPY_PRV_2("TNT_2_2_DPY_PRV_2", "Major Version number must be present in URL"),
    TNT_2_2_ERR_PRV_1("TNT.2.2.ERR.PRV.1","Test case GET /events with invalid parameter name validating \"bad request\" - HTTP 400"),
    TNT_2_2_ERR_PRV_2("TNT.2.2.ERR.PRV.2","Test case validating \"bad request\" - HTTP 400 with invalid values of filter parameters"),
    TNT_2_2_ERR_PRV_3("TNT.2.2.ERR.PRV.3","Test case POST /event-subscriptions with missing mandatory attributes validating \"bad request\" - HTTP 400"),
    TNT_2_2_ERR_PRV_4("TNT.2.2.ERR.PRV.4","Test case POST /event-subscriptions  with invalid attribute value validating \"bad request\" - HTTP 400"),
    TNT_2_2_ERR_PRV_5("TNT.2.2.ERR.PRV.5","Test case GET /event-subscriptions with invalid ID validating \"bad request\" - HTTP 400"),
    TNT_2_2_ERR_PRV_6("TNT.2.2.ERR.PRV.6","Test case GET /event-subscriptions with invalid Id validating \"not found\" request - HTTP 404"),
    TNT_2_2_ERR_PRV_7("TNT.2.2.ERR.PRV.7","Test case DELETE /event-subscriptions with invalid ID validating \"bad request\" - HTTP 400"),
    TNT_2_2_ERR_PRV_8("TNT.2.2.ERR.PRV.8","Test case PUT /event-subscriptions with mismatch of subscriptionID in body and URL validating \"bad request\" - HTTP 400"),
    TNT_2_2_ERR_PRV_9("TNT.2.2.ERR.PRV.9","Test case DELETE /event-subscriptions with invalid ID validating \"not found\" request - HTTP 404"),
    TNT_2_2_ERR_PRV_10("TNT.2.2.ERR.PRV.10","Test case PUT /event-subscriptions with invalid attribute value validating \"bad request\" - HTTP 400"),
    TNT_2_2_PUB_SUB_1("TNT.2.2.PUB.SUB.1", "Http code 204 for HEAD notification must be accepted as the only valid response. Callback URL HEAD response successful"),
    TNT_2_2_PUB_SUB_2("TNT.2.2.PUB.SUB.2", "Notification HEAD request must be received. Receipt of a HEAD request and success response"),
    TNT_2_2_PUB_SUB_3("TNT.2.2.PUB.SUB.3", "Invalid callback URL Head request must be rejected. Rejection of a head request"),
    TNT_2_2_PUB_SUB_4("TNT.2.2.PUB.SUB.4", "Subscription requested must be rejected if the secrets are not adequate for the signature algorithm"),
    TNT_2_2_PUB_SUB_5("TNT.2.2.PUB.SUB.5", "Notification must rotated secret. Receipt an event after secret rotation"),
    TNT_2_2_PUB_SUB_6("TNT.2.2.PUB.SUB.6", "Notification POST request must be received. Receipt of a POST request and success response"),
    TNT_3_0_REQUIREMENT_ID_1("TNT.3.0-REQUIREMENT-ID-1", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_1_1("TNT.3.0-REQUIREMENT-ID-1-1", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_1_2("TNT.3.0-REQUIREMENT-ID-1-2", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_2("TNT.3.0-REQUIREMENT-ID-2", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_3("TNT.3.0-REQUIREMENT-ID-3", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_4("TNT.3.0-REQUIREMENT-ID-4", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_5("TNT.3.0-REQUIREMENT-ID-5", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_6("TNT.3.0-REQUIREMENT-ID-6", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_7("TNT.3.0-REQUIREMENT-ID-7", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_7_1("TNT.3.0-REQUIREMENT-ID-7-1", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_7_2("TNT.3.0-REQUIREMENT-ID-7-2", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_8("TNT.3.0-REQUIREMENT-ID-8", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_9("TNT.3.0-REQUIREMENT-ID-9", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_10("TNT.3.0-REQUIREMENT-ID-10", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_11("TNT.3.0-REQUIREMENT-ID-11", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_12("TNT.3.0-REQUIREMENT-ID-12", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_13("TNT.3.0-REQUIREMENT-ID-13", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_13_1("TNT.3.0-REQUIREMENT-ID-13-1", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_13_2("TNT.3.0-REQUIREMENT-ID-13-2", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_14("TNT.3.0-REQUIREMENT-ID-14", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_15("TNT.3.0-REQUIREMENT-ID-15", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_16("TNT.3.0-REQUIREMENT-ID-16", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_16_1("TNT.3.0-REQUIREMENT-ID-16-1", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_16_2("TNT.3.0-REQUIREMENT-ID-16-2", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_17("TNT.3.0-REQUIREMENT-ID-17", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_18("TNT.3.0-REQUIREMENT-ID-18", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_19("TNT.3.0-REQUIREMENT-ID-19", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_19_1("TNT.3.0-REQUIREMENT-ID-19-1", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_19_2("TNT.3.0-REQUIREMENT-ID-19-2", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_20("TNT.3.0-REQUIREMENT-ID-20", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_20_1("TNT.3.0-REQUIREMENT-ID-20-1", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_20_2("TNT.3.0-REQUIREMENT-ID-20-2", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_20_3("TNT.3.0-REQUIREMENT-ID-20-3", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_20_4("TNT.3.0-REQUIREMENT-ID-20-4", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_21("TNT.3.0-REQUIREMENT-ID-21", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_21_1("TNT.3.0-REQUIREMENT-ID-21-1", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_21_2("TNT.3.0-REQUIREMENT-ID-21-2", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_22("TNT.3.0-REQUIREMENT-ID-22", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_22_1("TNT.3.0-REQUIREMENT-ID-22-1", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_22_2("TNT.3.0-REQUIREMENT-ID-22-2", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_22_3("TNT.3.0-REQUIREMENT-ID-22-3", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_23("TNT.3.0-REQUIREMENT-ID-23", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_23_1("TNT.3.0-REQUIREMENT-ID-23-1", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_23_2("TNT.3.0-REQUIREMENT-ID-23-2", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_24("TNT.3.0-REQUIREMENT-ID-24", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_25("TNT.3.0-REQUIREMENT-ID-25", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_25_1("TNT.3.0-REQUIREMENT-ID-25-1", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_25_2("TNT.3.0-REQUIREMENT-ID-25-2", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_26("TNT.3.0-REQUIREMENT-ID-26", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_26_1("TNT.3.0-REQUIREMENT-ID-26-1", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_26_2("TNT.3.0-REQUIREMENT-ID-26-2", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_27("TNT.3.0-REQUIREMENT-ID-27", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_28("TNT.3.0-REQUIREMENT-ID-28", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_28_1("TNT.3.0-REQUIREMENT-ID-28-1", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_28_2("TNT.3.0-REQUIREMENT-ID-28-2", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_28_3 ("TNT.3.0-REQUIREMENT-ID-28-3", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_29("TNT.3.0-REQUIREMENT-ID-29", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_29_1("TNT.3.0-REQUIREMENT-ID-29-1", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_29_2("TNT.3.0-REQUIREMENT-ID-29-2", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_30("TNT.3.0-REQUIREMENT-ID-30", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_31("TNT.3.0-REQUIREMENT-ID-31", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_32("TNT.3.0-REQUIREMENT-ID-32", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_32_1("TNT.3.0-REQUIREMENT-ID-32-1", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_32_2("TNT.3.0-REQUIREMENT-ID-32-2", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_33("TNT.3.0-REQUIREMENT-ID-33", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_34("TNT.3.0-REQUIREMENT-ID-34", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_34_1("TNT.3.0-REQUIREMENT-ID-34-1", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_34_2("TNT.3.0-REQUIREMENT-ID-34-2", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_COMMON_1("TNT.3.0-REQUIREMENT-ID-COMMON-1", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_COMMON_2("TNT.3.0-REQUIREMENT-ID-COMMON-2", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_COMMON_3("TNT.3.0-REQUIREMENT-ID-COMMON-3", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_COMMON_4("TNT.3.0-REQUIREMENT-ID-COMMON-4", "Some demo requirement. It will be updated later" ),
    TNT_3_0_REQUIREMENT_ID_COMMON_5("TNT.3.0-REQUIREMENT-ID-COMMON-5", "Some demo requirement. It will be updated later" );

    private static final Map<String, ValidationRequirementId> RequirementId = new HashMap<>();
    private static final Map<String, ValidationRequirementId> RequirementDetails = new HashMap<>();

    static {
        for (ValidationRequirementId v : values()) {
            RequirementId.put(v.id, v);
            RequirementDetails.put(v.details, v);
        }
    }
    private final String id;
    private final String details;

    ValidationRequirementId(String id, String details) {
        this.id = id;
        this.details = details;
    }

    public String getId(){
        return id;
    }

    public String getDetails() {
        return details;
    }
    public static ValidationRequirementId getById(String id){
        return RequirementId.get(id);
    }
}
