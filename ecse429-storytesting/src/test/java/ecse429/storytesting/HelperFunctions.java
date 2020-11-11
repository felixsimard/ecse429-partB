package ecse429.storytesting;

import ecse429.storytesting.Model.Category;
import ecse429.storytesting.Model.Project;
import ecse429.storytesting.Model.Todo;
import gherkin.deps.com.google.gson.Gson;
import org.json.simple.JSONObject;

import io.restassured.RestAssured;
import io.restassured.response.*;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;


public class HelperFunctions {

    private static final int STATUS_CODE_CREATED = 201;
    private static Gson gson = new Gson();


    public HelperFunctions() {
        RestAssured.baseURI = "http://localhost:4567/";
    }

    public static Project createProject(String title, boolean completed, boolean active, String description) {
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("title", title);
        requestParams.put("completed", completed);
        requestParams.put("active", active);
        requestParams.put("description", description);


        request.body(requestParams.toJSONString());

        Response response = request.post("/projects");

        Project result = gson.fromJson(response.asString(), Project.class);
        return result;
    }

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

    public static void linkTodoAndCategory(String todoId, String categoryId) {

        // add the category to the todo
        RequestSpecification requestPost = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("id", String.valueOf(categoryId));

        requestPost.body(requestParams.toJSONString())
                .baseUri("http://localhost:4567");


        requestPost
                .post("/todos/" + todoId + "/categories")
                .then()
                .assertThat()
                .statusCode(equalTo(STATUS_CODE_CREATED));

    }

    public static Category getCategoryFromTodoId(String todoId) {

        RequestSpecification request = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .baseUri("http://localhost:4567");

        Response response = request.get("/todos/" + todoId + "/categories");

        Category categoryList = gson.fromJson(response.asString(), Category.class);
        return categoryList;
    }

    public static Todo createTodo(String title, boolean doneStatus, String description) {
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("title", title);
        requestParams.put("doneStatus", doneStatus);
        requestParams.put("description", description);


        request.body(requestParams.toJSONString())
        .baseUri("http://localhost:4567");

        Response response = request.post("/todos");

        Todo result = gson.fromJson(response.asString(), Todo.class);
        return result;
    }

}
