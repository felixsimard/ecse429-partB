package ecse429.storytesting;

import ecse429.storytesting.Model.Project;
import ecse429.storytesting.Model.Todo;
import gherkin.deps.com.google.gson.Gson;
import org.json.simple.JSONObject;

import io.restassured.RestAssured;
import io.restassured.response.*;
import io.restassured.specification.RequestSpecification;


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

    public static Todo createTodo(String title, boolean doneStatus, String description) {
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("title", title);
        requestParams.put("doneStatus", doneStatus);
        requestParams.put("description", description);


        request.body(requestParams.toJSONString());

        Response response = request.post("/todos");

        Todo result = gson.fromJson(response.asString(), Todo.class);
        return result;
    }

}
