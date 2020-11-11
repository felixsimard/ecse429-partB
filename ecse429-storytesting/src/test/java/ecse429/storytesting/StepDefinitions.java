package ecse429.storytesting;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.*;
import cucumber.api.PendingException;
import ecse429.storytesting.Model.Todo;

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

    private Map

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


    @Given("a task containing")
    public void a_task_containing(DataTable table) {
        /* extract to-do's data */
        List<Map<String, String>> list = table.asMaps(String.class, String.class);
        String title = list.get(0).get("title");
        boolean completed = Boolean.parseBoolean(list.get(0).get("completed"));
        boolean active = Boolean.parseBoolean(list.get(0).get("active"));
        String description = list.get(0).get("description");

        // create the to-do
        HelperFunctions.createProject(title, completed, active, description);
    }

    @Given("^a category containing$")
    public void a_category_containing(DataTable arg1) throws Exception {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // List<YourType>, List<List<E>>, List<Map<K,V>> or Map<K,V>.
        // E,K,V must be a scalar (String, Integer, Date, enum etc)
        throw new PendingException();
    }

    @When("^I link the above task to the (.*) priority category$")
    public void i_link_the_above_task_to_the_priority_category(String arg1) throws Exception {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^the task is categorized with the corresponding priority$")
    public void the_task_is_categorized_with_the_corresponding_priority() throws Exception {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Given("^multiple categories containing$")
    public void multiple_categories_containing(DataTable arg1) throws Exception {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // List<YourType>, List<List<E>>, List<Map<K,V>> or Map<K,V>.
        // E,K,V must be a scalar (String, Integer, Date, enum etc)
        throw new PendingException();
    }

    @Given("^a link between the above task and a high priority category$")
    public void a_link_between_the_above_task_and_a_high_priority_category() throws Exception {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }



    @Given("^a course as a project$")
    public void a_course_as_a_project(DataTable arg1) throws Exception {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // List<YourType>, List<List<E>>, List<Map<K,V>> or Map<K,V>.
        // E,K,V must be a scalar (String, Integer, Date, enum etc)
    }
    @Given("^created tasks$")
    public void created_tasks(DataTable table) throws Exception {
        Todo assignment1;
        Todo assignment2;
        Todo project1;

        List<Map<String, String>> list = table.asMaps(String.class, String.class);
        Map<String, String> ass1Map = list.get(0);
        assignment1 = HelperFunctions.createTodo(ass1Map.get("title"), Boolean.parseBoolean(ass1Map.get("doneStatus")), ass1Map.get("description"));
        Context.getContext().set(ass1Map.get("title"), assignment1.id);
    }
    @Given("^\"([^\"]*)\" and \"([^\"]*)\" are added to the class \"([^\"]*)\" todo list$")
    public void and_are_added_to_the_class_todo_list(String arg1, String arg2, String arg3) throws Exception {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
    @When("^I remove ([^\"]*) from the class ([^\"]*) todo list$")
    public void i_remove_from_the_class_todo_list(String title, String classTitle) throws Exception {

        int statusCode = HelperFunctions.deleteTodoFromProject(Context.getContext().get(title), Context.getContext().get(classTitle));

        assertEquals();
    }
    @Then("^the returned statusCode is \\{int}$")
    public void the_returned_statusCode_is(int arg1) throws Exception {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

}
