package tests;

import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.Assume;
import page.OrderPage;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class OrderCreationTests extends BaseTest {
    private final OrderPage orderPage = new OrderPage();

    @Test
    @DisplayName("Create authorized order with ingredients")
    public void createOrder() {
        List<String> ingredients = getAvailableIngredients();

        orderPage.createOrder(ingredients, authToken)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Create unauthorized order")
    public void createOrderWithoutAuth() {
        List<String> ingredients = orderPage.getIngredients();
        Assume.assumeFalse("No ingredients", ingredients.isEmpty());

        orderPage.createOrderWithoutAuth(ingredients)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Create order without ingredients")
    public void createOrderWithoutIngredients() {
        orderPage.createOrderWithoutIngredients()
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Create order with wrong ingredient hash")
    public void createOrderWithInvalidIngredient() {
        orderPage.createOrderWithInvalidIngredient()
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}