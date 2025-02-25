package tn.esprit.atlas.controllers.admin.user;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import tn.esprit.atlas.controllers.admin.AdminDashboardController;
import tn.esprit.atlas.entities.User;
import tn.esprit.atlas.controllers.user.UserController;

import java.util.List;

public class AdminUserController {

    @FXML
    private ListView<User> userListView;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    private UserController userController = new UserController();
    private AdminDashboardController dashboardController;
    private User selectedUser;

    @FXML
    public void initialize() {
        loadUsers();

        // Initialize buttons as hidden
        updateButton.setVisible(false);
        deleteButton.setVisible(false);

        // Set up custom cell factory for the ListView
        userListView.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create an HBox to align elements
                    HBox row = new HBox();
                    row.setSpacing(10); // Space between columns
                    row.setAlignment(Pos.CENTER_LEFT);
                    row.setMaxWidth(Double.MAX_VALUE); // Allow the HBox to grow within the ListView width

                    // Name Label (Fixed Width)
                    Label nameLabel = new Label(user.getName());
                    nameLabel.setMaxWidth(userListView.getWidth() * 0.3); // 30% of ListView width
                    nameLabel.setMinWidth(userListView.getWidth() * 0.3); // Ensure it doesn't shrink
                    nameLabel.setWrapText(true); // Wrap text if it exceeds the width

                    // Email Label (Fixed Width)
                    Label emailLabel = new Label(user.getEmail());
                    emailLabel.setMaxWidth(userListView.getWidth() * 0.3); // 30% of ListView width
                    emailLabel.setMinWidth(userListView.getWidth() * 0.3); // Ensure it doesn't shrink
                    emailLabel.setWrapText(true); // Wrap text if it exceeds the width

                    // Role Label (Fixed Width)
                    Label roleLabel = new Label(user.getRole());
                    roleLabel.setMaxWidth(userListView.getWidth() * 0.3); // 30% of ListView width
                    roleLabel.setMinWidth(userListView.getWidth() * 0.3); // Ensure it doesn't shrink
                    roleLabel.setWrapText(true); // Wrap text if it exceeds the width

                    // Add elements to the HBox
                    row.getChildren().addAll(nameLabel, emailLabel, roleLabel);

                    // Bind the HBox width to the ListView width
                    row.prefWidthProperty().bind(userListView.widthProperty().subtract(40)); // Subtract some padding

                    // Apply the row as the ListCell's graphic
                    setGraphic(row);
                }
            }
        });

        // Add listener to handle selection changes
        userListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Update the selectedUser field
                selectedUser = newSelection;

                // Show buttons and update their text
                updateButton.setVisible(true);
                deleteButton.setVisible(true);
                updateButton.setText("Update " + newSelection.getName());
                deleteButton.setText("Delete " + newSelection.getName());
            } else {
                // Hide buttons if no user is selected
                updateButton.setVisible(false);
                deleteButton.setVisible(false);

                // Reset the selectedUser field
                selectedUser = null;
            }
        });
    }

    public void setDashboardController(AdminDashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void loadUsers() {
        List<User> users = userController.listUsers();
        userListView.getItems().clear();
        userListView.getItems().addAll(users);
    }

    @FXML
    private void handleDeleteUser() {
        if (selectedUser == null) {
            showAlert("Error", "Please select a user to delete.");
            return;
        }

        // Show a confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete User");
        confirmationAlert.setHeaderText("Are you sure you want to delete this user?");
        confirmationAlert.setContentText("User: " + selectedUser.getName());

        // Wait for the user's response
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // User confirmed deletion
                userController.removeUser(selectedUser.getId());
                loadUsers(); // Refresh the list
                showAlert("Success", "User deleted successfully.");
            }
        });
    }

    @FXML
    private void handleGoToUpdateUser() {
        if (dashboardController == null) {
            System.out.println("DashboardController is null.");
            return;
        }
        if (selectedUser == null) {
            System.out.println("selectedUser is null.");
            return;
        }
        dashboardController.goToUpdateUser(selectedUser);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}