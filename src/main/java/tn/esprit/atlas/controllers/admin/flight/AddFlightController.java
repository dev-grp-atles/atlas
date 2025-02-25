package tn.esprit.atlas.controllers.admin.flight;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import tn.esprit.atlas.controllers.admin.AdminDashboardController;
import tn.esprit.atlas.entities.Flight;
import tn.esprit.atlas.services.FlightService;
import tn.esprit.atlas.services.AirLineService;
import tn.esprit.atlas.entities.AirLine;

import java.time.LocalDate;
import java.util.List;

public class AddFlightController {
    @FXML
    private TextField addflight_departureField;
    @FXML
    private TextField addflight_destinationField;
    @FXML
    private DatePicker addflight_departureDatePicker;
    @FXML
    private DatePicker addflight_returnDatePicker;
    @FXML
    private TextField addflight_availableSeatsField;
    @FXML
    private TextField addflight_priceField;
    @FXML
    private ComboBox<AirLine> addflight_airlineComboBox;

    private FlightService flightService = new FlightService();
    private AirLineService airLineService = new AirLineService();
    private AdminDashboardController dashboardController;

    public void setDashboardController(AdminDashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    @FXML
    private void initialize() {
        // Populate the airline ComboBox
        List<AirLine> airlines = airLineService.getall();
        addflight_airlineComboBox.getItems().addAll(airlines);

        // Set a cell factory to display only the airline name in the dropdown list
        addflight_airlineComboBox.setCellFactory(param -> new ListCell<AirLine>() {
            @Override
            protected void updateItem(AirLine airline, boolean empty) {
                super.updateItem(airline, empty);
                if (empty || airline == null) {
                    setText(null);
                } else {
                    setText(airline.getNom());
                }
            }
        });

        // Set a button cell to display only the airline name in the selected item area
        addflight_airlineComboBox.setButtonCell(new ListCell<AirLine>() {
            @Override
            protected void updateItem(AirLine airline, boolean empty) {
                super.updateItem(airline, empty);
                if (empty || airline == null) {
                    setText(null);
                } else {
                    setText(airline.getNom());
                }
            }
        });

        // Ensure available seats and price fields only accept numbers
        addflight_availableSeatsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                addflight_availableSeatsField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        addflight_priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                addflight_priceField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });
    }

    @FXML
    private void handleAddFlight() {
        String departure = addflight_departureField.getText().trim();
        String destination = addflight_destinationField.getText().trim();
        LocalDate departureDate = addflight_departureDatePicker.getValue();
        LocalDate returnDate = addflight_returnDatePicker.getValue();
        String availableSeatsText = addflight_availableSeatsField.getText().trim();
        String priceText = addflight_priceField.getText().trim();
        AirLine selectedAirline = addflight_airlineComboBox.getValue();

        // Validate required fields
        if (departure.isEmpty() || destination.isEmpty() || departureDate == null || returnDate == null || availableSeatsText.isEmpty() || priceText.isEmpty() || selectedAirline == null) {
            showAlert("Error", "Please fill all fields.");
            return;
        }

        // Parse numeric fields
        int availableSeats = Integer.parseInt(availableSeatsText);
        double price = Double.parseDouble(priceText);

        Flight flight = new Flight(departure, destination, departureDate, returnDate, availableSeats, price, selectedAirline.getAirline_id());
        flightService.addFlight(flight);

        if (dashboardController != null) {
            dashboardController.handleGoToFlights();
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