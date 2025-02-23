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

public class ReviewController {
    @FXML
    private Button addReviewButton;
    @FXML
    private Button updateReviewButton;
    @FXML
    private Button deleteReviewButton;
    @FXML
    private Button goBackButton;
    @FXML
    private TextField reviewTextField;
    @FXML
    private TextField ratingField;

    @FXML
    private void handleGoBack() throws IOException {
        Parent mainRoot = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/main-view.fxml"));
        Scene newScene = new Scene(mainRoot);
        Stage stage = (Stage) goBackButton.getScene().getWindow();
        stage.setScene(newScene);
    }

    @FXML
    private void addReview(ActionEvent event) {
        String reviewText = reviewTextField.getText();
        String rating = ratingField.getText();
        // Add logic to save review to database
    }

    @FXML
    private void updateReview(ActionEvent event) {
        // Add logic to update review details
    }

    @FXML
    private void deleteReview(ActionEvent event) {
        // Add logic to delete review
    }
}