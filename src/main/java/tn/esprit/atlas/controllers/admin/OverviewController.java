package tn.esprit.atlas.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import tn.esprit.atlas.services.*;

import java.net.URL;
import java.util.ResourceBundle;

public class OverviewController implements Initializable {

    @FXML
    private Label users_count; // Label for users count
    @FXML
    private Label users_count1; // Label for flights count
    @FXML
    private Label users_count11; // Label for airlines count
    @FXML
    private Label users_count111; // Label for reservations count
    @FXML
    private Label users_count1111; // Label for hotels count
    @FXML
    private Label users_count11111; // Label for offers count

    private UserService userService = new UserService();
    private FlightService flightService = new FlightService();
    private AirLineService airLineService = new AirLineService();
    private ReservationService reservationService = new ReservationService();
    private OfferService offerService = new OfferService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Fetch and display counts
        updateCounts();
    }

    private void updateCounts() {
        // Fetch counts from services
        int userCount = userService.getAllUsers().size();
        int flightCount = flightService.getall().size();
        int airlineCount = airLineService.getall().size();
        int reservationCount = reservationService.getAllReservations().size();
        int offerCount = offerService.getAllOffers().size();

        // Update the labels with the counts
        users_count.setText(String.valueOf(userCount));
        users_count1.setText(String.valueOf(flightCount));
        users_count11.setText(String.valueOf(airlineCount));
        users_count111.setText(String.valueOf(reservationCount));
        users_count11111.setText(String.valueOf(offerCount));
    }
}