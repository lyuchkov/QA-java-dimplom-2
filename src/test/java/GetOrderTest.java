import client.OrderClient;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import pojos.Ingredients;
import pojos.User;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class GetOrderTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    @DisplayName("Check order data receiving with auth for specific user")
    public void getOrderDataWithAuthTest() {
        UserClient userClient = new UserClient();
        User user = new User("test-email" + ThreadLocalRandom.current().nextInt(0, 9999999) + "@yandex.ru", "1234", "saske");
        Response response = userClient.getCorrectUserCreationResponse(user);

        response.then().statusCode(200);
        response.then().body("success", equalTo(true));

        String accessToken = response.getBody().path("accessToken");

        OrderClient orderClient = new OrderClient();
        response = orderClient.getAllIngredientsResponse();
        List<String> ids = response.jsonPath().getList("data._id");


        Ingredients ingredients = new Ingredients(List.of(ids.get(0), ids.get(ids.size() - 1)));
        response = orderClient.getBasicCreateOrderResponseWithAuth(ingredients, accessToken);

        response.then().assertThat().statusCode(200);

        response = orderClient.getBasicGetOrderResponseWithAuth(user, accessToken);

        assertEquals(200, response.statusCode());
        assertTrue(response.path("success"));

        List<Object> orders = response.jsonPath().getList("orders");
        assertEquals(1, orders.size());
    }

    @Test
    @DisplayName("Check order data receiving with no auth for specific user")
    public void getOrderDataWithNoAuthTest() {
        UserClient userClient = new UserClient();
        User user = new User("test-email" + ThreadLocalRandom.current().nextInt(0, 9999999) + "@yandex.ru", "1234", "saske");
        Response response = userClient.getCorrectUserCreationResponse(user);

        response.then().statusCode(200);
        response.then().body("success", equalTo(true));

        OrderClient orderClient = new OrderClient();


        response = orderClient.getBasicGetOrderResponseWithNoAuth(user);

        assertEquals(401, response.statusCode());
        assertFalse(response.path("success"));
    }
}
