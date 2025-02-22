package tn.esprit.atlas.controllers.admin;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class AddAirlineController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField countryField;

    @FXML
    private Button selectLogoButton;

    @FXML
    private ImageView logoImageView;
    @FXML
    private Button addairlines_button;
    @FXML
    private Label title_username;

    @FXML
    private void handleSelectLogo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Logo Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            logoImageView.setImage(image);
        }
    }
    @FXML
    private void handleGoToAddAirline(){
        loadScene("/tn/esprit/atlas/views/admin/addairlines-view.fxml");
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

    public Button getAirlines_button() {
        return addairlines_button;
    }

    public void setAirlines_button(Button airlines_button) {
        this.addairlines_button = airlines_button;
    }


}