package org.dcsa.api.validator.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum ValidationRequirementId {
    TNT_2_2_API_PRV_1("TNT.2.2.API.PRV.1", "Full Version number present in response headers"),
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
    TNT_2_2_DPY_PRV_2("TNT_2_2_DPY_PRV_2", "Major Version number must be present in URL");

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
