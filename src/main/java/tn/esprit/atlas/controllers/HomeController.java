package tn.esprit.atlas.controllers;
//package tn.esprit.atlas.controllers.user;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tn.esprit.atlas.entities.User;
import tn.esprit.atlas.utils.UserSession;

import java.io.IOException;

public class HomeController {
    @FXML
    private Label title_username;
    @FXML
    private Button dashboard_button;



    private User user = UserSession.getUser();

    @FXML
    private void initialize() {
        title_username.setText(user.getName() + " " + user.getSurname());
        title_username.setStyle("-fx-font-weight: bold;");

        dashboard_button.setVisible("Admin".equals(user.getRole()));
    }

    @FXML
    private void handleGoToDashboard(){
        loadScene("/tn/esprit/atlas/views/admin/admindashboard-view.fxml");
    }

    //private void handleGoToForum() {loadScene("tn/esprit/atlas/views/community/forum_view.fxml");}


    public void loadScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            if (loader.getLocation() == null) {
                System.out.println("Error: FXML location not found: " + fxml);
                return;  // Return early if location is null
            }
            Parent root = loader.load();
            Stage stage = (Stage) title_username.getScene().getWindow(); // Or any other component's scene reference
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle any potential IOExceptions
        }
    }


    /*
    @FXML
    private void handleCommunityClick(MouseEvent event) {
        try {
            // Load the community forum view FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/community/forum_view.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Label) event.getSource()).getScene().getWindow();

            // Set the new scene with the loaded fxml
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading community forum view: " + e.getMessage());
            e.printStackTrace();
        }
    }
     */
}
