package org.dcsa.api.validator.model;

import lombok.Data;
import org.dcsa.api.validator.constant.TestStatusCode;

@Data
public class HtmlReportModel {

    private String requirementId;
    private String requirement;
    private String testName;
    private TestStatusCode testStatusCode;
    private String failureReason;
    private String testDetails;

}
