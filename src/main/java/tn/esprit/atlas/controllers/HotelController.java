package tn.esprit.atlas.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HotelController {
    @FXML
    private Button addHotelButton;
    @FXML
    private Button updateHotelButton;
    @FXML
    private Button deleteHotelButton;
    @FXML
    private Button goBackButton;
    @FXML
    private TextField hotelNameField;
    @FXML
    private TextField hotelLocationField;

    @FXML
    private void handleGoBack() throws IOException {
        Parent mainRoot = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/main-view.fxml"));
        Scene newScene = new Scene(mainRoot);
        Stage stage = (Stage) goBackButton.getScene().getWindow();
        stage.setScene(newScene);
    }

    @FXML
    private void addHotel(ActionEvent event) {
        String name = hotelNameField.getText();
        String location = hotelLocationField.getText();
        // Add logic to save hotel to database
    }

    @FXML
    private void updateHotel(ActionEvent event) {
        // Add logic to update hotel details
    }

    @FXML
    private void deleteHotel(ActionEvent event) {
        // Add logic to delete hotel
    }
}