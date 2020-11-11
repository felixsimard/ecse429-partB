package ecse429.storytesting;

import cucumber.api.DataTable;
import cucumber.api.java.en.*;
import cucumber.api.PendingException;

import java.util.List;
import java.util.Map;

public class StepDefinitions {

    @Given("a task containing")
    public void a_task_containing(DataTable table) {
        /* extract to-do's data */
        List<Map<String, String>> list = table.asMaps(String.class, String.class);
        String title = list.get(0).get("title");
        boolean completed = Boolean.parseBoolean(list.get(0).get("completed"));
        boolean active = Boolean.parseBoolean(list.get(0).get("active"));
        String description = list.get(0).get("description");

        /* create the to-do */
        HelperFunctions.createTodo(title, completed, active, description);
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

}
