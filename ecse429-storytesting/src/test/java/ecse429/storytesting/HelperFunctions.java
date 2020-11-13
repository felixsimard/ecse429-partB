package ecse429.storytesting;

import ecse429.storytesting.Model.Category;
import ecse429.storytesting.Model.Project;
import ecse429.storytesting.Model.Todo;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.*;
import org.json.simple.JSONObject;

import io.restassured.RestAssured;
import io.restassured.response.*;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;


public class HelperFunctions {

    private static final int STATUS_CODE_CREATED = 201;
    private static Gson gson = new Gson();
    private static Process process;

    public HelperFunctions() {
        RestAssured.baseURI = "http://localhost:4567/";
    }

    //--------APPLICATION----------//

    public static Process startApplication() {
        Runtime rt = Runtime.getRuntime();
        try {
            process = rt.exec("java -jar runTodoManagerRestAPI-1.5.5.jar");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return process;
    }

    public static void stopApplication() {
        if(process != null) process.destroy();
    }

    public static void restoreInitialState() {
        Context.getContext();
    }

    //---------PROJECTS------------//


    public static Project createProject(String title, String completed, String active, String description) {
        RequestSpecification request = RestAssured.given().baseUri("http://localhost:4567");

        JSONObject requestParams = new JSONObject();
        if(!title.equals("")) requestParams.put("title", title);
        if(!completed.equals("")) requestParams.put("completed", completed);
        if(!active.equals("")) requestParams.put("active", active);
        if(!description.equals("")) requestParams.put("description", description);

        request.body(requestParams.toJSONString());

        Response response = request.post("/projects");

        Context.getContext().set("status_code", response.getStatusCode());

        Project result = gson.fromJson(response.asString(), Project.class);
        return result;
    }

    public void deleteProject(int projectId) {
        RequestSpecification request = RestAssured.given().baseUri("http://localhost:4567");

        request.delete("/projects/" + projectId);
    }

    public static void addTodoToProject(int todoId, int projectId) {
        RequestSpecification request = RestAssured.given().baseUri("http://localhost:4567");;

        JSONObject requestParams = new JSONObject();
        requestParams.put("id", "" + todoId);

        request.body(requestParams.toJSONString());

        Response response = request.post("/projects/" + projectId + "/tasks");
    }

    public static int deleteTodoFromProject(int todoId, int projectId) {
        RequestSpecification request = RestAssured.given().baseUri("http://localhost:4567");

        Response response = request.delete("/projects/" + projectId + "/tasks/" + todoId);

        return response.getStatusCode();

    }

    //---------CATEGORIES------------//


    public static Category createCategory(String title, String description)
    {
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", title);
        requestBody.put("description", description);

        RequestSpecification request = given()
                .body(requestBody.toJSONString())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .baseUri("http://localhost:4567");

        Response response = request.post("/categories");

        response.then()
                .assertThat().statusCode(STATUS_CODE_CREATED)
                .assertThat().body(containsString("id"))
                .assertThat().body(containsString(title))
                .assertThat().body(containsString(description));

        //return response.jsonPath().getString("id");
        Category c = gson.fromJson(response.asString(), Category.class);
        return c;
    }

    public static void deleteCategory(int categoryId) {
        RequestSpecification request = RestAssured.given().baseUri("http://localhost:4567");

        request.delete("/categories/" + categoryId);
    }

    public static int linkTodoAndCategory(int todoId, int categoryId) {

        // add the category to the todo
        RequestSpecification requestPost = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("id", String.valueOf(categoryId));

        requestPost.body(requestParams.toJSONString())
                .baseUri("http://localhost:4567");


        Response r = requestPost.post("/todos/" + todoId + "/categories");
        return r.getStatusCode();
    }

    public static Category getCategoryFromTodoId(int todoId) {

        RequestSpecification request = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .baseUri("http://localhost:4567");

        Response response = request.get("/todos/" + todoId + "/categories");

        JsonObject json = new JsonParser().parse(response.asString()).getAsJsonObject();
        JsonArray arr = json.getAsJsonArray("categories");
        Category c = gson.fromJson(arr.get(0).getAsJsonObject(), Category.class);
        return c;
    }

    //---------TODOS------------//

    public static Todo createTodo(String title, boolean doneStatus, String description) {
        RequestSpecification request = RestAssured.given().baseUri("http://localhost:4567");

        JSONObject requestParams = new JSONObject();
        requestParams.put("title", title);
        requestParams.put("doneStatus", doneStatus);
        requestParams.put("description", description);

        request.body(requestParams.toJSONString());

        Response response = request.post("/todos");

        Todo result = gson.fromJson(response.asString(), Todo.class);
        return result;
    }

    public static void deleteTodo(int todoId) {
        RequestSpecification request = RestAssured.given().baseUri("http://localhost:4567");

        request.delete("/todos/" + todoId);
    }
    
    public static Todo getTodoFromTodoId(int todoId) {
    	
    	RequestSpecification request = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .baseUri("http://localhost:4567");

        Response response = request.get("/todos/" + todoId);
        
        Todo todo = gson.fromJson(response.asString(), Todo.class);
        
        return todo;
    	
    }
    
    public static Todo updateTodoDescription(int todoId, String new_description) {
    	
    	RequestSpecification request = RestAssured.given();
    	
    	JSONObject requestParams = new JSONObject();
    	requestParams.put("description", new_description);
    	
    	request.body(requestParams.toJSONString())
    	.baseUri("http://localhost:4567");
    	
    	Response response = request.put("/todos/"+todoId);
    	
    	Todo result = gson.fromJson(response.asString(), Todo.class);
    	
    	return result;	
    }



}
