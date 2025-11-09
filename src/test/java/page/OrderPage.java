package page;

import constants.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.OrderRequest;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderPage {

    @Step("Получение списка ингредиентов")
    public List<String> getIngredients() {
        return given()
                .get(Endpoints.INGREDIENTS)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data._id");
    }

    @Step("Создание заказа с ингредиентами")
    public Response createOrder(List<String> ingredients, String authToken) {
        OrderRequest orderRequest = new OrderRequest(ingredients);

        return given()
                .header("Content-type", "application/json")
                .header("Authorization", authToken)
                .body(orderRequest)
                .when()
                .post(Endpoints.ORDERS);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(List<String> ingredients) {
        OrderRequest orderRequest = new OrderRequest(ingredients);

        return given()
                .header("Content-type", "application/json")
                .body(orderRequest)
                .when()
                .post(Endpoints.ORDERS);
    }

    @Step("Создание заказа без ингредиентов")
    public Response createOrderWithoutIngredients() {
        return given()
                .header("Content-type", "application/json")
                .body(new OrderRequest(null))
                .when()
                .post(Endpoints.ORDERS);
    }

    @Step("Создание заказа с неверным хешем ингредиента")
    public Response createOrderWithInvalidIngredient() {
        OrderRequest orderRequest = new OrderRequest(List.of("wrong_hash"));

        return given()
                .header("Content-type", "application/json")
                .body(orderRequest)
                .when()
                .post(Endpoints.ORDERS);
    }
}