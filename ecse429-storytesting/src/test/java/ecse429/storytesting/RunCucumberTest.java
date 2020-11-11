package ecse429.storytesting;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
@RunWith(Cucumber.class)
@CucumberOptions(
		monochrome = true,
		features = "src/test/resources/ecse429/storytesting/",
		plugin = {"pretty"}
		)
public class RunCucumberTest{
}
