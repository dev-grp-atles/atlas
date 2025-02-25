package tn.esprit.atlas.controllers.admin.airline;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import tn.esprit.atlas.controllers.admin.AdminDashboardController;
import tn.esprit.atlas.entities.AirLine;
import tn.esprit.atlas.services.AirLineService;

import java.io.File;

public class AirlineController {
    @FXML
    private ListView<AirLine> airlineListView;

    private AirLineService airLineService = new AirLineService();
    private AdminDashboardController dashboardController;

    @FXML
    private Button update_button;
    @FXML
    private Button delete_button;

    private AirLine selectedAirline; // Declare selectedAirline as a class-level field

    @FXML
    private void initialize() {
        loadAirlines();

        // Initialize buttons as hidden
        update_button.setVisible(false);
        delete_button.setVisible(false);

        airlineListView.setCellFactory(lv -> new ListCell<AirLine>() {
            @Override
            protected void updateItem(AirLine airline, boolean empty) {
                super.updateItem(airline, empty);
                if (empty || airline == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create an HBox to align elements
                    HBox row = new HBox();
                    row.setSpacing(10); // Space between columns
                    row.setAlignment(Pos.CENTER_LEFT);
                    row.setMaxWidth(Double.MAX_VALUE); // Allow the HBox to grow within the ListView width

                    // Name Label (Fixed Width)
                    Label nameLabel = new Label(airline.getNom());
                    nameLabel.setMaxWidth(airlineListView.getWidth() * 0.3); // 30% of ListView width
                    nameLabel.setMinWidth(airlineListView.getWidth() * 0.3); // Ensure it doesn't shrink
                    nameLabel.setWrapText(true); // Wrap text if it exceeds the width

                    // Country Label (Fixed Width)
                    Label countryLabel = new Label(airline.getPays());
                    countryLabel.setMaxWidth(airlineListView.getWidth() * 0.3); // 30% of ListView width
                    countryLabel.setMinWidth(airlineListView.getWidth() * 0.3); // Ensure it doesn't shrink
                    countryLabel.setWrapText(true); // Wrap text if it exceeds the width

                    // Logo Image (Fixed Width)
                    ImageView logoImageView = new ImageView();
                    if (airline.getLogo() != null && !airline.getLogo().isEmpty()) {
                        File file = new File(airline.getLogo());
                        if (file.exists()) {
                            Image image = new Image(file.toURI().toString());
                            logoImageView.setImage(image);
                            logoImageView.setFitWidth(50); // Fixed width for the logo
                            logoImageView.setFitHeight(50); // Fixed height for the logo
                            logoImageView.setPreserveRatio(true);
                        }
                    }

                    // Add elements to the HBox
                    row.getChildren().addAll(nameLabel, countryLabel, logoImageView);

                    // Bind the HBox width to the ListView width
                    row.prefWidthProperty().bind(airlineListView.widthProperty().subtract(40)); // Subtract some padding

                    // Apply the row as the ListCell's graphic
                    setGraphic(row);
                }
            }
        });

        // Add listener to handle selection changes
        airlineListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Update the selectedAirline field
                selectedAirline = newSelection;

                // Show buttons and update their text
                update_button.setVisible(true);
                delete_button.setVisible(true);
                update_button.setText("Update " + newSelection.getNom());
                delete_button.setText("Delete " + newSelection.getNom());
            } else {
                // Hide buttons if no airline is selected
                update_button.setVisible(false);
                delete_button.setVisible(false);

                // Reset the selectedAirline field
                selectedAirline = null;
            }
        });
    }

    public void setDashboardController(AdminDashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void loadAirlines() {
        airlineListView.getItems().clear();
        airlineListView.getItems().addAll(airLineService.getall());
    }

    @FXML
    private void handleDeleteAirline() {
        if (selectedAirline == null) {
            showAlert("Error", "Please select an airline to delete.");
            return;
        }

        // Show a confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Airline");
        confirmationAlert.setHeaderText("Are you sure you want to delete this airline?");
        confirmationAlert.setContentText("Airline: " + selectedAirline.getNom());

        // Wait for the user's response
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // User confirmed deletion
                airLineService.deleteAirline(selectedAirline);
                loadAirlines(); // Refresh the list
                showAlert("Success", "Airline deleted successfully.");
            }
        });
    }

    @FXML
    private void handleGoToAddAirline() {
        if (dashboardController != null) {
            dashboardController.goToAddAirline();
        }
    }

    @FXML
    private void handleGoToUpdateAirline() {
        if (dashboardController != null && selectedAirline != null) {
            dashboardController.goToUpdateAirline(selectedAirline);
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}