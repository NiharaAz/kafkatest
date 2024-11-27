package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

// The test runner class is used to configure the integration with Spring and JUnit 5
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:feature", // path to feature files
        glue = {"com/example/kafka_test/steps", "com/example/kafka_test/configuration"},
        plugin = { "pretty",
                "html:target/cucumber-html-report" }// path to step definition classes
        )
public class testrunner {
}
