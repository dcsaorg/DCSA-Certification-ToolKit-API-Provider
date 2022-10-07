package org.dcsa.api.validator.model.enums;

public enum ScenarioNames {
    TNT_2_2_SUB_PRV_NEW_SUBSCRIPTION("TNT.2.2.SUB.PRV_NEW_SUBSCRIPTION request must be create new subscription with POST /event subscriptions with a valid Callback Url and secret");
    private String name;
    ScenarioNames(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
