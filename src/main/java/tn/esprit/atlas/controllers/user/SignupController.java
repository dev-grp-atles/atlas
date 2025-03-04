package tn.esprit.atlas.controllers.user;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.atlas.entities.User;
import tn.esprit.atlas.utils.UserSession;

import java.io.File;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Pattern;

public class SignupController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField addressField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button goBackButton;

    @FXML
    private Button uploadButton;

    @FXML
    private File uploadedFile;

    @FXML
    private Label fileLabel;

    private UserController userController = new UserController(); // Instance of UserController

    @FXML
    private void initialize() {
        phoneField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d{0,10}") ? change : null));
        ageField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d{0,3}") ? change : null));
    }

    @FXML
    private void handleSignUp() {
        if (validateForm()) {
            if (isValidEmail(emailField.getText())) {
                if (mathPasswords()) {
                    User user = new User();
                    user.setName(firstNameField.getText());
                    user.setSurname(lastNameField.getText());
                    user.setAge(Integer.parseInt(ageField.getText()));
                    user.setAdresse(addressField.getText());
                    user.setEmail(emailField.getText());
                    user.setPassword(passwordField.getText());
                    user.setNumTel(phoneField.getText());
                    user.setRole("Voyageur");
                    user.setProfileImage(uploadedFile != null ? uploadedFile.getAbsolutePath() : null);
                    user.setVoyageurPreferences("");
                    user.setDestinationsPreferrees("");
                    user.setBudget(0.0);
                    try {
                        // Register the user
                        userController.createUser(user);

                        // Store the user in the session after successful registration
                        UserSession.setUser(user);

                        // Show success alert
                        showAlert(AlertType.INFORMATION, "Success", "User Registered", "Your profile has been successfully registered!");

                        // Clear the form
                        clearForm();

                        // Load the profile view
                        loadScene("/tn/esprit/atlas/views/home-view.fxml");
                    } catch (SQLIntegrityConstraintViolationException e) {
                        showAlert(AlertType.ERROR, "Error", "Email Already Exists", "The email address is already registered. Please use a different email.");
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlert(AlertType.ERROR, "Error", "Registration Failed", "An error occurred while registering the user. Please try again.");
                    }
                } else {
                    showAlert(AlertType.INFORMATION, "Error", "Passwords mismatch", "Passwords do not match!");
                }
            } else {
                showAlert(AlertType.ERROR, "Form Error", "Invalid Email", "Please enter a valid email address.");
            }
        } else {
            showAlert(AlertType.ERROR, "Form Error", "Invalid Input", "Please fill in all fields and upload an image.");
        }
    }


    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean validateForm() {
        return !firstNameField.getText().isEmpty() &&
                !lastNameField.getText().isEmpty() &&
                !ageField.getText().isEmpty() &&
                !addressField.getText().isEmpty() &&
                !emailField.getText().isEmpty() &&
                !phoneField.getText().isEmpty() &&
                !passwordField.getText().isEmpty() &&
                !confirmPasswordField.getText().isEmpty() &&
                uploadedFile != null && isValidEmail(emailField.getText());
    }

    public boolean mathPasswords() {
        return passwordField.getText().equals(confirmPasswordField.getText());
    }

    @FXML
    private void handleUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        uploadedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        fileLabel.setText(uploadedFile != null ? uploadedFile.getName() : "");
    }

    @FXML
    private void handleGoToSignIn(MouseEvent event) throws IOException {
        loadScene("/tn/esprit/atlas/views/user/signin-view.fxml");
    }

    @FXML
    private void handleGoBack() throws IOException {
        loadScene("/tn/esprit/atlas/views/user/signin-view.fxml");
    }

    private void loadScene(String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) goBackButton.getScene().getWindow();
        double width = stage.getWidth();
        double height = stage.getHeight();
        stage.getScene().setRoot(root);
        stage.setWidth(width);
        stage.setHeight(height);
    }

    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        ageField.clear();
        addressField.clear();
        emailField.clear();
        phoneField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        uploadedFile = null;
    }

    private void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
