package tn.esprit.atlas.controllers.admin.flight;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.atlas.controllers.admin.AdminDashboardController;
import tn.esprit.atlas.entities.Flight;
import tn.esprit.atlas.services.FlightService;
import tn.esprit.atlas.services.AirLineService;
import tn.esprit.atlas.entities.AirLine;

import java.time.LocalDate;
import java.util.List;

public class UpdateFlightController {
    @FXML
    private TextField updateflight_departureField;
    @FXML
    private TextField updateflight_destinationField;
    @FXML
    private DatePicker updateflight_departureDatePicker;
    @FXML
    private DatePicker updateflight_returnDatePicker;
    @FXML
    private TextField updateflight_availableSeatsField;
    @FXML
    private TextField updateflight_priceField;
    @FXML
    private ComboBox<AirLine> updateflight_airlineComboBox;

    private FlightService flightService = new FlightService();
    private AirLineService airLineService = new AirLineService();
    private AdminDashboardController dashboardController;
    private Flight selectedFlight;

    public void setDashboardController(AdminDashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    @FXML
    private void initialize() {
        // Populate the airline ComboBox
        List<AirLine> airlines = airLineService.getall();
        updateflight_airlineComboBox.getItems().addAll(airlines);

        // Set a cell factory to display only the airline name in the dropdown list
        updateflight_airlineComboBox.setCellFactory(param -> new ListCell<AirLine>() {
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
        updateflight_airlineComboBox.setButtonCell(new ListCell<AirLine>() {
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
        updateflight_availableSeatsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                updateflight_availableSeatsField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        updateflight_priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                updateflight_priceField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });
    }

    public void setSelectedFlight(Flight flight) {
        this.selectedFlight = flight;
        if (selectedFlight != null) {
            updateflight_departureField.setText(selectedFlight.getDeparture());
            updateflight_destinationField.setText(selectedFlight.getDestination());
            updateflight_departureDatePicker.setValue(selectedFlight.getDepartureDate());
            updateflight_returnDatePicker.setValue(selectedFlight.getReturnDate());
            updateflight_availableSeatsField.setText(String.valueOf(selectedFlight.getAvailableSeats()));
            updateflight_priceField.setText(String.valueOf(selectedFlight.getPrice()));

            // Set the selected airline in the ComboBox
            AirLine selectedAirline = airLineService.getall().stream()
                    .filter(airline -> airline.getAirline_id() == selectedFlight.getAirline_id())
                    .findFirst()
                    .orElse(null);
            updateflight_airlineComboBox.setValue(selectedAirline);
        }
    }

    @FXML
    private void handleUpdateFlight() {
        if (selectedFlight == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a flight to update.");
            return;
        }

        String departure = updateflight_departureField.getText().trim();
        String destination = updateflight_destinationField.getText().trim();
        LocalDate departureDate = updateflight_departureDatePicker.getValue();
        LocalDate returnDate = updateflight_returnDatePicker.getValue();
        String availableSeatsText = updateflight_availableSeatsField.getText().trim();
        String priceText = updateflight_priceField.getText().trim();
        AirLine selectedAirline = updateflight_airlineComboBox.getValue();

        // Validate required fields
        if (departure.isEmpty() || destination.isEmpty() || departureDate == null || returnDate == null || availableSeatsText.isEmpty() || priceText.isEmpty() || selectedAirline == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields.");
            return;
        }

        // Parse numeric fields
        int availableSeats = Integer.parseInt(availableSeatsText);
        double price = Double.parseDouble(priceText);

        selectedFlight.setDeparture(departure);
        selectedFlight.setDestination(destination);
        selectedFlight.setDepartureDate(departureDate);
        selectedFlight.setReturnDate(returnDate);
        selectedFlight.setAvailableSeats(availableSeats);
        selectedFlight.setPrice(price);
        selectedFlight.setAirline_id(selectedAirline.getAirline_id());

        flightService.updateFlight(selectedFlight);
        showAlert(Alert.AlertType.CONFIRMATION, "Success", "Flight updated successfully.");
        dashboardController.handleGoToFlights();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}