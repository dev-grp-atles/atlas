package tn.esprit.atlas.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
