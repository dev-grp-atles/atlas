package tn.esprit.atlas.controllers.admin.offer;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.atlas.controllers.admin.AdminDashboardController;
import tn.esprit.atlas.entities.Offer;
import tn.esprit.atlas.services.OfferService;

import java.io.File;

public class AddOfferController {
    @FXML
    private TextField addoffer_nameField;
    @FXML
    private TextArea addoffer_descriptionField;
    @FXML
    private TextField addoffer_priceField;
    @FXML
    private TextField addoffer_durationField;
    @FXML
    private TextField addoffer_destinationsField;
    @FXML
    private TextField addoffer_availableSeatsField;
    @FXML
    private Button addoffer_imagePickerButton;
    @FXML
    private Label addoffer_imagePathLabel;

    private File selectedImageFile;
    private OfferService offerService = new OfferService();
    private AdminDashboardController dashboardController;

    public void setDashboardController(AdminDashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    @FXML
    private void initialize() {
        // Ensure price, duration, and available seats fields only accept numbers
        addoffer_priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                addoffer_priceField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });

        addoffer_durationField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                addoffer_durationField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        addoffer_availableSeatsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                addoffer_availableSeatsField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    private void handleImagePicker() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        Stage stage = (Stage) addoffer_imagePickerButton.getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(stage);

        if (selectedImageFile != null) {
            addoffer_imagePathLabel.setText(selectedImageFile.getAbsolutePath());
        } else {
            addoffer_imagePathLabel.setText("No image selected");
        }
    }

    @FXML
    private void handleAddOffer() {
        String name = addoffer_nameField.getText().trim();
        String description = addoffer_descriptionField.getText().trim();
        String priceText = addoffer_priceField.getText().trim();
        String durationText = addoffer_durationField.getText().trim();
        String destinations = addoffer_destinationsField.getText().trim();
        String availableSeatsText = addoffer_availableSeatsField.getText().trim();
        String packageImage = selectedImageFile != null ? selectedImageFile.getAbsolutePath() : "";

        // Validate required fields
        if (name.isEmpty() || description.isEmpty() || priceText.isEmpty() || durationText.isEmpty() || destinations.isEmpty() || availableSeatsText.isEmpty() || packageImage.isEmpty()) {
            showAlert("Error", "Please fill all fields.");
            return;
        }

        // Parse numeric fields
        double price = Double.parseDouble(priceText);
        int duration = Integer.parseInt(durationText);
        int availableSeats = Integer.parseInt(availableSeatsText);

        Offer offer = new Offer(name, description, price, duration, destinations, availableSeats, packageImage);
        offerService.addOffer(offer);

        if (dashboardController != null) {
            dashboardController.handleGoToOffers();
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