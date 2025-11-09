package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.apache.http.HttpStatus;
import org.junit.Test;
import page.AuthPage;

import static org.hamcrest.Matchers.equalTo;

public class LoginTests extends BaseTest {
    public static final String EMAIL_OR_PASSWORD_ARE_INCORRECT_MESSAGE = "email or password are incorrect";
    private final AuthPage authPage = new AuthPage();

    @Test
    @DisplayName("Success auth")
    public void loginSuccess() {
        authPage.login(testUser)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Auth with wrong password")
    public void loginWithWrongPassword() {
        User wrongUser = new User(testUser.getEmail(), "wrongpassword", null);

        authPage.login(wrongUser)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo(EMAIL_OR_PASSWORD_ARE_INCORRECT_MESSAGE));
    }

    @Test
    @DisplayName("Auth with wrong email")
    public void loginWithWrongEmail() {
        User wrongUser = new User("wrong@example.com", testUser.getPassword(), null);

        authPage.login(wrongUser)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo(EMAIL_OR_PASSWORD_ARE_INCORRECT_MESSAGE));
    }
}