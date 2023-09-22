import client.OrderClient;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import pojos.Ingredients;
import pojos.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateOrderTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    @DisplayName("Check correct order creation with auth")
    public void correctOrderCreationWithAuthTest() {
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

        assertEquals(200, response.statusCode());
        assertTrue(response.path("success"));
    }

    @Test
    @DisplayName("Check order creation without auth")
    public void orderCreationWithNoAuthTest() {
        OrderClient orderClient = new OrderClient();
        Response response = orderClient.getAllIngredientsResponse();
        List<String> ids = response.jsonPath().getList("data._id");


        Ingredients ingredients = new Ingredients(List.of(ids.get(0), ids.get(ids.size() - 1)));
        response = orderClient.getCreationOrderResponseWithNoAuth(ingredients);

        assertEquals(200, response.statusCode());
        assertTrue(response.path("success"));
    }

    @Test
    @DisplayName("Check order creation without auth and with not valid hashes")
    public void orderCreationWithNoAuthAndNotValidHashesTest() {
        OrderClient orderClient = new OrderClient();
        Response response = orderClient.getAllIngredientsResponse();
        List<String> ids = response.jsonPath().getList("data._id");


        Ingredients ingredients = new Ingredients(List.of(ids.get(0) + "not_valid", ids.get(ids.size() - 1) + "not_valid"));
        response = orderClient.getCreationOrderResponseWithNoAuth(ingredients);

        assertEquals(500, response.statusCode());
    }

    @Test
    @DisplayName("Check order creation without auth and without ingredients")
    public void orderCreationWithNoAuthAndWithNoIngredientsTest() {
        OrderClient orderClient = new OrderClient();

        Ingredients ingredients = new Ingredients(new ArrayList<>());
        Response response = orderClient.getCreationOrderResponseWithNoAuth(ingredients);

        assertEquals(400, response.statusCode());
    }
}
