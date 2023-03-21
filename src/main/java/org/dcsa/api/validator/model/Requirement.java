package org.dcsa.api.validator.model;

import lombok.Data;

@Data
public class Requirement {

    private String requirementDescription;
    private String requirementSource;
    private String testApproach;

    public Requirement(){

    }

}
