package org.dcsa.api.provider.ctk.model;

import lombok.Data;

@Data
public class Requirement {

    private String requirementDescription;
    private String requirementSource;
    private String testApproach;

    public Requirement(){

    }

}
