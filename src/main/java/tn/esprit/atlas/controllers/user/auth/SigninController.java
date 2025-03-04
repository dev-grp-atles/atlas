package tn.esprit.atlas.controllers.user.auth;

import com.google.api.client.auth.oauth2.Credential;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tn.esprit.atlas.controllers.user.UserController;
import tn.esprit.atlas.entities.User;
import tn.esprit.atlas.utils.UserSession;

import java.io.IOException;
import java.util.regex.Pattern;

public class SigninController {

    @FXML
    public Button signInButton;

    @FXML
    private Button goBackButton;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label forgotPasswordLabel;

    private UserController userController = new UserController(); // Instance of UserController

    @FXML
    private void handleGoBack() throws IOException {
        loadScene("/tn/esprit/atlas/views/main-view.fxml");
    }

    @FXML
    private void handleGoToSignUp(MouseEvent event) throws IOException {
        loadScene("/tn/esprit/atlas/views/user/auth/signup-view.fxml");
    }

    private void loadScene(String fxmlPath) throws IOException {
        // Load the FXML file
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));

        // Get the current stage
        Stage stage = (Stage) goBackButton.getScene().getWindow();

        // Preserve window size
        double width = stage.getWidth();
        double height = stage.getHeight();

        // Set new root while keeping size
        stage.getScene().setRoot(root);
        stage.setWidth(width);
        stage.setHeight(height);
    }

    public void handleSignIn(ActionEvent actionEvent) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Validate email and password
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both email and password.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Error", "Please enter a valid email address.");
            return;
        }

        // Attempt to sign in the user
        User user = userController.signInUser(email, password);

        if (user != null) {
            UserSession.setUser(user); // Set the user session
            showAlert("Success", "Sign-in successful!");
            try {
                loadScene("/tn/esprit/atlas/views/home-view.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "Invalid email or password.");
        }
    }

    // Email validation using regex
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    @FXML
    private void handleForgotPassword(MouseEvent event) throws IOException {
        // Load the forgot password view
        Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/user/auth/forgotpassword-view.fxml"));
        Stage stage = (Stage) forgotPasswordLabel.getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    // Handle Google Sign-In
    @FXML
    private void handleGoogleSignIn(ActionEvent event) {
        try {
            Credential credential = GoogleAuthService.getCredentials();
            if (credential != null) {
                System.out.println("Google Sign-In successful! Access Token: " + credential.getAccessToken());
                showAlert("Success", "Google Sign-In successful!");

                // Fetch user info from Google
                String userInfoJson = GoogleAuthService.fetchUserInfo(credential);
                System.out.println("User Info: " + userInfoJson);

                // Parse user info and create a User object
                User user = createUserFromGoogleResponse(userInfoJson);

                // Set the user session
                UserSession.setUser(user);

                // Redirect to home-view.fxml
                loadScene("/tn/esprit/atlas/views/home-view.fxml");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to sign in with Google.");
        }
    }

    /**
     * Create a User object from the Google user info JSON response.
     *
     * @param userInfoJson The JSON response from Google.
     * @return A User object.
     */
    private User createUserFromGoogleResponse(String userInfoJson) {
        JsonObject jsonObject = JsonParser.parseString(userInfoJson).getAsJsonObject();

        // Extract fields from the JSON response
        String email = jsonObject.get("email").getAsString();
        String name = jsonObject.get("name").getAsString();
        String pictureUrl = jsonObject.get("picture").getAsString(); // Optional: Profile picture URL

        // Create and return a User object
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setProfileImage(pictureUrl); // Optional: Set profile picture URL
        // Set other fields as needed (e.g., role, etc.)

        return user;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}