package client;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.http.Header;
import io.restassured.response.Response;
import pojos.Ingredients;
import pojos.User;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private final Gson gson = new Gson();

    @Step("Get all ingredients response")
    public Response getAllIngredientsResponse(){
        return given()
                .get("/api/ingredients");
    }

    @Step("Create order with no auth")
    public Response getCreationOrderResponseWithNoAuth(Ingredients body){
        return  given()
                .header("Content-type", "application/json")
                .body( gson.toJson(body))
                .post("/api/orders");
    }

    @Step("Create order with auth")
    public Response getBasicCreateOrderResponseWithAuth(Ingredients body, String accessToken) {
        return  given()
                .header(new Header("Authorization", accessToken))
                .header("Content-type", "application/json")
                .body( gson.toJson(body))
                .post("/api/orders");
    }

    @Step("Get order from specific user with auth")
    public Response getBasicGetOrderResponseWithAuth(User body, String accessToken) {
        return  given()
                .header(new Header("Authorization", accessToken))
                .header("Content-type", "application/json")
                .body( gson.toJson(body))
                .get("/api/orders");
    }
    @Step("Get order from specific user with no auth")
    public Response getBasicGetOrderResponseWithNoAuth(User body) {
        return  given()
                .header("Content-type", "application/json")
                .body( gson.toJson(body))
                .get("/api/orders");
    }
}
