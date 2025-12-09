package Options;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/features",
        glue = {"stepDefinitions"},
        plugin = {"pretty", "json:target/jsonReports/list-report.json"},
        tags = "@ListTests"
)
public class ListTestRunner {

}
