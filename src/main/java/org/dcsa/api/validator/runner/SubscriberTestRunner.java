package org.dcsa.api.validator.runner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(plugin = {"pretty",
        "html:target/cucumber-reports/cucumber.html",
        "json:target/cucumber-reports/cucumber.json"
}, features = {"src/main/java/org/dcsa/api/validator/features/tnt/v2/Subscriber.feature"}, glue = {"org.dcsa.api.validator.steps"}
)
public class SubscriberTestRunner extends AbstractTestNGCucumberTests {

}
