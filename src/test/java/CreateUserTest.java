import pojos.User;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

public class CreateUserTest {
    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    @DisplayName("Check response for correct user creation")
    public void createNewCorrectUserTest() {
        UserClient userClient = new UserClient();
        Response response = userClient.getCorrectUserCreationResponse(new User("test-email" + ThreadLocalRandom.current().nextInt(0, 9999999) + "@yandex.ru", "1234", "saske"));
        assertEquals("Тело ответа: " + response.asString(), 200, response.statusCode());
        assertTrue(response.path("success"));
    }
    @Test
    @DisplayName("Checking the response when a user is recreated")
    public void createTwoEqualUsersTest() {
        UserClient userClient = new UserClient();
        User user = new User("test-email" + ThreadLocalRandom.current().nextInt(0, 9999999) + "@yandex.ru", "1234", "saske");
        Response response = userClient.getTwoEqualsUsersCreationResponse(user);

        assertEquals("Тело ответа: " + response.asString(), 403, response.statusCode());
        assertFalse(response.path("success"));
    }
    @Test
    @DisplayName("Check response for no email user creation")
    public void createUserWithoutEmailTest() {
        UserClient userClient = new UserClient();
        User user = new User("", "1234", "saske");
        Response response = userClient.getUserWitoutEmailCreationResponse(user);

        assertEquals("Тело ответа: " + response.asString(), 403, response.statusCode());
        assertFalse(response.path("success"));
    }
    @Test
    @DisplayName("Check response for no password user creation")
    public void createUserWithoutPasswordTest() {
        UserClient userClient = new UserClient();
        User user = new User("test-email" + ThreadLocalRandom.current().nextInt(0, 9999999) + "@yandex.ru", "", "saske");
        Response response = userClient.getUserWitoutPasswordCreationResponse(user);

        assertEquals("Тело ответа: " + response.asString(), 403, response.statusCode());
        assertFalse(response.path("success"));
    }
    @Test
    @DisplayName("Check response for no name user creation")
    public void createUserWithoutNameTest() {
        UserClient userClient = new UserClient();
        User user = new User("test-email" + ThreadLocalRandom.current().nextInt(0, 9999999) + "@yandex.ru", "1234", "");
        Response response = userClient.getUserWitoutNameCreationResponse(user);

        assertEquals("Тело ответа: " + response.asString(), 403, response.statusCode());
        assertFalse(response.path("success"));
    }
}
