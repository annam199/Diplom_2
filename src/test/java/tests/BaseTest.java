package tests;

import constants.Endpoints;

import io.restassured.RestAssured;
import model.User;
import net.datafaker.Faker;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import page.AuthPage;
import page.OrderPage;

import java.util.List;

public class BaseTest {
    protected User testUser;
    protected String authToken;
    protected AuthPage authPage = new AuthPage();
    protected OrderPage orderPage = new OrderPage();

    Faker faker = new Faker();

    @Before
    public void setUp() {
        RestAssured.baseURI = Endpoints.BASE_URL;
//        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

//        Создание тестового пользователя
        testUser = new User(
                faker.internet().emailAddress(),
                "password",
                "Test User"
        );

        authToken = authPage.register(testUser)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("accessToken");
    }

    @After
    public void tearDown() {
        if (authToken != null) {
            authPage.deleteUser(authToken)
                    .then()
                    .statusCode(HttpStatus.SC_ACCEPTED);
        }
    }

    protected List<String> getAvailableIngredients() {
        List<String> ingredients = orderPage.getIngredients();
        Assume.assumeFalse("No ingredients", ingredients.isEmpty());
        return ingredients;
    }
}