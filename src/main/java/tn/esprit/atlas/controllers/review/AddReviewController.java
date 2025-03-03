package tn.esprit.atlas.controllers.review;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import tn.esprit.atlas.entities.Hotel;
import tn.esprit.atlas.entities.Review;
import tn.esprit.atlas.services.ReviewService;
import tn.esprit.atlas.utils.UserSession;

public class AddReviewController {

    @FXML private TextArea reviewCommentField;
    @FXML private Spinner<Integer> ratingSpinner;

    private Hotel hotel; // Selected hotel

    @FXML
    public void initialize() {
        // Initialize the spinner with a range of 1 to 5 and a default value of 5
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 5);
        ratingSpinner.setValueFactory(valueFactory);
    }

    /**
     * Sets the hotel for which the review is being added.
     */
    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @FXML
    private void handleSubmitReview() {
        String reviewComment = reviewCommentField.getText().trim();
        int rating = ratingSpinner.getValue();

        // Validate inputs
        if (reviewComment.isEmpty()) {
            showAlert("Input Error", "Please write your review.", Alert.AlertType.ERROR);
            return;
        }

        // Get the logged-in user's name from UserSession
        String reviewerName = UserSession.getUser().getUsername(); // Assuming User has a getUsername() method

        // Create a new Review object
        Review review = new Review();
        review.setReviewerName(reviewerName);
        review.setComment(reviewComment);
        review.setRating(rating);
        review.setHotelId(hotel.getId()); // Associate the review with the selected hotel

        try {
            // Save the review using the ReviewService
            ReviewService reviewService = new ReviewService();
            reviewService.add(review);
            showAlert("Success", "Review submitted successfully!", Alert.AlertType.INFORMATION);

            // Close the window after successful submission
            closeWindow();
        } catch (Exception e) {
            showAlert("Error", "Failed to submit review: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) reviewCommentField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}