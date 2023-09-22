import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import pojos.User;

import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class PatchUserTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    @DisplayName("Update user name with auth")
    public void patchUserNameWithAuth() {
        UserClient userClient = new UserClient();
        User user = new User("test-email" + ThreadLocalRandom.current().nextInt(0, 9999999) + "@yandex.ru", "1234", "saske");
        Response response = userClient.getCorrectUserCreationResponse(user);

        response.then().statusCode(200);
        response.then().body("success", equalTo(true));

        String accessToken = response.getBody().path("accessToken");

        user.setName("New name");
        response = userClient.getPatchUserResponse(user, accessToken);
        assertEquals("Тело ответа: " + response.asString(), 200, response.statusCode());
        assertTrue(response.body().path("success"));
    }

    @Test
    @DisplayName("Update user email with auth")
    public void patchUserEmailWithAuth() {
        UserClient userClient = new UserClient();
        User user = new User("test-email" + ThreadLocalRandom.current().nextInt(0, 9999999) + "@yandex.ru", "1234", "saske");
        Response response = userClient.getCorrectUserCreationResponse(user);

        response.then().statusCode(200);
        response.then().body("success", equalTo(true));

        String accessToken = response.getBody().path("accessToken");

        user.setEmail("new-email" + ThreadLocalRandom.current().nextInt(0, 9999999) + "@yandex.ru");
        response = userClient.getPatchUserResponse(user, accessToken);
        assertEquals("Тело ответа: " + response.asString(), 200, response.statusCode());
        assertTrue(response.body().path("success"));
    }

    @Test
    @DisplayName("Update user password with auth")
    public void patchUserPasswordWithAuth() {
        UserClient userClient = new UserClient();
        User user = new User("test-email" + ThreadLocalRandom.current().nextInt(0, 9999999) + "@yandex.ru", "1234", "saske");
        Response response = userClient.getCorrectUserCreationResponse(user);

        response.then().statusCode(200);
        response.then().body("success", equalTo(true));

        String accessToken = response.getBody().path("accessToken");

        user.setPassword("new_password");
        response = userClient.getPatchUserResponse(user, accessToken);
        assertEquals("Тело ответа: " + response.asString(), 200, response.statusCode());
        assertTrue(response.body().path("success"));
    }

    @Test
    @DisplayName("Update user name without auth")
    public void patchUserNameWithNoAuthTest() {
        UserClient userClient = new UserClient();
        User user = new User("test-email" + ThreadLocalRandom.current().nextInt(0, 9999999) + "@yandex.ru", "1234", "saske");
        Response response = userClient.getCorrectUserCreationResponse(user);

        response.then().statusCode(200);
        response.then().body("success", equalTo(true));

        user.setName("New name");
        response = userClient.getPatchUserResponseWithNoAccessToken(user);
        assertEquals("Тело ответа: " + response.asString(), 401, response.statusCode());
        assertFalse(response.body().path("success"));
    }

    @Test
    @DisplayName("Update user email without auth")
    public void patchUserEmailWithNoAuthTest() {
        UserClient userClient = new UserClient();
        User user = new User("test-email" + ThreadLocalRandom.current().nextInt(0, 9999999) + "@yandex.ru", "1234", "saske");
        Response response = userClient.getCorrectUserCreationResponse(user);

        response.then().statusCode(200);
        response.then().body("success", equalTo(true));

        user.setEmail("new-email" + ThreadLocalRandom.current().nextInt(0, 9999999) + "@yandex.ru");

        response = userClient.getPatchUserResponseWithNoAccessToken(user);
        assertEquals("Тело ответа: " + response.asString(), 401, response.statusCode());
        assertFalse(response.body().path("success"));
    }

    @Test
    @DisplayName("Update user name data without auth")
    public void patchUserPasswordWithNoAuthTest() {
        UserClient userClient = new UserClient();
        User user = new User("test-email" + ThreadLocalRandom.current().nextInt(0, 9999999) + "@yandex.ru", "1234", "saske");
        Response response = userClient.getCorrectUserCreationResponse(user);

        response.then().statusCode(200);
        response.then().body("success", equalTo(true));

        user.setPassword("new_password");
        response = userClient.getPatchUserResponseWithNoAccessToken(user);
        assertEquals("Тело ответа: " + response.asString(), 401, response.statusCode());
        assertFalse(response.body().path("success"));
    }
}
