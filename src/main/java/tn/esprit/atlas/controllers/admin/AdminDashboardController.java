package tn.esprit.atlas.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.atlas.entities.User;
import tn.esprit.atlas.utils.UserSession;
import tn.esprit.atlas.controllers.user.UserController;

import java.io.IOException;
import java.util.List;

public class AdminDashboardController {
    @FXML
    private Label role;
    @FXML
    private Label title_username;
    @FXML
    private Label users_count;

    @FXML
    private HBox airlines_button;

    //interfaces
    @FXML
    private HBox overview;
    @FXML
    private HBox airlines;

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
        overview.setVisible(true);
        airlines.setVisible(false);
    }

    // Method to fetch all users
    private void fetchAllUsers() {
        UserController userController = new UserController();
        users = userController.listUsers(); // Fetch the list of users

        // Print the users to verify (optional)
        users.forEach(u -> System.out.println(u.getName() + " - " + u.getEmail()));
    }
    @FXML
    private void handleGoToAirlines(){
        overview.setVisible(false);
        airlines.setVisible(true);

    }
    @FXML
    private void handleGoToOverview(){
        loadScene("/tn/esprit/atlas/views/admin/admindashboard-view.fxml");
    }
    @FXML
    private void handleGoToUsers(){
        loadScene("/tn/esprit/atlas/views/admin/users-view.fxml");
    }
    @FXML
    private void handleGoToReservations(){
        loadScene("/tn/esprit/atlas/views/admin/reservations-view.fxml");
    }
    @FXML
    private void handleGoToFlights(){
        loadScene("/tn/esprit/atlas/views/admin/flights-view.fxml");
    }

    public void loadScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) title_username.getScene().getWindow(); // Or any other component's scene reference
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle any potential IOExceptions
        }
    }
}