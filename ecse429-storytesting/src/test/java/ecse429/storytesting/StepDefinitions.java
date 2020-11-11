package ecse429.storytesting;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.*;
import cucumber.api.PendingException;
import ecse429.storytesting.Model.Category;
import ecse429.storytesting.Model.Todo;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.get;
import static org.hamcrest.core.IsEqual.equalTo;
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

    @Given("^a task with title \"([^\"]*)\"$")
    public void a_task_with_title(String title) throws Exception {
        Todo todo = HelperFunctions.createTodo(title, false, "");
        Context.getContext().set("task_id", "" + todo.id);
    }

    @Given("^a category with the \"([^\"]*)\"$")
    public void a_category_with_the_HIGH(String title) throws Exception {
        Category category = HelperFunctions.createCategory(title, "");
        Context.getContext().set("category_id", category.id);
    }

    @When("^I link the task to the \"([^\"]*)\"$")
    public void i_link_the_task_to_the_HIGH(String priority) throws Exception {
        String task_id = Context.getContext().get("task_id");
        String category_id = Context.getContext().get("category_id");

        HelperFunctions.linkTodoAndCategory(task_id, category_id);
    }

    @Then("^the task is categorized with the \"([^\"]*)\"")
    public void the_task_is_categorized_with_the_HIGH(String priority) throws Exception {
        String task_id = Context.getContext().get("task_id");

        Category c = HelperFunctions.getCategoryFromTodoId(task_id);

        assertEquals(priority, c.title);
    }
}
//    @Given("a task containing")
//    public void a_task_containing(DataTable table) {
//        /* extract to-do's data */
//        List<Map<String, String>> list = table.asMaps(String.class, String.class);
//        String title = list.get(0).get("title");
//        boolean completed = Boolean.parseBoolean(list.get(0).get("completed"));
//        boolean active = Boolean.parseBoolean(list.get(0).get("active"));
//        String description = list.get(0).get("description");
//
//        // create the to-do
//        HelperFunctions.createTodo(title, completed, active, description);
//    }
