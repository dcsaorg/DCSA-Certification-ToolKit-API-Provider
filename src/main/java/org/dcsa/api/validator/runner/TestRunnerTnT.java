package org.dcsa.api.validator.runner;


import io.cucumber.messages.internal.com.google.common.base.Optional;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.TestNGCucumberRunner;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;


@CucumberOptions(plugin = {"pretty",
        "html:target/cucumber-reports/cucumber.html",
        "json:target/cucumber-reports/cucumber.json"
}, features = {"src/main/java/org/dcsa/api/validator/features/tnt/v2/EventSubscription.feature"}, glue = {"org.dcsa.api.validator.steps"}
)
public class TestRunnerTnT extends AbstractTestNGCucumberTests {

}
