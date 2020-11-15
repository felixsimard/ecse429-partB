package ecse429.storytesting;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
@RunWith(Cucumber.class)
@CucumberOptions(
		monochrome = true,
		features = "src/test/resources/ecse429/storytesting/",
		plugin = {"pretty"}
		//tags = "@Story09, @Story03, @Story05, @Story06, @Story01, @Story08, Story10, Story07, @Story02, @Story04"
		//tags = "@Story05, @Story04, @Story02, @Story07, @Story01, @Story08, Story09, Story06, @Story03, @Story10"
		)
public class RunCucumberTest{
}
