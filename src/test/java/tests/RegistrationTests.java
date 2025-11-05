package tests;

import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.apache.http.HttpStatus;
import org.junit.Test;
import page.AuthPage;

import static org.hamcrest.Matchers.equalTo;

public class RegistrationTests extends BaseTest {
    public static final String REQUIRED_FIELDS_MESSAGE = "Email, password and name are required fields";
    public static final String USER_ALREADY_EXISTS_MESSAGE = "User already exists";
    private AuthPage authPage = new AuthPage();

    @Test
    @DisplayName("Success registration")
    public void registerSuccess() {
        User newUser = new User(
                "test" + System.currentTimeMillis() + "@example.com",
                "password123",
                "Test User"
        );

        authPage.register(newUser)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
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
    @DisplayName("Register with same email")
    public void registerWithExistingEmail() {
        authPage.register(testUser)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo(USER_ALREADY_EXISTS_MESSAGE));
    }
}