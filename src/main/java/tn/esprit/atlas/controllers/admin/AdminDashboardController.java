package tn.esprit.atlas.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tn.esprit.atlas.entities.User;
import tn.esprit.atlas.utils.UserSession;
import tn.esprit.atlas.controllers.user.UserController;

import java.util.List;

public class AdminDashboardController {
    @FXML
    private Label role;
    @FXML
    private Label title_username;
    @FXML
    private Label users_count;

    private List<User> users; // List to store all users

    private User user = UserSession.getUser();

    @FXML
    private void initialize() {
        // Set the role and username in the UI
        role.setText(user.getRole());
        title_username.setText(user.getName() + " " + user.getSurname());

        // Fetch all users and store them in the list
        fetchAllUsers();
        users_count.setText(String.valueOf(users.size()));
    }

    // Method to fetch all users
    private void fetchAllUsers() {
        UserController userController = new UserController();
        users = userController.listUsers(); // Fetch the list of users

        // Print the users to verify (optional)
        users.forEach(u -> System.out.println(u.getName() + " - " + u.getEmail()));
    }
}