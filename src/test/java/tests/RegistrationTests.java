package tests;

import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import page.AuthPage;

import static org.hamcrest.Matchers.equalTo;

public class RegistrationTests {
    public static final String REQUIRED_FIELDS_MESSAGE = "Email, password and name are required fields";
    public static final String USER_ALREADY_EXISTS_MESSAGE = "User already exists";
    public String authToken;
    private final AuthPage authPage = new AuthPage();

    @After
    public void deleteCreatedUser() {
        if (this.authToken != null) {
            authPage.deleteUser(this.authToken)
                    .then()
                    .statusCode(HttpStatus.SC_ACCEPTED);
            this.authToken = null;
        }
    }

    @Test
    @DisplayName("Success registration")
    public void registerSuccess() {
        User newUser = new User(
                "test" + System.currentTimeMillis() + "@example.com",
                "password123",
                "Test User"
        );

        authToken = authPage.register(newUser)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Register without email")
    public void registerWithoutEmail() {
        User userWithoutEmail = new User(null, "password123", "Test User");

        authPage.register(userWithoutEmail)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo(REQUIRED_FIELDS_MESSAGE));
    }

    @Test
    @DisplayName("Register without password")
    public void registerWithoutPassword() {
        User userWithoutPassword = new User("test@example.com", null, "Test User");

        authPage.register(userWithoutPassword)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo(REQUIRED_FIELDS_MESSAGE));
    }

    @Test
    @DisplayName("Register without name")
    public void registerWithoutName() {
        User userWithoutEmail = new User("test@example.com", "password123", null);

        authPage.register(userWithoutEmail)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo(REQUIRED_FIELDS_MESSAGE));
    }

    @Test
    @DisplayName("Register with same email")
    public void registerWithExistingEmail() {
        User newUser = new User(
                "test" + System.currentTimeMillis() + "@example.com",
                "password123",
                "Test User"
        );

        authToken = authPage.register(newUser)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");

        authPage.register(newUser)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo(USER_ALREADY_EXISTS_MESSAGE));
    }
}