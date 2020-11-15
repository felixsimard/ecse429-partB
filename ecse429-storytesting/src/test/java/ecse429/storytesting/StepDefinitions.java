package ecse429.storytesting;

//import com.sun.org.apache.xpath.internal.operations.Bool;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ecse429.storytesting.model.Category;
import ecse429.storytesting.model.Id;
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

    // -----------STORY02------------//

    @When("^I add the task \"([^\"]*)\" to the task list of the course \"([^\"]*)\"$")
    public void i_add_the_task_to_the_task_list_of_the_course(String taskTitle, String courseTitle) throws Exception {
        int status_code = HelperFunctions.addTodoToProject(Context.getContext().get(taskTitle), Context.getContext().get(courseTitle));
        Context.getContext().set("status_code", status_code, ContextElement.ElementType.OTHER);
    }

    @Then("^the task \"([^\"]*)\" is in the task list of the course \"([^\"]*)\"$")
    public void the_task_is_in_the_task_list_of_the_course(String taskTitle, String courseTitle) throws Exception {
        int courseTodoListId = Context.getContext().get(courseTitle);
        int todoId = Context.getContext().get(taskTitle);

        Project courseTodoList = HelperFunctions.getProjectByProjectId(courseTodoListId);

        assertNotNull(courseTodoList.getTasks());
        assertEquals(1, courseTodoList.getTasks().stream().filter(id -> id.getId() == todoId).count());
    }

    @Then("^the task \"([^\"]*)\" is not in the task list of the course \"([^\"]*)\"$")
    public void the_task_is_not_in_the_task_list_of_the_course(String taskTitle, String courseTitle) throws Exception {
        int courseTodoListId = Context.getContext().get(courseTitle);

        Project courseTodoList = HelperFunctions.getProjectByProjectId(courseTodoListId);

        assertNull(courseTodoList.getTasks());
    }

    @When("^I add \"([^\"]*)\" to the the tasksof list of \"([^\"]*)\"$")
    public void i_add_to_the_the_tasksof_list_of(String taskTitle, String courseTitle) throws Exception {
        int courseId = Context.getContext().get(courseTitle);
        int todoId = Context.getContext().get(taskTitle);

        HelperFunctions.addProjectToTodoTasksOf(todoId, courseId);
    }

    @Given("^I delete the task with title \"([^\"]*)\"$")
    public void i_delete_the_task_with_title(String taskTitle) throws Exception{
        int todoId = Context.getContext().get(taskTitle);

        HelperFunctions.deleteTodo(todoId);
    }

    // -----------STORY03------------//

    @When("^I mark the task \"([^\"]*)\" to done$")
    public void i_mark_the_task_to_done(String task_title) throws Exception {
        int todoId = Context.getContext().get(task_title);

        int status_code = HelperFunctions.updateTodoDoneStatus(todoId, true);
        Context.getContext().set("status_code", status_code, ContextElement.ElementType.OTHER);
    }

    @Then("^the task \"([^\"]*)\" in the task list of the course \"([^\"]*)\" is marked as done$")
    public void the_task_in_the_task_list_of_the_course_is_marked_as_done(String taskTitle, String courseTitle) throws Exception {
        int courseTodoListId = Context.getContext().get(courseTitle);
        int todoId = Context.getContext().get(taskTitle);

        Project courseTodoList = HelperFunctions.getProjectByProjectId(courseTodoListId);

        assertNotNull(courseTodoList.getTasks());
        assertEquals(1, courseTodoList.getTasks().stream().filter(id -> id.getId() == todoId).count());

        Todo todo = HelperFunctions.getTodoFromTodoId(todoId);
        assertTrue(todo.getDoneStatus());
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
		HelperFunctions.deleteProjectFromTodo(Context.getContext().get(taskTitle), Context.getContext().get(courseTitle));
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
        expectedErrorMessage = expectedErrorMessage.replace("taskId", Context.getContext().get(taskTitle) + "");

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
        Project createdCourseTodoList = HelperFunctions.getProjectByProjectId(Context.getContext().get(title));
        assertNotEquals(createdCourseTodoList, null);
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

    /*--- Story7 ---*/

    /*--- Normal and Alternate Flows ---*/

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

    /**
     * This method asserts that that the query for all incomplete tasks returns all the tasks that it should.
     */
    @Then("^a set is returned which is identical to the initial set of tasks with value false for doneStatus and has \"([^\"]*)\" elements\\.$")
    public void aSetIsReturnedWhichIsIdenticalToTheInitialSetOfTasksWithValueFalseForDoneStatusAndHasElements(String arg0) {
        List<String> listFromQuery = Context.getContext().getListVariables("story7_queryList");
        List<String> actualIncompleteTasks = Context.getContext().getListVariables("story7_falseTasks");
        assertArrayEquals(listFromQuery.toArray(), actualIncompleteTasks.toArray());
    }

    /*--------Error Flow --------*/

    @Given("^a non existent project title \"([^\"]*)\"$")
    public void aNonExistentProjectTitle(String arg0)  {
        Context.getContext().set("error_queryList", -1,ContextElement.ElementType.PROJECT);
    }

    @When("^I query the incomplete tasks for this non existent project$")
    public void iQueryTheIncompleteTasksForThisNonExistentProject() {
        int projectID = Context.getContext().get("error_queryList");
        List<String> list = HelperFunctions.getAllIncompleteTasksOfProject(projectID);
        Context.getContext().setListVariables("error_queryList", list,ContextElement.ElementType.TODO);
    }

    /**
     * This test makes sure that the variable in the context "story7__error_queryList" representing the return
     * value from the previous query is empty.
     *
     * Note that we are expecting it to fail and the fail catch in the helper function then returns an empty list.
     *
     * Shared with story 7 and 8
     */
    @Then("^an error message is returned$")
    public void anErrorMessageIsReturned() {
        List listFromQuery = Context.getContext().getListVariables("error_queryList");
        List emptyList = new ArrayList<>();
        assertArrayEquals(listFromQuery.toArray(), emptyList.toArray());
    }
    /*---------------*/

    /*--- Story8 ---*/
    /*--- Note that some of step definitions for story 8 are reused from story 7 and story 1 ---*/

    @Given("^\"([^\"]*)\" projects exists$")
    public void projectsExists(String arg0) throws Throwable {
        Project newProject = HelperFunctions.createProject("story8_project","","","");
        Context.getContext().set("story8_project", newProject.getId(),ContextElement.ElementType.PROJECT);
    }

    /**
     * This method will create a category "HIGH" and store it in the Context variables map.
     *
     */
    @And("^a category exists with the title HIGH$")
    public void aCategoryExistsWithTheTitleHIGH() {
        Category newCategory = HelperFunctions.createCategory("HIGH", "For high priority tasks");
        Context.getContext().set("high_category", newCategory.getId(),ContextElement.ElementType.CATEGORY);
    }

    @And("^an initial set of tasks connected to any of the projects with \"([^\"]*)\" tasks that have false as doneStatus value, and are connected to the HIGH category$")
    public void anInitialSetOfTasksConnectedToAnyOfTheProjectsWithTasksThatHaveFalseAsDoneStatusValueAndAreConnectedToTheHIGHCategory(String arg0) throws Throwable {
        int projectID = Context.getContext().get("story8_project");
        int categoryId = Context.getContext().get("high_category");
        int numberOfTasks = Integer.parseInt(arg0);
        List<String> list = new ArrayList<String>();

        while(numberOfTasks > 0){
            Todo task = HelperFunctions.createTodo(String.format("Task %d that is not done - for story8", numberOfTasks),false,"");
            list.add((String.valueOf(task.getId())));
            HelperFunctions.addTodoToProject(task.getId(), projectID);
            HelperFunctions.linkCategoryAndTodo(task.getId(),categoryId);
            numberOfTasks--;
        }
        Collections.sort(list);
        Context.getContext().setListVariables("story8_highPriority", list, ContextElement.ElementType.TODO);
    }

    @And("^another set of tasks connected to any of the projects with \"([^\"]*)\" tasks that are not connected to the HIGH category$")
    public void anotherSetOfTasksConnectedToAnyOfTheProjectsWithTasksThatAreNotConnectedToTheHIGHCategory(String arg0) throws Throwable {
        int projectID = Context.getContext().get("story8_project");
        int numberOfTasks = Integer.parseInt(arg0);
        List<String> list = new ArrayList<String>();

        while(numberOfTasks > 0){
            Todo task = HelperFunctions.createTodo(String.format("Task %d that is not done - for story8", numberOfTasks),false,"");
            list.add((String.valueOf(task.getId())));
            HelperFunctions.addTodoToProject(task.getId(), projectID);
            numberOfTasks--;
        }
        Collections.sort(list);
        Context.getContext().setListVariables("story8_noPriority", list, ContextElement.ElementType.TODO);
    }

    @When("^I query the incomplete tasks with category HIGH priority and doneStatus=false$")
    public void iQueryTheIncompleteTasksWithCategoryHIGHPriorityAndDoneStatusFalse() {
        int categoryId = Context.getContext().get("high_category");
        List<Integer> list = HelperFunctions.getAllIncompleteTasksOfProjectWithHighPriority(categoryId);
        Context.getContext().setListVariables("story8_queryList", list,ContextElement.ElementType.TODO);
        Context.getContext().setListVariables("error_queryList", list,ContextElement.ElementType.TODO);
    }

    @Then("^a set is returned that is identical to the initial tasks with doneStatus=false and are connected to the HIGH priority and has \"([^\"]*)\" elements\\.$")
    public void aSetIsReturnedThatIsIdenticalToTheInitialTasksWithDoneStatusFalseAndAreConnectedToTheHIGHPriorityAndHasElements(String arg0) throws Throwable {
        List listFromQuery = Context.getContext().getListVariables("story8_queryList");
        List actualHighPriorityTasks = Context.getContext().getListVariables("story8_highPriority");
        assertArrayEquals(listFromQuery.toArray(),actualHighPriorityTasks.toArray());
    }

    /*-------Error Flow--------*/
    @And("^a non existent category HIGH$")
    public void aNonExistentCategoryHIGH() {
        Context.getContext().set("high_category", -1,ContextElement.ElementType.CATEGORY);
    }

    @And("^an initial set of tasks connected to any of the projects$")
    public void anInitialSetOfTasksConnectedToAnyOfTheProjectsWith() {
        int projectID = Context.getContext().get("story8_project");
       // int categoryId = Context.getContext().get("high_category");
        int numberOfTasks = 3;
        List<String> list = new ArrayList<String>();

        while(numberOfTasks > 0){
            Todo task = HelperFunctions.createTodo(String.format("Task %d that is not done - for story8", numberOfTasks),false,"");
            list.add((String.valueOf(task.getId())));
            HelperFunctions.addTodoToProject(task.getId(), projectID);
           // HelperFunctions.linkCategoryAndTodo(task.getId(),categoryId);
            numberOfTasks--;
        }
        Collections.sort(list);
        Context.getContext().setListVariables("story8_highPriority", list, ContextElement.ElementType.TODO);
    }
    /*---------------*/

    /*--- Story9 ---*/

    @Given("^a task with a priority \"([^\"]*)\"$")
    public void aTaskWithAPriority(String oldPriority) throws Throwable {
        Todo newTask = HelperFunctions.createTodo("story9_todo",false,"");
        Category newCategory = HelperFunctions.createCategory("story9_oldPriority","");
        HelperFunctions.linkTodoAndCategory(newTask.getId(),newCategory.getId());
        Context.getContext().set("story9_todo", newTask.getId(), ContextElement.ElementType.TODO);
        Context.getContext().set("story9_oldPriority", newCategory.getId(), ContextElement.ElementType.TODO);
    }

    @And("^a category called \"([^\"]*)\"$")
    public void aCategoryCalled(String newPriority) throws Throwable {
        Category newCategory = HelperFunctions.createCategory(newPriority,"");
        Context.getContext().set(newPriority, newCategory.getId(), ContextElement.ElementType.CATEGORY);
    }

    @When("^I adjust the priority of the task to \"([^\"]*)\" and remove \"([^\"]*)\"$")
    public void iAdjustThePriorityOfTheTaskToAndRemove(String newPriority, String oldPriority) throws Throwable {
        int newCategoryId = Context.getContext().get(newPriority);
        int oldCategoryId = Context.getContext().get("story9_oldPriority");
        int todoId = Context.getContext().get("story9_todo");
        HelperFunctions.linkTodoAndCategory(todoId, newCategoryId);
        HelperFunctions.removeTodoAndCategoryLink(todoId, oldCategoryId);
    }

    @Then("^the task is categorized with the \"([^\"]*)\" and the old link to \"([^\"]*)\" is removed$")
    public void theTaskIsCategorizedWithTheAndTheOldLinkToIsRemoved(String newPriority, String oldPriority) throws Throwable {
        int todoId = Context.getContext().get("story9_todo");
        int newCategoryId = Context.getContext().get(newPriority);
        Category queryCategoryNew = HelperFunctions.getCategoryFromTodoId(todoId, newPriority);
        Category queryCategoryOld = HelperFunctions.getCategoryFromTodoId(todoId, "story9_oldPriority");

        assertEquals(newCategoryId,queryCategoryNew.getId());
        assertEquals(null, queryCategoryOld);
    }

    /*-------Error Flow--------*/
    @Given("^a non existing task with a priority \"([^\"]*)\"$")
    public void aNonExistingTaskWithAPriority(String arg0) throws Throwable {
        Context.getContext().set("story9_errorTask", -1, ContextElement.ElementType.TODO);
    }

    @When("^I adjust the priority of the task to \"([^\"]*)\"$")
    public void iAdjustThePriorityOfTheTaskTo(String newPriority) throws Throwable {
        int newCategoryId = Context.getContext().get(newPriority);
        int todoId = Context.getContext().get("story9_errorTask");
        int statusCode = HelperFunctions.linkTodoAndCategory(todoId, newCategoryId);
        Context.getContext().set("story9_error_response", statusCode, ContextElement.ElementType.OTHER);
    }

    @Then("^I receive an error message$")
    public void iReceiveAnErrorMessage() {
        int statusCode = Context.getContext().get("story9_error_response");
        assertEquals(404,statusCode);
    }
    /*---------------*/

    /*--- Story10 ---*/

    @Given("^a task with description \"([^\"]*)\"$")
    public void a_task_with_description_and_id(String current_description) throws Exception {
        Todo todo = HelperFunctions.createTodo("TestStory10", false, current_description);
        Context.getContext().set("TestStory10", todo.getId(), ContextElement.ElementType.TODO);
    }

    @When("^I update the task description to \"([^\"]*)\"$")
    public void i_update_the_task_description_to(String new_description) throws Exception {
        int task_id = Context.getContext().get("TestStory10");
        HelperFunctions.updateTodoDescription(task_id, new_description,"TestStory10");
    }

    @Then("^the task has description \"([^\"]*)\"$")
    public void the_task_has_description(String resulting_description) throws Exception {
        int task_id = Context.getContext().get("TestStory10");
        Todo t = HelperFunctions.getTodoFromTodoId(task_id);
        assertEquals(resulting_description, t.getDescription());
    }

    @When("^I update the task \"([^\"]*)\" with description \"([^\"]*)\"$")
    public void i_update_the_task_with_description(String non_existent_task_id, String other_task_description) throws Exception {
        int task_id = Integer.parseInt(non_existent_task_id);
        try{
            HelperFunctions.updateTodoDescription(task_id, other_task_description,"TestStory10");
            Context.getContext().set("Story10Error", 0, ContextElement.ElementType.OTHER);
        }catch (Exception e){
            Context.getContext().set("Story10Error", 1, ContextElement.ElementType.OTHER);
        }
    }

    /*---------------*/


}