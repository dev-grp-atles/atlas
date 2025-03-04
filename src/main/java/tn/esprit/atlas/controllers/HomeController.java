package tn.esprit.atlas.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tn.esprit.atlas.entities.User;
import tn.esprit.atlas.utils.UserSession;

import java.io.IOException;

public class HomeController {
    @FXML
    private Label title_username;
    @FXML
    private Button dashboard_button;
    @FXML
    private StackPane contentPane;
    @FXML
    private HBox overviewButton;
    @FXML
    private HBox tripPlannerButton;
    @FXML
    private HBox bookingButton;
    @FXML
    private HBox communityButton;
    @FXML
    private HBox aiGuideButton;
    @FXML
    private HBox distanceCalculatorButton;
    @FXML
    private HBox currencyConvertorButton;

    private User user = UserSession.getUser();

    @FXML
    private void initialize() {
        title_username.setText(user.getName() + " " + user.getSurname());
        title_username.setStyle("-fx-font-weight: bold;");

        dashboard_button.setVisible("Admin".equals(user.getRole()));

        // Load the overview by default
        loadOverview();
    }

    @FXML
    private void handleGoToDashboard() {
        loadScene("/tn/esprit/atlas/views/admin/admindashboard-view.fxml");
    }

    @FXML
    private void loadOverview() {
        loadView("/tn/esprit/atlas/views/user/overview-view.fxml");
        setActiveButton(overviewButton);
    }

    @FXML
    private void handleGoToTripPlanner() {
        loadView("/tn/esprit/atlas/views/user/tripplanner-view.fxml");
        setActiveButton(tripPlannerButton);
    }

    @FXML
    private void handleGoToBooking() {
        loadView("/tn/esprit/atlas/views/user/booking-view.fxml");
        setActiveButton(bookingButton);
    }

    @FXML
    private void handleGoToCommunity() {
        loadView("/tn/esprit/atlas/views/user/community-view.fxml");
        setActiveButton(communityButton);
    }

    @FXML
    private void handleGoToAIGuide() {
        loadView("/tn/esprit/atlas/views/user/aiguide-view.fxml");
        setActiveButton(aiGuideButton);
    }

    @FXML
    private void handleGoToDistanceCalculator() {
        loadView("/tn/esprit/atlas/views/user/distancecalculator-view.fxml");
        setActiveButton(distanceCalculatorButton);
    }

    @FXML
    private void handleGoToCurrencyconvertor() {
        loadView("/tn/esprit/atlas/views/user/currency-convertor-view.fxml");
        setActiveButton(currencyConvertorButton);
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActiveButton(HBox activeButton) {
        // Remove the active class from all buttons
        overviewButton.getStyleClass().remove("sidenav-button-active");
        tripPlannerButton.getStyleClass().remove("sidenav-button-active");
        bookingButton.getStyleClass().remove("sidenav-button-active");
        communityButton.getStyleClass().remove("sidenav-button-active");
        aiGuideButton.getStyleClass().remove("sidenav-button-active");
        distanceCalculatorButton.getStyleClass().remove("sidenav-button-active");

        // Add the active class to the clicked button
        activeButton.getStyleClass().add("sidenav-button-active");
    }

    public void loadScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) title_username.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}