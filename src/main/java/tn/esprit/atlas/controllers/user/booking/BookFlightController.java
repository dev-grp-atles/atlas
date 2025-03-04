package tn.esprit.atlas.controllers.user.booking;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import tn.esprit.atlas.entities.AirLine;
import tn.esprit.atlas.entities.Flight;
import tn.esprit.atlas.services.AirLineService;
import tn.esprit.atlas.services.FlightService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BookFlightController implements Initializable {

    @FXML
    private FlowPane airlinesContainer; // Container to display airlines

    private AirLineService airLineService = new AirLineService();
    private FlightService flightService = new FlightService();

    private StackPane bookingContent; // Reference to the bookingContent StackPane

    // Set the bookingContent StackPane
    public void setBookingContent(StackPane bookingContent) {
        this.bookingContent = bookingContent;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAirlines();
    }

    private void loadAirlines() {
        // Fetch all airlines
        List<AirLine> airlines = airLineService.getall();

        // Fetch all flights
        List<Flight> flights = flightService.getall();

        // Display each airline
        for (AirLine airline : airlines) {
            // Count the number of flights for this airline
            int numberOfFlights = (int) flights.stream()
                    .filter(flight -> flight.getAirline_id() == airline.getAirline_id())
                    .count();

            // Create a VBox to display airline details
            VBox airlineBox = new VBox(10);
            airlineBox.setMinWidth(500);
            airlineBox.setStyle("-fx-background-color: #f2f3f5; -fx-background-radius: 5px; -fx-padding: 10; -fx-cursor: hand");

            // Add airline logo
            ImageView logoView = new ImageView();
            logoView.setFitHeight(50); // Set a fixed height for the logo
            logoView.setPreserveRatio(true); // Maintain aspect ratio

            // Load the logo (ensure it's a valid URL or file path)
            String logo = airline.getLogo();
            if (logo != null && !logo.isEmpty()) {
                try {
                    // Convert file path to URL if necessary
                    if (logo.startsWith("C:")) {
                        logo = new File(logo).toURI().toString();
                    }
                    logoView.setImage(new Image(logo));
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid logo URL: " + logo);
                    // Optionally, set a default image
                    logoView.setImage(new Image("file:src/main/resources/tn/esprit/atlas/assets/default-logo.png"));
                }
            } else {
                // Set a default image if no logo is provided
                logoView.setImage(new Image("file:src/main/resources/tn/esprit/atlas/assets/default-logo.png"));
            }

            // Add the logo to the airline box
            airlineBox.getChildren().add(logoView);

            // Add airline name
            Label nameLabel = new Label(airline.getNom());
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            airlineBox.getChildren().add(nameLabel);

            // Add airline country
            Label countryLabel = new Label("Country: " + airline.getPays());
            countryLabel.setStyle("-fx-font-size: 12;");
            airlineBox.getChildren().add(countryLabel);

            // Add number of flights
            Label flightsLabel = new Label("Available Flights: " + numberOfFlights);
            flightsLabel.setStyle("-fx-font-size: 12;");
            airlineBox.getChildren().add(flightsLabel);

            // Set click event for the airline box
            airlineBox.setOnMouseClicked(event -> {
                try {
                    // Load the flights for this airline into the bookingContent
                    loadFlightsForAirline(airline);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Add the airline box to the container
            airlinesContainer.getChildren().add(airlineBox);
        }
    }

    private void loadFlightsForAirline(AirLine airline) throws IOException {
        System.out.println("Loading flights for airline: " + airline.getNom());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/user/booking/flights-view.fxml"));
        Parent flightsView = loader.load();

        FlightsController flightsController = loader.getController();
        flightsController.setAirline(airline); // Set the airline before displaying the view

        // Replace the current content in the bookingContent StackPane
        bookingContent.getChildren().setAll(flightsView);
    }
}