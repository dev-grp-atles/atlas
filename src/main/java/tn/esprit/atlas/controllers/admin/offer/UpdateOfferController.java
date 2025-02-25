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

public class UpdateOfferController {
    @FXML
    private TextField updateoffer_nameField;
    @FXML
    private TextArea updateoffer_descriptionField;
    @FXML
    private TextField updateoffer_priceField;
    @FXML
    private TextField updateoffer_durationField;
    @FXML
    private TextField updateoffer_destinationsField;
    @FXML
    private TextField updateoffer_availableSeatsField;
    @FXML
    private Button updateoffer_imagePickerButton;
    @FXML
    private Label updateoffer_imagePathLabel;

    private File selectedImageFile;
    private OfferService offerService = new OfferService();
    private AdminDashboardController dashboardController;
    private Offer selectedOffer;

    public void setDashboardController(AdminDashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void setSelectedOffer(Offer offer) {
        this.selectedOffer = offer;
        if (selectedOffer != null) {
            updateoffer_nameField.setText(selectedOffer.getName());
            updateoffer_descriptionField.setText(selectedOffer.getDescription());
            updateoffer_priceField.setText(String.valueOf(selectedOffer.getPrice()));
            updateoffer_durationField.setText(String.valueOf(selectedOffer.getDuration()));
            updateoffer_destinationsField.setText(selectedOffer.getDestinations());
            updateoffer_availableSeatsField.setText(String.valueOf(selectedOffer.getAvailableSeats()));
            updateoffer_imagePathLabel.setText(selectedOffer.getPackageImage());
        }
    }

    @FXML
    private void initialize() {
        // Ensure price, duration, and available seats fields only accept numbers
        updateoffer_priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                updateoffer_priceField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });

        updateoffer_durationField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                updateoffer_durationField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        updateoffer_availableSeatsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                updateoffer_availableSeatsField.setText(newValue.replaceAll("[^\\d]", ""));
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

        Stage stage = (Stage) updateoffer_imagePickerButton.getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(stage);

        if (selectedImageFile != null) {
            updateoffer_imagePathLabel.setText(selectedImageFile.getAbsolutePath());
        } else {
            updateoffer_imagePathLabel.setText("No image selected");
        }
    }

    @FXML
    private void handleUpdateOffer() {
        if (selectedOffer == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an offer to update.");
            return;
        }

        String name = updateoffer_nameField.getText().trim();
        String description = updateoffer_descriptionField.getText().trim();
        String priceText = updateoffer_priceField.getText().trim();
        String durationText = updateoffer_durationField.getText().trim();
        String destinations = updateoffer_destinationsField.getText().trim();
        String availableSeatsText = updateoffer_availableSeatsField.getText().trim();
        String packageImage = selectedImageFile != null ? selectedImageFile.getAbsolutePath() : updateoffer_imagePathLabel.getText();

        // Validate required fields
        if (name.isEmpty() || description.isEmpty() || priceText.isEmpty() || durationText.isEmpty() || destinations.isEmpty() || availableSeatsText.isEmpty() || packageImage.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields.");
            return;
        }

        // Parse numeric fields
        double price = Double.parseDouble(priceText);
        int duration = Integer.parseInt(durationText);
        int availableSeats = Integer.parseInt(availableSeatsText);

        selectedOffer.setName(name);
        selectedOffer.setDescription(description);
        selectedOffer.setPrice(price);
        selectedOffer.setDuration(duration);
        selectedOffer.setDestinations(destinations);
        selectedOffer.setAvailableSeats(availableSeats);
        selectedOffer.setPackageImage(packageImage);

        offerService.updateOffer(selectedOffer);
        showAlert(Alert.AlertType.CONFIRMATION, "Success", "Offer updated successfully.");
        dashboardController.handleGoToOffers();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}