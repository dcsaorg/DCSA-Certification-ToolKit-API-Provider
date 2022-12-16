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
    TNT_2_2_SUB_PRV_9("TNT.2.2.SUB.PRV.9","Shared secret is not exposed in API endpoints"),
    TNT_2_2_SUB_PRV_10("TNT.2.2.SUB.PRV.10", "Subscription requested must be rejected if the secrets are not adequate for the signature algorithm"),
    TNT_2_2_EVT_PRV_1("TNT.2.2.EVT.PRV.1", "GET /events returns all events"),
    TNT_2_2_SUB_CSM_201("TNT.2.2.SUB.CSM.201", ""),
    TNT_2_2_API_CSM_400("TNT.2.2.API.CSM.400", ""),
    TNT_2_2_API_CSM_200("TNT.2.2.API.CSM.200", ""),
    TNT_2_2_SUB_CSM_3("TNT.2.2.SUB.CSM.3", ""),
    TNT_2_2_API_SUB_CSM_200("TNT.2.2.API.SUB.CSM.200", ""),
    TNT_2_2_API_SUB_CSM_202("TNT.2.2.API.SUB.CSM.202", ""),
    TNT_2_2_API_SUB_CSM_403("TNT.2.2.API.SUB.CSM.403", ""),
    TNT_2_2_API_SUB_CSM_400("TNT.2.2.API.SUB.CSM.400", ""),
    TNT_2_2_CSM_200("TNT.2.2.CSM.200", ""),
    TNT_2_2_CSM_HEAD_200("TNT.2.2.CSM.HEAD.200", ""),
    TNT_2_2_CSM_HEAD_404("TNT.2.2.CSM.HEAD.404", ""),
    TNT_2_2_CSM_POST_200("TNT.2.2.CSM.POST.200", ""),
    TNT_2_2_CSM_POST_400("TNT.2.2.CSM.POST.400", ""),
    TNT_2_2_SUB_CSM_TIME_200("TNT.2.2.SUB.CSM.TIME.200", ""),
    TNT_2_2_SUB_CSM_HEAD_404("TNT.2.2.SUB.CSM.HEAD.404", ""),
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
    TNT_2_2_ERR_PRV_10("TNT.2.2.ERR.PRV.10","Test case PUT /event-subscriptions with invalid attribute value validating \"bad request\" - HTTP 400");
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
