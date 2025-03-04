package tn.esprit.atlas.controllers.hotel;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import tn.esprit.atlas.entities.Hotel;
import tn.esprit.atlas.entities.Review;
import tn.esprit.atlas.services.ReviewService;

import java.util.List;

public class HotelReviewsController {

    @FXML private Label hotelNameLabel;
    @FXML private VBox reviewsListContainer;

    private Hotel hotel;
    private final ReviewService reviewService;

    public HotelReviewsController() {
        reviewService = new ReviewService();
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        if (hotel != null) {
            hotelNameLabel.setText(hotel.getName());

            List<Review> reviews = reviewService.getReviewsByHotelId(hotel.getId());

            // Debugging
            System.out.println("Fetched reviews for hotel: " + hotel.getName());
            System.out.println("Reviews count: " + reviews.size());

            loadReviews(reviews);
        }
    }

    private void loadReviews(List<Review> reviews) {
        reviewsListContainer.getChildren().clear();

        if (reviews == null || reviews.isEmpty()) {
            Label noReviewsLabel = new Label("No reviews available.");
            noReviewsLabel.getStyleClass().add("no-reviews-label");
            reviewsListContainer.getChildren().add(noReviewsLabel);
        } else {
            for (Review review : reviews) {
                ReviewItem reviewItem = new ReviewItem(review);
                VBox.setMargin(reviewItem, new Insets(10, 0, 10, 0));
                reviewsListContainer.getChildren().add(reviewItem);
            }
        }
    }

    // Custom ReviewItem component with Edit & Delete buttons
    private class ReviewItem extends VBox {
        private final Label reviewerName = new Label();
        private final Label reviewComment = new Label();
        private final Label reviewRating = new Label();
        private final Button editButton = createIconButton("/tn/esprit/atlas/assets/icons/editIcon.png");
        private final Button deleteButton = createIconButton("/tn/esprit/atlas/assets/icons/deleteIcon.png");
        private final Review review;

        public ReviewItem(Review review) {
            this.review = review;

            reviewerName.setText(review.getUser() != null ? review.getUser().getName() : review.getReviewerName());
            reviewComment.setText(review.getComment());
            reviewRating.setText("Rating: " + review.getRating() + " stars");

            // Style classes
            reviewerName.getStyleClass().add("reviewer-name");
            reviewComment.getStyleClass().add("review-comment");
            reviewRating.getStyleClass().add("review-rating");

            // Button actions
            editButton.setOnAction(event -> editReview());
            deleteButton.setOnAction(event -> deleteReview());

            // Create button container (aligned top-right)
            HBox buttonContainer = new HBox(10, editButton, deleteButton);
            HBox.setHgrow(buttonContainer, Priority.ALWAYS);
            buttonContainer.setStyle("-fx-alignment: top-right;");

            // Layout
            this.setSpacing(10);
            this.setPadding(new Insets(15));
            this.getStyleClass().add("review-item");
            this.getChildren().addAll(buttonContainer, reviewerName, reviewComment, reviewRating);
        }

        private Button createIconButton(String iconPath) {
            Button button = new Button();
            button.setStyle("-fx-background-color: transparent;");
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
            icon.setFitWidth(16);
            icon.setFitHeight(16);
            button.setGraphic(icon);
            return button;
        }

        private void editReview() {
            TextInputDialog dialog = new TextInputDialog(review.getComment());
            dialog.setTitle("Edit Review");
            dialog.setHeaderText("Modify the review comment:");
            dialog.setContentText("New Comment:");

            dialog.showAndWait().ifPresent(newComment -> {
                review.setComment(newComment);
                reviewService.update(review);
                reviewComment.setText(newComment);
            });
        }

        private void deleteReview() {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete this review?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    reviewService.delete(review);
                    reviewsListContainer.getChildren().remove(this);
                }
            });
        }
    }
}
