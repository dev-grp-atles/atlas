package tn.esprit.atlas.controllers.user.booking;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import tn.esprit.atlas.entities.Offer;
import tn.esprit.atlas.services.ReservationService;

import java.sql.Date;
import java.time.LocalDate;

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
    private ComboBox<String> roomTypeComboBox; // ComboBox for Room Type
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
    private ComboBox<String> viewComboBox; // ComboBox for View
    @FXML
    private TextField commentsField;
    @FXML
    private Label totalAmountLabel; // Label to display the total amount

    private Offer offer;
    private StackPane bookingContent;

    // Initialize the ComboBoxes with allowed values
    @FXML
    private void initialize() {
        // Initialize Room Type ComboBox
        roomTypeComboBox.getItems().addAll("single", "double", "suite");
        roomTypeComboBox.setValue("single"); // Set a default value

        // Initialize View ComboBox
        viewComboBox.getItems().addAll("mer", "piscine");
        viewComboBox.setValue("mer"); // Set a default value
    }

    // Set the offer
    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    // Set the StackPane to load views
    public void setBookingContent(StackPane bookingContent) {
        this.bookingContent = bookingContent;
    }

    // Handle the Submit button click event
    @FXML
    private void handleSubmitButtonClick() {
        // Collect data from the form
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String roomType = roomTypeComboBox.getValue(); // Get the selected value from the ComboBox
        int numberOfPeople = Integer.parseInt(numberOfPeopleField.getText());
        LocalDate arrivalDate = arrivalDatePicker.getValue();
        LocalDate departureDate = departureDatePicker.getValue();
        boolean breakfastIncluded = breakfastCheckBox.isSelected();
        boolean extraBed = extraBedCheckBox.isSelected();
        String view = viewComboBox.getValue(); // Get the selected value from the ComboBox
        String comments = commentsField.getText();

        // Calculate the number of nights
        int numberOfNights = (int) arrivalDate.until(departureDate).getDays();

        // Calculate the total amount (you can customize this logic based on your pricing)
        double totalAmount = offer.getPrice() * numberOfNights;

        // Display the total amount in the form
        totalAmountLabel.setText(String.format("%.2f DT", totalAmount));

        // Create a new reservation
        ReservationService reservationService = new ReservationService();
        reservationService.createReservation(firstName, lastName, email, phone, roomType, numberOfPeople, Date.valueOf(arrivalDate), Date.valueOf(departureDate), numberOfNights, breakfastIncluded, extraBed, view, comments, totalAmount);

        // Optionally, you can show a confirmation message or navigate back to the previous view
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reservation Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Your reservation has been successfully submitted!");
        alert.showAndWait();

        // Navigate back to the previous view
        bookingContent.getChildren().clear();
    }
}