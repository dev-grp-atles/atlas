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
import java.util.regex.Pattern;

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

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{8,15}"); // Phone number must be 8-15 digits

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setVolId(int volId) {
        this.volId = volId;
    }

    public void setBookingContent(StackPane bookingContent) {
        this.bookingContent = bookingContent;
    }

    @FXML
    private void handleReservationSubmit() {
        // Collect data from the form
        String passengerName = passengerNameField.getText().trim();
        String passengerEmail = passengerEmailField.getText().trim();
        String passengerPhone = passengerPhoneField.getText().trim();
        String numberOfPassengersText = numberOfPassengersField.getText().trim();
        String specialRequests = specialRequestsField.getText().trim();

        // Validate required fields
        if (passengerName.isEmpty() || passengerEmail.isEmpty() || passengerPhone.isEmpty() || numberOfPassengersText.isEmpty()) {
            showAlert("Input Error", "All fields except special requests must be filled in.");
            return;
        }

        // Validate email format
        if (!EMAIL_PATTERN.matcher(passengerEmail).matches()) {
            showAlert("Input Error", "Please enter a valid email address.");
            return;
        }

        // Validate phone number format
        if (!PHONE_PATTERN.matcher(passengerPhone).matches()) {
            showAlert("Input Error", "Please enter a valid phone number (8-15 digits).");
            return;
        }

        // Validate number of passengers input
        if (!NUMBER_PATTERN.matcher(numberOfPassengersText).matches()) {
            showAlert("Input Error", "Please enter a valid positive number of passengers.");
            return;
        }

        int numberOfPassengers = Integer.parseInt(numberOfPassengersText);
        if (numberOfPassengers <= 0) {
            showAlert("Input Error", "Number of passengers must be greater than zero.");
            return;
        }

        // Create a new FlightReservation object
        FlightReservation reservation = new FlightReservation();
        reservation.setUserId(user.getId()); // Use the current user's ID
        reservation.setVolId(volId); // Set the flight ID
        reservation.setPassengerName(passengerName);
        reservation.setPassengerEmail(passengerEmail);
        reservation.setPassengerPhone(passengerPhone);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setNumberOfPassengers(numberOfPassengers);
        reservation.setTotalPrice(0.0); // Calculate price based on the flight and passengers
        reservation.setPaymentStatus("Pending");
        reservation.setReservationStatus("Pending");
        reservation.setSpecialRequests(specialRequests);

        // Add the reservation to the database
        reservationService.addFlightReservation(reservation);

        // Show a success message
        showAlert("Reservation Confirmation", "Your flight reservation has been successfully submitted!");

        // Clear the fields
        passengerNameField.clear();
        passengerEmailField.clear();
        passengerPhoneField.clear();
        numberOfPassengersField.clear();
        specialRequestsField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
