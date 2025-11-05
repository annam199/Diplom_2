package page;

import constants.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;

import static io.restassured.RestAssured.given;

public class AuthPage {

    @Step("Авторизация пользователя")
    public Response login(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(Endpoints.AUTH_LOGIN);
    }

    @Step("Регистрация нового пользователя")
    public Response register(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(Endpoints.AUTH_REGISTER);
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String authToken) {
        return given()
                .header("Authorization", authToken)
                .when()
                .delete(Endpoints.AUTH_USER);
    }
}