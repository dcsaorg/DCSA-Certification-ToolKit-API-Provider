package org.dcsa.api.provider.ctk.model;
import java.util.List;

public class RequirementListWrapper {

    List<Requirement> requirement;

    public RequirementListWrapper(){

    }

    public List<Requirement> getRequirement() {
        return requirement;
    }

    public void setRequirement(List<Requirement> requirement) {
        this.requirement = requirement;
    }

}
