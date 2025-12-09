package Options;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/features",
        glue = {"stepDefinitions"},
        plugin = {"pretty", "json:target/jsonReports/cucumber-report.json"},
        tags = "@Regression"
)
public class TestRunner {

}
