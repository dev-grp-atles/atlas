package tn.esprit.atlas.controllers.user.booking;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import tn.esprit.atlas.entities.FlightReservation;
import tn.esprit.atlas.entities.User;
import tn.esprit.atlas.services.FlightReservationService;
import tn.esprit.atlas.utils.UserSession;

import java.time.LocalDateTime;

public class FlightReservationFormController {

    @FXML
    private TextField passengerNameField;
    @FXML
    private TextField passengerEmailField;
    @FXML
    private TextField passengerPhoneField;
    @FXML
    private TextField numberOfPassengersField;
    @FXML
    private TextField specialRequestsField;

    private User user = UserSession.getUser(); // Get the current user from the session
    private int userId;
    private int volId; // The flight ID for which the reservation is being made
    private StackPane bookingContent; // StackPane to load views

    private FlightReservationService reservationService = new FlightReservationService();

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Set the flight ID for which the reservation is being made
    public void setVolId(int volId) {
        this.volId = volId;
    }

    // Set the StackPane to load views
    public void setBookingContent(StackPane bookingContent) {
        this.bookingContent = bookingContent;
    }

    @FXML
    private void handleReservationSubmit() {
        // Collect data from the form
        String passengerName = passengerNameField.getText();
        String passengerEmail = passengerEmailField.getText();
        String passengerPhone = passengerPhoneField.getText();
        int numberOfPassengers = Integer.parseInt(numberOfPassengersField.getText());
        String specialRequests = specialRequestsField.getText();

        // Create a new FlightReservation object
        FlightReservation reservation = new FlightReservation();
        reservation.setUserId(user.getId()); // Use the current user's ID
        reservation.setVolId(volId); // Set the flight ID
        reservation.setPassengerName(passengerName);
        reservation.setPassengerEmail(passengerEmail);
        reservation.setPassengerPhone(passengerPhone);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setNumberOfPassengers(numberOfPassengers);
        reservation.setTotalPrice(0.0); // You can calculate the total price based on the flight and number of passengers
        reservation.setPaymentStatus("Pending");
        reservation.setReservationStatus("Pending");
        reservation.setSpecialRequests(specialRequests);

        // Add the reservation to the database
        reservationService.addFlightReservation(reservation);

        // Show a success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reservation Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Your flight reservation has been successfully submitted!");
        alert.showAndWait();

        // Navigate back to the previous view
        // bookingContent.getChildren().clear();
        passengerNameField.clear();
        passengerEmailField.clear();
        passengerPhoneField.clear();
        numberOfPassengersField.clear();
        specialRequestsField.clear();
    }
}