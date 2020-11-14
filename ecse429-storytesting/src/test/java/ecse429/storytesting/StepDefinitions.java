package ecse429.storytesting;

//import com.sun.org.apache.xpath.internal.operations.Bool;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.*;
import cucumber.api.PendingException;
import ecse429.storytesting.Model.Category;
import ecse429.storytesting.Model.Id;
import ecse429.storytesting.Model.Project;
import ecse429.storytesting.Model.Todo;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import static io.restassured.RestAssured.get;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StepDefinitions {

	//--------BACKGROUND-----------//

	@Given("^the application is running$")
	public void the_application_is_running() throws Exception {
		try {
			HelperFunctions.stopApplication();
			Thread.sleep(260);
			boolean appStarted = false;
			Process process = HelperFunctions.startApplication();
			Thread.sleep(200);
			while (!appStarted) {
				try {
					get("http://localhost:4567/");
					appStarted = true;
				} catch (Exception e){
					Thread.sleep(200);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*----------------------------*/

	@Given("^a task with title \"([^\"]*)\"$")
	public void a_task_with_title(String title) throws Exception {
		Todo todo = HelperFunctions.createTodo(title, false, "");
		Context.getContext().set("task_id", todo.getId());
	}

	@Given("^a category with the title \"([^\"]*)\"$")
	public void a_category_with_the_HIGH(String title) throws Exception {
		Category category = HelperFunctions.createCategory(title, "");
		Context.getContext().set("category_id", category.getId());
	}

	@When("^I link the task to the category with title \"([^\"]*)\"$")
	public void i_link_the_task_to_the_HIGH(String priority) throws Exception {
		int task_id = Context.getContext().get("task_id");
		int category_id = Context.getContext().get("category_id");

		int statusCode = HelperFunctions.linkTodoAndCategory(task_id, category_id);
		Context.getContext().set("status_code", statusCode);
	}

	@Then("^the task is categorized with the title \"([^\"]*)\"")
	public void the_task_is_categorized_with_the_HIGH(String priority) throws Exception {
		int task_id = Context.getContext().get("task_id");

		Category c = HelperFunctions.getCategoryFromTodoId(task_id);

		assertEquals(priority, c.getTitle());
	}

	// -----------STORY04------------//

	@Given("^a course with title \"([^\"]*)\"$")
	public void a_course_with_title(String courseTitle) throws Exception {
		Project course = HelperFunctions.createProject(courseTitle, "", "", "");
		Context.getContext().set(courseTitle, course.getId());
	}

	@Given("^created tasks$")
	public void created_tasks(DataTable table) throws Exception {
		Todo assignment1;
		Todo assignment2;
		Todo homework1;

		List<Map<String, String>> list = table.asMaps(String.class, String.class);
		Map<String, String> ass1Map = list.get(0);
		assignment1 = HelperFunctions.createTodo(ass1Map.get("title"), Boolean.parseBoolean(ass1Map.get("doneStatus")),
				ass1Map.get("description"));
		Context.getContext().set(ass1Map.get("title"), assignment1.getId());

		Map<String, String> ass2Map = list.get(1);
		assignment2 = HelperFunctions.createTodo(ass2Map.get("title"), Boolean.parseBoolean(ass2Map.get("doneStatus")),
				ass2Map.get("description"));
		Context.getContext().set(ass2Map.get("title"), assignment2.getId());

		Map<String, String> hw1Map = list.get(2);
		homework1 = HelperFunctions.createTodo(hw1Map.get("title"), Boolean.parseBoolean(hw1Map.get("doneStatus")),
				hw1Map.get("description"));
		Context.getContext().set(hw1Map.get("title"), homework1.getId());

	}

	@Given("^\"([^\"]*)\" and \"([^\"]*)\" are added to the course \"([^\"]*)\" todo list$")
	public void and_are_added_to_the_course_todo_list(String ass1, String ass2, String courseTitle) throws Exception {
		HelperFunctions.addTodoToProject(Context.getContext().get(ass1), Context.getContext().get(courseTitle));
		HelperFunctions.addTodoToProject(Context.getContext().get(ass2), Context.getContext().get(courseTitle));
	}

	@When("^I remove \"([^\"]*)\" from the course \"([^\"]*)\" todo list$")
	public void i_remove_from_the_course_todo_list(String todoTitle, String courseTitle) throws Exception {
		int statusCode = HelperFunctions.deleteTodoFromProject(Context.getContext().get(todoTitle),
				Context.getContext().get(courseTitle));
		Context.getContext().set("status_code", statusCode);
	}

	@Then("^the returned statusCode is \"(\\d+)\"$")
	public void the_returned_statusCode_is(int statusCode) throws Exception {
		assertEquals(statusCode, Context.getContext().get("status_code"));
	}

	@Then("^the relationship between \"([^\"]*)\" and the course \"([^\"]*)\" is destroyed$")
	public void the_relationship_between_and_the_course_is_destroyed(String taskTitle, String courseTitle) throws Exception {
		Project courseTodoList = HelperFunctions.getProjectByProjectId(Context.getContext().get(courseTitle));
		int todoId = Context.getContext().get(taskTitle);

		//confirm that the task is no longer linked to the course to do list
		assert(courseTodoList.getTasks().stream().filter(id -> id.getId() == todoId).count() == 0);
	}

	//--------STORY05-------//

	@When("^I create a new to do list with title \"([^\"]*)\", completed status \"([^\"]*)\", active status \"([^\"]*)\", and description \"([^\"]*)\"$")
	public void i_create_a_new_to_do_list_with_title_completed_status_active_status_and_description(String title, String completed, String active, String description) throws Exception {
		HelperFunctions.createProject(title, completed, active, description);
	}

	@Then("^the returned status code is \"(\\d+)\"$")
	public void the_returned_status_code_is(int statusCode) throws Exception {
		assertEquals(statusCode, Context.getContext().get("status_code"));
	}



	/*--- Story7 ---*/

	/**
	 * This method will create a project with title equal to the input and store it in the Context variables map.
	 *
	 * @param projectTitle - The title of the project to be created.
	 */
	@Given("^a project with title \"([^\"]*)\"$")
	public void aProjectWithTitle(String projectTitle) {
		Project newProject = HelperFunctions.createProject(projectTitle,"","","");
		Context.getContext().set("story7_project", newProject.getId());
	}

	/**
	 * This method will create a certain amount of tasks with doneStatus=false connected to the project with name = "story7_project".
	 * The id's of the created tasks are then stored in a list in the Context listVariables Map with key="story7_falseTasks"
	 *
	 * @param arg0 - Number of tasks to be created with done Status false
	 */
	@And("^an initial set of tasks connected to the project with \"([^\"]*)\" tasks that have false as doneStatus value$")
	public void anInitialSetOfTasksConnectedToTheProjectWithTasksThatHaveFalseAsDoneStatusValue(String arg0) {
		int projectID = Context.getContext().get("story7_project");
		int numberOfTasks = Integer.parseInt(arg0);
		List<String> list = new ArrayList<String>();

		while(numberOfTasks > 0){
			Todo task = HelperFunctions.createTodo(String.format("Task %d that is not done", numberOfTasks),false,"");
			list.add((String.valueOf(task.getId())));
			HelperFunctions.addTodoToProject(task.getId(), projectID);
			numberOfTasks--;
		}
		Collections.sort(list);
		Context.getContext().setListVariables("story7_falseTasks", list);
	}

	/**
	 * This method will create a certain amount of tasks with doneStatus=true connected to the project with name = "story7_project".
	 * The id's of the created tasks are then stored in a list in the Context listVariables Map with key="story7_trueTasks"
	 *
	 * @param arg0 - Number of tasks to be created with done Status true
	 */
	@And("^another set of tasks connected to the project with \"([^\"]*)\" tasks that have true as doneStatus value$")
	public void anotherSetOfTasksConnectedToTheProjectWithTasksThatHaveTrueAsDoneStatusValue(String arg0) {
		int projectID = Context.getContext().get("story7_project");
		int numberOfTasks = Integer.parseInt(arg0);
		List<String> list = new ArrayList<String>();

		while(numberOfTasks > 0){
			Todo task = HelperFunctions.createTodo(String.format("Task %d that is done", numberOfTasks),true,"");
			list.add((String.valueOf(task.getId())));
			HelperFunctions.addTodoToProject(task.getId(), projectID);
			numberOfTasks--;
		}
		Collections.sort(list);
		Context.getContext().setListVariables("story7_trueTasks", list);
	}

	/**
	 * This method queries the number of tasks that are incomplete from the project with title="story7_project" and stores all ids of task
	 * in a list in the context
	 *
	 */
	@When("^I query the incomplete tasks for this project$")
	public void iQueryTheIncompleteTasksForThisProject() {
		int projectID = Context.getContext().get("story7_project");
		List<Integer> list = HelperFunctions.getAllIncompleteTasksOfProject(projectID);
		Context.getContext().setListVariables("story7_queryList", list);
	}

	@Then("^a set is returned which is identical to the initial set of tasks with value false for doneStatus and has \"([^\"]*)\" elements\\.$")
	public void aSetIsReturnedWhichIsIdenticalToTheInitialSetOfTasksWithValueFalseForDoneStatusAndHasElements(String arg0) {
		List<Integer> listFromQuery = Context.getContext().getListVariables("story7_queryList");
		List<Integer> actualIncompleteTasks = Context.getContext().getListVariables("story7_falseTasks");
		assertArrayEquals(listFromQuery.toArray(),actualIncompleteTasks.toArray());
	}

	/*---------------*/

	/*--- Story8 ---*/

	/*---------------*/

	/*--- Story9 ---*/

	/*---------------*/
	
	/*--- Story10 ---*/

	@Given("^a task with description \"([^\"]*)\"$")
	public void a_task_with_description(String description) throws Exception {
		Todo todo = HelperFunctions.createTodo("TestStory10", false, description);
		Context.getContext().set("task_id", todo.getId());
	}

	@When("^I update the task description to \"([^\"]*)\"$")
	public void i_update_the_task_description_to(String new_description) throws Exception {
		int task_id = Context.getContext().get("task_id");
		HelperFunctions.updateTodoDescription(task_id, new_description);
	}

	@Then("^the task has description \"([^\"]*)\"$")
	public void the_task_has_description(String resulting_description) throws Exception {
		int task_id = Context.getContext().get("task_id");

		Todo t = HelperFunctions.getTodoFromTodoId(task_id);

		assertEquals(resulting_description, t.getDescription());
	}

	@Given("^a task with description \"([^\"]*)\"$")
	public void a_task_with_description_and_id(String current_description) throws Exception {
		Todo todo = HelperFunctions.createTodo("TestStory10", false, current_description);
		Context.getContext().set("task_id", todo.getId());
	}

	@When("^I update the task \"([^\"]*)\" with description \"([^\"]*)\"$")
	public void i_update_the_task_with_description(String non_existent_task_id, String other_task_description) throws Exception {
		int statusCode = HelperFunctions.updateTodoDescriptionWithNonExistentTaskId(Integer.parseInt(non_existent_task_id), other_task_description);
		Context.getContext().set("status_code", statusCode);
	}

	/*---------------*/



}