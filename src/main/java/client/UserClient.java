package client;

import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.http.Header;
import io.restassured.response.Response;
import pojos.User;

import static io.restassured.RestAssured.given;

public class UserClient {
    private final Gson gson = new Gson();

    @Step("Get response for correct user creation")
    public Response getCorrectUserCreationResponse(User user) {
        return getBasicUserCreationResponse(user);
    }

    @Step("Get response for post request /api/auth/register")
    private Response getBasicUserCreationResponse(Object body){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(gson.toJson(body))
                .when()
                .post("/api/auth/register");
    }


    @Step("Get response for two equal users creation.")
    @Description("First request is checked for 200 status code")
    public Response getTwoEqualsUsersCreationResponse(Object body) {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(gson.toJson(body))
                .when()
                .post("/api/auth/register").then().assertThat().statusCode(200);
        return getBasicUserCreationResponse(body);
    }
    @Step("Get response for create user without password")
    public Response getUserWitoutPasswordCreationResponse(User user) {
        return getBasicUserCreationResponse(user);
    }
    @Step("Get response for create user without email")
    public Response getUserWitoutEmailCreationResponse(User user) {
        return getBasicUserCreationResponse(user);
    }
    @Step("Get response for create user without name")
    public Response getUserWitoutNameCreationResponse(User user) {
        return getBasicUserCreationResponse(user);
    }

    @Step("Get response for user login")
    public Response getUserLoginResponse(User body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(gson.toJson(body))
                .when()
                .post("/api/auth/login");
    }

    @Step("Get response for user data update")
    public Response getPatchUserResponse(User body, String accessToken) {
        return given()
                .header(new Header("Authorization", accessToken))
                .header("Content-type", "application/json")
                .and()
                .body(gson.toJson(body))
                .when()
                .patch("api/auth/user");
    }

    @Step("Get response for user data update without access token")
    public Response getPatchUserResponseWithNoAccessToken(User user) {
        return getPatchUserResponse(user, "");
    }
}
