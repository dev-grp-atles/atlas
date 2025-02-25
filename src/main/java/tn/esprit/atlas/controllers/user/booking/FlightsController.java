package tn.esprit.atlas.controllers.user.booking;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import tn.esprit.atlas.entities.AirLine;
import tn.esprit.atlas.entities.Flight;
import tn.esprit.atlas.services.FlightService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FlightsController implements Initializable {

    @FXML
    private VBox flightsContainer; // Container to display flights

    private AirLine airline;
    private FlightService flightService = new FlightService();

    private int userId; // Assuming you have a way to get the current user's ID

    // Set the selected airline
    public void setAirline(AirLine airline) {
        this.airline = airline;
        System.out.println("Airline set: " + airline.getNom());
        loadFlights(); // Explicitly load flights after setting the airline
    }

    // Set the current user ID
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("FlightsController initialized!");
        // No need to call loadFlights here since it will be called after setAirline
    }

    private void loadFlights() {
        if (airline == null) {
            System.out.println("Airline is null! Cannot load flights.");
            return;
        }

        // Fetch flights for the selected airline
        List<Flight> flights = flightService.getall().stream()
                .filter(flight -> flight.getAirline_id() == airline.getAirline_id())
                .toList();

        System.out.println("Number of flights found: " + flights.size());

        // Clear the container before adding new flights
        flightsContainer.getChildren().clear();

        // Add a title at the top of the flights container
        Label titleLabel = new Label("Flights of " + airline.getNom());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10px;");
        flightsContainer.getChildren().add(titleLabel);

        // Display each flight in a row with a "Reserve" button
        for (Flight flight : flights) {
            System.out.println("Flight: " + flight.getDeparture() + " >> " + flight.getDestination());

            // Create an HBox to hold the flight details and the "Reserve" button
            HBox flightBox = new HBox(10); // 10 is the spacing between elements
            flightBox.setStyle("-fx-background-color: #f2f3f5; -fx-background-radius: 5px; -fx-padding: 10;");

            // Create a VBox to hold the flight route, date, and price
            VBox flightDetails = new VBox(5); // 5 is the spacing between labels

            // Add flight route (Qatar >> Tunisia format)
            Label routeLabel = new Label(flight.getDeparture() + " >> " + flight.getDestination());
            routeLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            // Add flight date
            Label dateLabel = new Label("Date: " + flight.getDepartureDate());
            dateLabel.setStyle("-fx-font-size: 12px;");

            // Add flight price
            Label priceLabel = new Label("Price: " + flight.getPrice() + " DT");
            priceLabel.setStyle("-fx-font-size: 12px;");

            // Add route, date, and price to the VBox
            flightDetails.getChildren().addAll(routeLabel, dateLabel, priceLabel);

            // Create a "Reserve" button
            Button reserveButton = new Button("Reserve");
            reserveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
            reserveButton.setOnAction(event -> {
                // Load the flight reservation form when the "Reserve" button is clicked
                loadFlightReservationForm(flight);
            });

            // Add the flight details and the reserve button to the HBox
            flightBox.getChildren().addAll(flightDetails, reserveButton);

            // Align the "Reserve" button to the far right
            HBox.setHgrow(flightDetails, Priority.ALWAYS); // Allow flight details to take up remaining space
            HBox.setMargin(reserveButton, new Insets(0, 0, 0, 10)); // Add margin to the button

            // Add the flight box to the container
            flightsContainer.getChildren().add(flightBox);
        }
    }

    // Method to load the flight reservation form
    private void loadFlightReservationForm(Flight flight) {
        try {
            // Load the FXML file for the flight reservation form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/user/booking/reservationform-view.fxml"));
            Parent reservationForm = loader.load();

            // Get the controller for the reservation form
            FlightReservationFormController controller = loader.getController();
            controller.setUserId(userId); // Set the current user ID
            controller.setVolId(flight.getVol_id()); // Set the flight ID for which the reservation is being made

            // Replace the current content with the reservation form
            flightsContainer.getChildren().setAll(reservationForm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}