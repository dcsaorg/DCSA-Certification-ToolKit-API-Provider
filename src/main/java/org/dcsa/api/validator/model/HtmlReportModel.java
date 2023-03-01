package org.dcsa.api.validator.model;

import lombok.Data;
import org.dcsa.api.validator.constant.TestStatusCode;
import org.dcsa.api.validator.model.enums.ValidationRequirementId;

@Data
public class HtmlReportModel {

    private String requirementId;
    private String requirement;
    private String testName;
    private TestStatusCode testStatusCode;
    private StringBuilder failureReason;
    private StringBuilder testDetails;

    private ValidationRequirementId validationRequirementID;

    private boolean isDetailsFilled;

    public HtmlReportModel(){
        testDetails= new StringBuilder();
        failureReason = new StringBuilder();
    }
}
