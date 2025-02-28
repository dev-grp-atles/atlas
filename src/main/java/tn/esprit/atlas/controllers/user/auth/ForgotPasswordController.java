package tn.esprit.atlas.controllers.user.auth;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.atlas.controllers.user.UserController;
import tn.esprit.atlas.utils.UserSession;

import java.io.IOException;

public class ForgotPasswordController {

    @FXML
    private TextField emailField;

    @FXML
    private Button resetPasswordButton;

    @FXML
    private Button goBackButton;

    private UserController userController = new UserController();

    @FXML
    private void handleResetPassword(ActionEvent event) {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            showAlert("Error", "Please enter your email address.");
            return;
        }

        // Call the UserController to reset the password
        boolean resetSuccess = userController.resetPassword(email);

        if (resetSuccess) {
            showAlert("Success", "A password reset link has been sent to your email.");
        } else {
            showAlert("Error", "No account found with that email address.");
        }
    }

    @FXML
    private void handleGoBack(ActionEvent event) throws IOException {
        // Load the sign-in view
        Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/user/auth/signin-view.fxml"));
        Stage stage = (Stage) goBackButton.getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}