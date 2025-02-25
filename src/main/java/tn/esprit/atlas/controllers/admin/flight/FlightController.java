package tn.esprit.atlas.controllers.admin.flight;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.atlas.controllers.admin.AdminDashboardController;
import tn.esprit.atlas.entities.Flight;
import tn.esprit.atlas.services.FlightService;

public class FlightController {
    @FXML
    private ListView<Flight> flightListView;

    private FlightService flightService = new FlightService();
    private AdminDashboardController dashboardController;

    @FXML
    private Button update_button;
    @FXML
    private Button delete_button;

    private Flight selectedFlight;

    @FXML
    private void initialize() {
        loadFlights();

        update_button.setVisible(false);
        delete_button.setVisible(false);

        flightListView.setCellFactory(lv -> new ListCell<Flight>() {
            @Override
            protected void updateItem(Flight flight, boolean empty) {
                super.updateItem(flight, empty);
                if (empty || flight == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox row = new HBox();
                    row.setSpacing(10);
                    row.setAlignment(Pos.CENTER_LEFT);
                    row.setMaxWidth(Double.MAX_VALUE);

                    Label departureLabel = new Label(flight.getDeparture());
                    departureLabel.setMaxWidth(flightListView.getWidth() * 0.2);
                    departureLabel.setMinWidth(flightListView.getWidth() * 0.2);
                    departureLabel.setWrapText(true);

                    Label destinationLabel = new Label(flight.getDestination());
                    destinationLabel.setMaxWidth(flightListView.getWidth() * 0.2);
                    destinationLabel.setMinWidth(flightListView.getWidth() * 0.2);
                    destinationLabel.setWrapText(true);

                    Label departureDateLabel = new Label(flight.getDepartureDate().toString());
                    departureDateLabel.setMaxWidth(flightListView.getWidth() * 0.2);
                    departureDateLabel.setMinWidth(flightListView.getWidth() * 0.2);
                    departureDateLabel.setWrapText(true);

                    Label returnDateLabel = new Label(flight.getReturnDate().toString());
                    returnDateLabel.setMaxWidth(flightListView.getWidth() * 0.2);
                    returnDateLabel.setMinWidth(flightListView.getWidth() * 0.2);
                    returnDateLabel.setWrapText(true);

                    Label priceLabel = new Label(String.valueOf(flight.getPrice()));
                    priceLabel.setMaxWidth(flightListView.getWidth() * 0.1);
                    priceLabel.setMinWidth(flightListView.getWidth() * 0.1);
                    priceLabel.setWrapText(true);

                    row.getChildren().addAll(departureLabel, destinationLabel, departureDateLabel, returnDateLabel, priceLabel);
                    row.prefWidthProperty().bind(flightListView.widthProperty().subtract(40));
                    setGraphic(row);
                }
            }
        });

        flightListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedFlight = newSelection;
                update_button.setVisible(true);
                delete_button.setVisible(true);
                update_button.setText("Update " + newSelection.getDeparture());
                delete_button.setText("Delete " + newSelection.getDeparture());
            } else {
                update_button.setVisible(false);
                delete_button.setVisible(false);
                selectedFlight = null;
            }
        });
    }

    public void setDashboardController(AdminDashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void loadFlights() {
        flightListView.getItems().clear();
        flightListView.getItems().addAll(flightService.getall());
    }

    @FXML
    private void handleDeleteFlight() {
        if (selectedFlight == null) {
            showAlert("Error", "Please select a flight to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Flight");
        confirmationAlert.setHeaderText("Are you sure you want to delete this flight?");
        confirmationAlert.setContentText("Flight: " + selectedFlight.getDeparture());

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                flightService.deleteFlight(selectedFlight);
                loadFlights();
                showAlert("Success", "Flight deleted successfully.");
            }
        });
    }

    @FXML
    private void handleGoToAddFlight() {
        if (dashboardController != null) {
            dashboardController.goToAddFlight();
        }
    }

    @FXML
    private void handleGoToUpdateFlight() {
        if (dashboardController != null && selectedFlight != null) {
            dashboardController.goToUpdateFlight(selectedFlight);
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