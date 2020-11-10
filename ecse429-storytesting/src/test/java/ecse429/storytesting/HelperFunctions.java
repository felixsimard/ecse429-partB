package ecse429.storytesting;

import org.json.simple.JSONObject;

import io.restassured.RestAssured;
import io.restassured.response.*;
import io.restassured.specification.RequestSpecification;


public class HelperFunctions {

    private static final int STATUS_CODE_CREATED = 201;

    public static int createTodo(String title, boolean completed, boolean active, String description) {
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("title", title);
        requestParams.put("completed", completed);
        requestParams.put("active", active);
        requestParams.put("description", description);


        request.body(requestParams.toJSONString());

        Response response = request.post("/todos");

        int todoID = Integer.parseInt((String) response.jsonPath().get("id"));
        return todoID;
    }

}
