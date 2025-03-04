package tn.esprit.atlas.controllers.user.booking;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import tn.esprit.atlas.entities.Offer;
import tn.esprit.atlas.services.ReservationService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class ReservationFormController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private ComboBox<String> roomTypeComboBox;
    @FXML
    private TextField numberOfPeopleField;
    @FXML
    private DatePicker arrivalDatePicker;
    @FXML
    private DatePicker departureDatePicker;
    @FXML
    private CheckBox breakfastCheckBox;
    @FXML
    private CheckBox extraBedCheckBox;
    @FXML
    private ComboBox<String> viewComboBox;
    @FXML
    private TextField commentsField;
    @FXML
    private Label totalAmountLabel;

    private Offer offer;
    private StackPane bookingContent;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{8,15}");

    @FXML
    private void initialize() {
        roomTypeComboBox.getItems().addAll("single", "double", "suite");
        roomTypeComboBox.setValue("single");
        viewComboBox.getItems().addAll("mer", "piscine");
        viewComboBox.setValue("mer");
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public void setBookingContent(StackPane bookingContent) {
        this.bookingContent = bookingContent;
    }

    @FXML
    private void handleSubmitButtonClick() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String numberOfPeopleText = numberOfPeopleField.getText().trim();
        LocalDate arrivalDate = arrivalDatePicker.getValue();
        LocalDate departureDate = departureDatePicker.getValue();
        String comments = commentsField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || numberOfPeopleText.isEmpty()) {
            showAlert("Input Error", "All fields except comments must be filled in.");
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showAlert("Input Error", "Please enter a valid email address.");
            return;
        }

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            showAlert("Input Error", "Please enter a valid phone number (8-15 digits).");
            return;
        }

        if (!NUMBER_PATTERN.matcher(numberOfPeopleText).matches()) {
            showAlert("Input Error", "Please enter a valid positive number of people.");
            return;
        }

        int numberOfPeople = Integer.parseInt(numberOfPeopleText);
        if (numberOfPeople <= 0) {
            showAlert("Input Error", "Number of people must be greater than zero.");
            return;
        }

        if (arrivalDate == null || departureDate == null) {
            showAlert("Input Error", "Please select both arrival and departure dates.");
            return;
        }

        if (arrivalDate.isBefore(LocalDate.now())) {
            showAlert("Input Error", "Arrival date cannot be in the past.");
            return;
        }

        if (departureDate.isBefore(arrivalDate)) {
            showAlert("Input Error", "Departure date must be after the arrival date.");
            return;
        }

        int numberOfNights = (int) arrivalDate.until(departureDate).getDays();
        double totalAmount = offer.getPrice() * numberOfNights;
        totalAmountLabel.setText(String.format("%.2f DT", totalAmount));

        ReservationService reservationService = new ReservationService();
        reservationService.createReservation(firstName, lastName, email, phone, roomTypeComboBox.getValue(), numberOfPeople,
                Date.valueOf(arrivalDate), Date.valueOf(departureDate), numberOfNights, breakfastCheckBox.isSelected(),
                extraBedCheckBox.isSelected(), viewComboBox.getValue(), comments, totalAmount);

        showAlert("Reservation Confirmation", "Your reservation has been successfully submitted!");
        bookingContent.getChildren().clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
