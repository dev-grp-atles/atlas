package tn.esprit.atlas.controllers.admin.user;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.atlas.controllers.admin.AdminDashboardController;
import tn.esprit.atlas.entities.User;
import tn.esprit.atlas.controllers.user.UserController;

import java.io.File;

public class UpdateUserController {

    @FXML
    private TextField updateuser_nameField;

    @FXML
    private TextField updateuser_surnameField;

    @FXML
    private TextField updateuser_ageField;

    @FXML
    private TextField updateuser_emailField;

    @FXML
    private TextField updateuser_passwordField;

    @FXML
    private TextField updateuser_adresseField;

    @FXML
    private ComboBox<String> updateuser_roleComboBox;

    @FXML
    private TextField updateuser_profileImageField;

    @FXML
    private TextField updateuser_numTelField;

    @FXML
    private TextField updateuser_voyageurPreferencesField;

    @FXML
    private TextField updateuser_destinationsPreferreesField;

    @FXML
    private TextField updateuser_budgetField;

    @FXML
    private Button uploadImageButton;

    private UserController userController = new UserController();
    private AdminDashboardController dashboardController;
    private User selectedUser;

    @FXML
    public void initialize() {
        // Initialize the role ComboBox with the three options
        updateuser_roleComboBox.getItems().addAll("Voyageur", "SupportClient", "Admin");

        // Ensure age and budget fields only accept numbers
        updateuser_ageField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                updateuser_ageField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        updateuser_budgetField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                updateuser_budgetField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });

        // Ensure phone number field only accepts digits
        updateuser_numTelField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                updateuser_numTelField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void setDashboardController(AdminDashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void setSelectedUser(User user) {
        this.selectedUser = user;
        if (selectedUser != null) {
            // Populate the fields with the selected user's data
            updateuser_nameField.setText(selectedUser.getName());
            updateuser_surnameField.setText(selectedUser.getSurname());
            updateuser_ageField.setText(String.valueOf(selectedUser.getAge()));
            updateuser_emailField.setText(selectedUser.getEmail());
            updateuser_passwordField.setText(selectedUser.getPassword());
            updateuser_adresseField.setText(selectedUser.getAdresse());
            updateuser_roleComboBox.setValue(selectedUser.getRole());
            updateuser_profileImageField.setText(selectedUser.getProfileImage());
            updateuser_numTelField.setText(selectedUser.getNumTel());
            updateuser_voyageurPreferencesField.setText(selectedUser.getVoyageurPreferences());
            updateuser_destinationsPreferreesField.setText(selectedUser.getDestinationsPreferrees());
            updateuser_budgetField.setText(String.valueOf(selectedUser.getBudget()));
        }
    }

    @FXML
    private void handleUpdateUser() {
        if (selectedUser == null) {
            showAlert("Error", "No user selected.");
            return;
        }

        // Validate required fields
        if (updateuser_nameField.getText().trim().isEmpty() || updateuser_emailField.getText().trim().isEmpty() || updateuser_roleComboBox.getValue() == null) {
            showAlert("Error", "Please fill all required fields (Name, Email, Role).");
            return;
        }

        // Validate email format
        if (!updateuser_emailField.getText().trim().contains("@")) {
            showAlert("Error", "Please enter a valid email address.");
            return;
        }

        // Validate age and budget fields
        if (updateuser_ageField.getText().trim().isEmpty() || updateuser_budgetField.getText().trim().isEmpty()) {
            showAlert("Error", "Age and Budget fields cannot be empty.");
            return;
        }

        // Update the selected user
        selectedUser.setName(updateuser_nameField.getText().trim());
        selectedUser.setSurname(updateuser_surnameField.getText().trim());
        selectedUser.setAge(Integer.parseInt(updateuser_ageField.getText().trim()));
        selectedUser.setEmail(updateuser_emailField.getText().trim());
        selectedUser.setPassword(updateuser_passwordField.getText().trim());
        selectedUser.setAdresse(updateuser_adresseField.getText().trim());
        selectedUser.setRole(updateuser_roleComboBox.getValue());
        selectedUser.setProfileImage(updateuser_profileImageField.getText().trim());
        selectedUser.setNumTel(updateuser_numTelField.getText().trim());
        selectedUser.setVoyageurPreferences(updateuser_voyageurPreferencesField.getText().trim());
        selectedUser.setDestinationsPreferrees(updateuser_destinationsPreferreesField.getText().trim());
        selectedUser.setBudget(Double.parseDouble(updateuser_budgetField.getText().trim()));

        // Call the update method from UserController
        userController.modifyUser(selectedUser);

        // Show success message
        showAlert("Success", "User updated successfully.");

        // Return to the users list view
        if (dashboardController != null) {
            dashboardController.handleGoToUsers();
        }
    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // Convert the file path to a URL and set it in the profile image field
            String fileUrl = selectedFile.toURI().toString();
            updateuser_profileImageField.setText(fileUrl);
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