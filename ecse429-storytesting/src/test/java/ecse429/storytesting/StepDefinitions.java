package ecse429.storytesting;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.*;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;


class IsItFriday {
    static String isItFriday(String today) {
    	return "Friday".equals(today) ? "TGIF" : "Nope";
    }
}

public class StepDefinitions {
		
	/*------ STORY EXAMPLE ------*/
	private String today;
    private String actualAnswer;
    
    @Given("^today is \"([^\"]*)\"$")
    public void today_is(String today) {
        this.today = today;
    }

	@When("^I ask whether it's Friday yet$")
	public void i_ask_whether_it_s_Friday_yet() {
		actualAnswer = IsItFriday.isItFriday(today);
	}

	@Then("^I should be told \"([^\"]*)\"$")
	public void i_should_be_told(String expectedAnswer) {
		assertEquals(expectedAnswer, actualAnswer);
	}

	/*----------------------------*/
	
	
	/*
    @Given("a task containing")
    public void step_def(DataTable table) {
        // extract to-do's data
        Map<String, String> row = table.asMap(String.class, String.class);
        String title = row.get("title");
        boolean completed = Boolean.parseBoolean(row.get("completed"));
        boolean active = Boolean.parseBoolean(row.get("active"));
        String description = row.get("description");

        // create the to-do
        HelperFunctions.createTodo(title, completed, active, description);
    }
    */

}
