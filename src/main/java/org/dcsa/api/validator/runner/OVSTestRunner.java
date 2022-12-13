package org.dcsa.api.validator.runner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(plugin = {"pretty",
        "html:reports/ExecutionReport.html",
}, features = {
        "src/main/resources/features/ovs/v2/TimeStamp.feature"
   },
   glue = {
        "org.dcsa.api.validator.steps.common"
   }
)
public class OVSTestRunner extends AbstractTestNGCucumberTests {

}
