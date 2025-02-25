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

public class AddAirlineController {
    @FXML
    private TextField addairline_nameField;
    @FXML
    private TextField addairline_countryField;
    @FXML
    private ImageView logoImageView;

    private File uploadedLogoFile;
    private AirLineService airLineService = new AirLineService();
    private AdminDashboardController dashboardController;
    private AirlineController airlineController;

    public void setDashboardController(AdminDashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void setAirlineController(AirlineController airlineController) {
        this.airlineController = airlineController;
    }

    @FXML
    private void handleUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Logo");
        uploadedLogoFile = fileChooser.showOpenDialog(logoImageView.getScene().getWindow());

        if (uploadedLogoFile != null) {
            Image image = new Image(uploadedLogoFile.toURI().toString());
            logoImageView.setImage(image);
        }
    }

    @FXML
    private void handleAddAirline() {
        String name = addairline_nameField.getText().trim();
        String country = addairline_countryField.getText().trim();

        // Validate required fields
        if (name.isEmpty() || country.isEmpty()) {
            showAlert("Error", "Please fill all fields.");
            return;
        }

        if (uploadedLogoFile == null) {
            showAlert("Error", "Please select a logo.");
            return;
        }

        String logoPath = uploadedLogoFile.getAbsolutePath();
        AirLine airLine = new AirLine();
        airLine.setNom(name);
        airLine.setPays(country);
        airLine.setLogo(logoPath);

        airLineService.addAirline(airLine);

        // Refresh the list in AirlineController
        if (airlineController != null) {
            airlineController.loadAirlines();
        }

        // Return to the airline list view
        if (dashboardController != null) {
            dashboardController.handleGoToAirlines();
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