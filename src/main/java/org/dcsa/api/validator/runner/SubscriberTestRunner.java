package org.dcsa.api.validator.runner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(plugin = {"pretty",
        "html:reports/ExecutionReport.html",
}, features = {"src/main/java/org/dcsa/api/validator/features/tnt/v2/Subscriber.feature"}, glue = {"org.dcsa.api.validator.steps.common","org.dcsa.api.validator.steps.notification"}
)
public class SubscriberTestRunner extends AbstractTestNGCucumberTests {

}
