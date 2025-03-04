package tn.esprit.atlas.controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tn.esprit.atlas.entities.User;
import tn.esprit.atlas.utils.UserSession;

import java.io.IOException;

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
        loadScene("/tn/esprit/atlas/views/user/signup-view.fxml");
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
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both email and password.");
            return;
        }

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


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
