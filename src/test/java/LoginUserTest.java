import client.UserClient;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import pojos.User;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

public class LoginUserTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    @DisplayName("Check response for correct user login")
    public void loginCorrectUserTest() {
        UserClient userClient = new UserClient();
        User user = getSimpleUserObject();
        userClient.getCorrectUserCreationResponse(user).then().assertThat().statusCode(200);

        Response response = userClient.getUserLoginResponse(user);

        assertEquals("Тело ответа: " + response.asString(), 200, response.statusCode());
        assertTrue(response.path("success"));
    }

    @Test
    @DisplayName("Check response for user login with incorrect email")
    public void loginUserWithIncorrectEmailTest() {
        UserClient userClient = new UserClient();
        User user =getSimpleUserObject();
        userClient.getCorrectUserCreationResponse(user).then().assertThat().statusCode(200);

        user.setEmail(user.getEmail() + "incorrect-email");
        Response response = userClient.getUserLoginResponse(user);

        assertEquals("Тело ответа: " + response.asString(), 401, response.statusCode());
        assertFalse(response.path("success"));
    }

    @Test
    @DisplayName("Check response for user login with incorrect password")
    public void loginUserWithIncorrectPasswordTest() {
        UserClient userClient = new UserClient();
        User user = getSimpleUserObject();
        userClient.getCorrectUserCreationResponse(user).then().assertThat().statusCode(200);

        user.setPassword(user.getPassword() + "incorrect-password");
        Response response = userClient.getUserLoginResponse(user);

        assertEquals("Тело ответа: " + response.asString(), 401, response.statusCode());
        assertFalse(response.path("success"));
    }
    @Step("Get simple User object")
    private User getSimpleUserObject() {
        return new User("test-email" + ThreadLocalRandom.current().nextInt(0, 9999999) + "@yandex.ru", "1234", "name");
    }
}
