package tn.esprit.atlas.controllers.user.auth;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}