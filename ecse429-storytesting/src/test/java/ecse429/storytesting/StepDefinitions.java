package ecse429.storytesting;

import cucumber.api.DataTable;
import cucumber.api.java.en.*;

import java.util.List;
import java.util.Map;

public class StepDefinitions {

    @Given("a task containing")
    public void step_def(DataTable table) {
        /* extract to-do's data */
        Map<String, String> row = table.asMap(String.class, String.class);
        String title = row.get("title");
        boolean completed = Boolean.parseBoolean(row.get("completed"));
        boolean active = Boolean.parseBoolean(row.get("active"));
        String description = row.get("description");

        /* create the to-do */
        HelperFunctions.createTodo(title, completed, active, description);
    }

}
