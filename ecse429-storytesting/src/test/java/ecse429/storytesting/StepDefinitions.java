package ecse429.storytesting;

//import com.sun.org.apache.xpath.internal.operations.Bool;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ecse429.storytesting.model.Category;
import ecse429.storytesting.model.Project;
import ecse429.storytesting.model.Todo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.get;
import static org.junit.Assert.*;

public class StepDefinitions {

    public static String errorMessage;

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
                } catch (Exception e) {
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
        System.out.println("Created todo with id: " + todo.getId());
        Context.getContext().set(todo.getTitle(), todo.getId(), ContextElement.ElementType.TODO);
    }

    @Given("^a category with the title \"([^\"]*)\"$")
    public void a_category_with_the_HIGH(String title) throws Exception {
        Category category = HelperFunctions.createCategory(title, "");
        System.out.println("Created category with id: " + category.getId());
        System.out.println(category.getTitle());
        Context.getContext().set(category.getTitle(), category.getId(), ContextElement.ElementType.CATEGORY);
    }

    @When("^I delete the category with title \"([^\"]*)\"$")
    public void i_delete_the_category_with_title(String title) throws Exception {
        System.out.println(title);
        int category_id = Context.getContext().get(title);

        HelperFunctions.deleteCategory(category_id);
    }

    @When("^I link the task \"([^\"]*)\" to the category with title \"([^\"]*)\"$")
    public void i_link_the_task_to_the_HIGH(String task_title, String category_title) throws Exception {
        int task_id = Context.getContext().get(task_title);
        int category_id = Context.getContext().get(category_title);

        int statusCode = HelperFunctions.linkTodoAndCategory(task_id, category_id);
        System.out.println("Linked " + task_title + " to " + category_title + " : " + statusCode);
        Context.getContext().set("status_code", statusCode, ContextElement.ElementType.OTHER);
    }

    @Then("^the task \"([^\"]*)\" is categorized with the title \"([^\"]*)\"")
    public void the_task_is_categorized_with_the_HIGH(String task_title, String category_title) throws Exception {
        int task_id = Context.getContext().get(task_title);

        Category c = HelperFunctions.getCategoryFromTodoId(task_id, category_title);

        assertEquals(category_title, c.getTitle());
    }

    // -----------STORY04------------//

    @Given("^a course with title \"([^\"]*)\"$")
    public void a_course_with_title(String courseTitle) throws Exception {
        Project course = HelperFunctions.createProject(courseTitle, "", "", "");
        if (course != null) {
            Context.getContext().set(courseTitle, course.getId(), ContextElement.ElementType.PROJECT);
        }
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

        Context.getContext().set(ass1Map.get("title"), assignment1.getId(), ContextElement.ElementType.TODO);

        Map<String, String> ass2Map = list.get(1);
        assignment2 = HelperFunctions.createTodo(ass2Map.get("title"), Boolean.parseBoolean(ass2Map.get("doneStatus")),
                ass2Map.get("description"));

        Context.getContext().set(ass2Map.get("title"), assignment2.getId(), ContextElement.ElementType.TODO);

        Map<String, String> hw1Map = list.get(2);
        homework1 = HelperFunctions.createTodo(hw1Map.get("title"), Boolean.parseBoolean(hw1Map.get("doneStatus")),
                hw1Map.get("description"));

        Context.getContext().set(hw1Map.get("title"), homework1.getId(), ContextElement.ElementType.TODO);

    }

    @Given("^\"([^\"]*)\" and \"([^\"]*)\" are added to the course \"([^\"]*)\" todo list$")
    public void and_are_added_to_the_course_todo_list(String ass1, String ass2, String courseTitle) throws Exception {
        HelperFunctions.addTodoToProject(Context.getContext().get(ass1), Context.getContext().get(courseTitle));
        HelperFunctions.addTodoToProject(Context.getContext().get(ass2), Context.getContext().get(courseTitle));
    }

    @When("^I remove \"([^\"]*)\" from the tasks list of \"([^\"]*)\" todo list$")
    public void i_remove_from_the_course_todo_list(String taskTitle, String courseTitle) throws Exception {
        int statusCode = HelperFunctions.deleteTodoFromProject(Context.getContext().get(taskTitle),
                Context.getContext().get(courseTitle));
        Context.getContext().set("status_code", statusCode, ContextElement.ElementType.OTHER);
    }

    @When("^I remove \"([^\"]*)\" from the tasksof list of \"([^\"]*)\"$")
	public void i_remove_from_the_tasksof_list_of(String courseTitle, String taskTitle) {
		int statusCode = HelperFunctions.deleteProjectFromTodo(Context.getContext().get(taskTitle),
				Context.getContext().get(courseTitle));
		Context.getContext().set("status_code", statusCode, ContextElement.ElementType.OTHER);
	}

    @Then("^the relationship between \"([^\"]*)\" and the course \"([^\"]*)\" is destroyed$")
    public void the_relationship_between_and_the_course_is_destroyed(String taskTitle, String courseTitle) throws Exception {
		int courseTodoListId = Context.getContext().get(courseTitle);
		int todoId = Context.getContext().get(taskTitle);

        Project courseTodoList = HelperFunctions.getProjectByProjectId(courseTodoListId);
        Todo todo = HelperFunctions.getTodoFromTodoId(todoId);

        //confirm that the task is no longer linked to the course to do list
        assertEquals(0, courseTodoList.getTasks().stream().filter(id -> id.getId() == todoId).count());
        assertEquals(null, todo.getTasksof());
    }

    @Then("^the error message is \"([^\"]*)\" with \"([^\"]*)\" and \"([^\"]*)\"$")
    public void the_error_message_is_with(String expectedErrorMessageOutline, String taskTitle, String courseTitle) {
        String expectedErrorMessage = expectedErrorMessageOutline.replace("projectId", Context.getContext().get(courseTitle) + "");
        expectedErrorMessage = expectedErrorMessageOutline.replace("taskId", Context.getContext().get(taskTitle) + "");

        assertEquals(expectedErrorMessage, errorMessage);
    }

    //--------STORY05-------//

    @When("^I create a new to do list with title \"([^\"]*)\", completed status \"([^\"]*)\", active status \"([^\"]*)\", and description \"([^\"]*)\"$")
    public void i_create_a_new_to_do_list_with_title_completed_status_active_status_and_description(String title, String completed, String active, String description) throws Exception {
        Project courseTodoList = HelperFunctions.createProject(title, completed, active, description);
        if (courseTodoList != null)
            Context.getContext().set(title, courseTodoList.getId(), ContextElement.ElementType.PROJECT);
    }

    @Then("^the returned status code is \"(\\d+)\"$")
    public void the_returned_status_code_is(int statusCode) throws Exception {
        assertEquals(statusCode, Context.getContext().get("status_code"));
    }

    @Then("^\"([^\"]*)\" is created accordingly$")
    public void is_created_accordingly(String title) throws Exception {
        int statusCode = Context.getContext().get("status_code");
        if (statusCode == 201) {
            Project createdCourseTodoList = HelperFunctions.getProjectByProjectId(Context.getContext().get(title));
            assertNotEquals(createdCourseTodoList, null);
        } else {
            //error flow
            List<Project> courseTodoLists = HelperFunctions.getAllProjects();
            assert (courseTodoLists.stream().filter(course -> course.getTitle().equals(title)).count() == 0);
        }
    }

    //---------STORY06---------//

    @Given("^created course to do lists$")
    public void created_course_to_do_lists(DataTable table) {
        Project course1;
        Project course2;

        List<Map<String, String>> list = table.asMaps(String.class, String.class);
        Map<String, String> course1Map = list.get(0);
        course1 = HelperFunctions.createProject(course1Map.get("title"), course1Map.get("completed"),
                course1Map.get("active"), course1Map.get("description"));

        Map<String, String> course2Map = list.get(1);
        course2 = HelperFunctions.createProject(course2Map.get("title"), course2Map.get("completed"),
                course2Map.get("active"), course2Map.get("description"));

        Context.getContext().set(course1.getTitle(), course1.getId(), ContextElement.ElementType.PROJECT);
        Context.getContext().set(course2.getTitle(), course2.getId(), ContextElement.ElementType.PROJECT);
    }

    @When("^I delete the \"([^\"]*)\" to do list$")
    public void i_delete_the_to_do_list(String courseTitle) {
        int courseId = Context.getContext().get(courseTitle);
        HelperFunctions.deleteProject(courseId);
    }

    @When("^I delete a non existent course with id \"(\\d+)\"$")
    public void i_delete_a_non_existent_course_with_id(int courseId) {
        HelperFunctions.deleteProject(courseId);
    }

    @Then("^the course \"([^\"]*)\" is no longer in the database$")
    public void the_course_is_no_longer_in_the_database(String courseTitle) {
        int courseId = Context.getContext().get(courseTitle);
        List<Project> courses = HelperFunctions.getAllProjects();
        assertEquals(0, courses.stream().filter(course -> course.getId() == courseId).count());
    }

    @Then("^no tasks are linked to \"([^\"]*)\"$")
    public void no_tasks_are_linked_to(String courseTitle) {
        int courseId = Context.getContext().get(courseTitle);
        List<Todo> todos = HelperFunctions.getAllTodos();
        Stream<Todo> todosWithProjects = todos.stream().filter(todo -> todo.getTasksof() != null);
        assertEquals(0, todosWithProjects.filter(todo -> todo.getTasksof().contains(courseId)).count());
    }

    @Then("^the returned error message is \"([^\"]*)\"$")
    public void the_returned_error_message_is(String expectedErrorMessage) {
        assertEquals(expectedErrorMessage, errorMessage);
    }



    /*--- Story7 - and - Story8 ---*/
    /**
     * This method will create a project with title equal to the input and store it in the Context variables map.
     *
     * @param projectTitle - The title of the project to be created.
     */
    @Given("^a project with title \"([^\"]*)\"$")
    public void aProjectWithTitle(String projectTitle) {
        Project newProject = HelperFunctions.createProject(projectTitle,"","","");
        Context.getContext().set("story7_project", newProject.getId(),ContextElement.ElementType.PROJECT);
    }

    /*--- Story7 ---*/

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
        Context.getContext().setListVariables("story7_falseTasks", list, ContextElement.ElementType.TODO);
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
        Context.getContext().setListVariables("story7_trueTasks", list, ContextElement.ElementType.TODO);
    }

    /**
     * This method queries the number of tasks that are incomplete from the project with title="story7_project" and stores all ids of task
     * in a list in the context
     *
     */
    @When("^I query the incomplete tasks for this project$")
    public void iQueryTheIncompleteTasksForThisProject() {
        int projectID = Context.getContext().get("story7_project");
        List<String> list = HelperFunctions.getAllIncompleteTasksOfProject(projectID);
        Context.getContext().setListVariables("story7_queryList", list, ContextElement.ElementType.TODO);
    }

    @Then("^a set is returned which is identical to the initial set of tasks with value false for doneStatus and has \"([^\"]*)\" elements\\.$")
    public void aSetIsReturnedWhichIsIdenticalToTheInitialSetOfTasksWithValueFalseForDoneStatusAndHasElements(String arg0) {
        List<String> listFromQuery = Context.getContext().getListVariables("story7_queryList");
        List<String> actualIncompleteTasks = Context.getContext().getListVariables("story7_falseTasks");
        assertArrayEquals(listFromQuery.toArray(), actualIncompleteTasks.toArray());
    }

    /*--------Error Flow--------*/

    @Given("^a non existent project title \"([^\"]*)\"$")
    public void aNonExistentProjectTitle(String arg0)  {
        // nothing to do here
    }

    @Then("^an error message is returned$")
    public void anErrorMessageIsReturned() {
    }

    /*---------------*/

    /*---------------*/

    /*--- Story8 ---*/
    /*--- Note that some of step definitions for story 8 are reused from story 7 and story 1 ---*/

//	/**
//	 * Will create 2 false tasks with doneStatus=false and connect them to the category of the input string
//	 * @param priority
//	 */
//	@And("^an initial set of tasks connected to the project that have false as doneStatus value, and are connected to the \"([^\"]*)\"$")
//	public void anInitialSetOfTasksConnectedToTheProjectThatHaveFalseAsDoneStatusValueAndAreConnectedToThe(String priority) throws Throwable {
//		int projectID = Context.getContext().get("story7_project");
//		List<String> list = new ArrayList<String>();
//		// create the tasks
//		Todo task1 = HelperFunctions.createTodo("Task1 story8",false,"");
//		Todo task2 = HelperFunctions.createTodo("Task2 story8",false,"");
//		// add tasks to list
//		list.add((String.valueOf(task1.getId())));
//		list.add((String.valueOf(task2.getId())));
//		// add tasks to project
//		HelperFunctions.addTodoToProject(task1.getId(), projectID);
//		HelperFunctions.addTodoToProject(task2.getId(), projectID);
//		// add task to category HIGH priority
//		HelperFunctions.linkTodoAndCategory(task1.getId(), )
//
//		Collections.sort(list);
//		Context.getContext().setListVariables("story7_trueTasks", list);
//	}

    @And("^an initial set of tasks connected to the project that have false as doneStatus value, and are not connected to the \"([^\"]*)\"$")
    public void anInitialSetOfTasksConnectedToTheProjectThatHaveFalseAsDoneStatusValueAndAreNotConnectedToThe(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I query the incomplete tasks for this project with category HIGH priority and doneStatus=false$")
    public void iQueryTheIncompleteTasksForThisProjectWithCategoryHIGHPriorityAndDoneStatusFalse() {

    }

    @Then("^a set is returned that is identical to the initial tasks with doneStatus=false and are connected to the \"([^\"]*)\"$")
    public void aSetIsReturnedThatIsIdenticalToTheInitialTasksWithDoneStatusFalseAndAreConnectedToThe(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    /*---------------*/

    /*--- Story9 ---*/
    /*---------------*/

    /*--- Story10 ---*/

//	@Given("^a task with description \"([^\"]*)\"$")
//	public void a_task_with_description(String description) throws Exception {
//		Todo todo = HelperFunctions.createTodo("TestStory10", false, description);
//		ContextElement e = new ContextElement(todo.getId(), ContextElement.ElementType.TODO);
//		Context.getContext().set("task_id", e);
//	}

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
        Context.getContext().set("task_id", todo.getId(), ContextElement.ElementType.TODO);
    }

    @When("^I update the task \"([^\"]*)\" with description \"([^\"]*)\"$")
    public void i_update_the_task_with_description(String non_existent_task_id, String other_task_description) throws Exception {
        int statusCode = HelperFunctions.updateTodoDescriptionWithNonExistentTaskId(Integer.parseInt(non_existent_task_id), other_task_description);
        Context.getContext().set("status_code", statusCode, ContextElement.ElementType.OTHER);
    }

    /*---------------*/


}