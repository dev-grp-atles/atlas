package tn.esprit.atlas.controllers.admin.airline;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import tn.esprit.atlas.controllers.admin.AdminDashboardController;
import tn.esprit.atlas.entities.AirLine;
import tn.esprit.atlas.services.AirLineService;

import java.io.File;

public class UpdateAirlineController {
    @FXML
    private TextField updateairline_nameField;
    @FXML
    private TextField updateairline_countryField;
    @FXML
    private ImageView update_logoImageView;

    private File uploadedLogoFile;
    private AirLineService airLineService = new AirLineService();
    private AdminDashboardController dashboardController;
    private AirLine selectedAirline;

    public void setDashboardController(AdminDashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void setSelectedAirline(AirLine airline) {
        this.selectedAirline = airline;
        if (selectedAirline != null) {
            updateairline_nameField.setText(selectedAirline.getNom());
            updateairline_countryField.setText(selectedAirline.getPays());
            if (selectedAirline.getLogo() != null && !selectedAirline.getLogo().isEmpty()) {
                File logoFile = new File(selectedAirline.getLogo());
                if (logoFile.exists()) {
                    Image image = new Image(logoFile.toURI().toString());
                    update_logoImageView.setImage(image);
                }
            }
        }
    }

    @FXML
    private void handleUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Logo");
        uploadedLogoFile = fileChooser.showOpenDialog(update_logoImageView.getScene().getWindow());

        if (uploadedLogoFile != null) {
            Image image = new Image(uploadedLogoFile.toURI().toString());
            update_logoImageView.setImage(image);
        }
    }

    @FXML
    private void handleUpdateAirline() {
        if (selectedAirline == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an airline to update.");
            return;
        }

        String name = updateairline_nameField.getText().trim();
        String country = updateairline_countryField.getText().trim();

        // Validate required fields
        if (name.isEmpty() || country.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields.");
            return;
        }

        String logoPath = uploadedLogoFile != null ? uploadedLogoFile.getAbsolutePath() : selectedAirline.getLogo();
        selectedAirline.setNom(name);
        selectedAirline.setPays(country);
        selectedAirline.setLogo(logoPath);

        airLineService.updateAirline(selectedAirline);
        showAlert(Alert.AlertType.CONFIRMATION, "Success", "Airline updated successfully.");
        dashboardController.handleGoToAirlines();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}