package tn.esprit.atlas.controllers.user.booking;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import tn.esprit.atlas.entities.Offer;

import java.io.File;

public class OfferDetailsController {

    @FXML
    private Label nameLabel; // Offer name
    @FXML
    private Label destinationsLabel; // Offer destinations
    @FXML
    private Label durationLabel; // Offer duration
    @FXML
    private Label priceLabel; // Offer price
    @FXML
    private Label descriptionLabel; // Offer description
    @FXML
    private ImageView offerImage; // Offer image
    @FXML
    private Label offerName; // Offer name (right side)
    @FXML
    private Button reserveButton; // Reserve button

    private Offer offer; // The selected offer
    private StackPane bookingContent; // StackPane to load views

    // Set the offer details to display
    public void setOffer(Offer offer) {
        this.offer = offer;
        updateUI();
    }

    // Set the StackPane to load views
    public void setBookingContent(StackPane bookingContent) {
        this.bookingContent = bookingContent;
    }

    private void updateUI() {
        if (offer != null) {
            nameLabel.setText(offer.getName());
            destinationsLabel.setText("Destinations: " + offer.getDestinations());
            durationLabel.setText("Duration: " + offer.getDuration() + " Days");
            priceLabel.setText("Price: " + offer.getPrice() + " DT");
            descriptionLabel.setText(offer.getDescription());

            // Load the image
            if (offer.getPackageImage() != null && !offer.getPackageImage().isEmpty()) {
                try {
                    // Convert file path to URL if necessary
                    if (offer.getPackageImage().startsWith("C:")) {
                        // If the path starts with "C:", treat it as a file path
                        String imagePath = new File(offer.getPackageImage()).toURI().toString();
                        Image image = new Image(imagePath);
                        offerImage.setImage(image);
                    } else {
                        // Treat it as a URL or classpath resource
                        Image image = new Image(offer.getPackageImage());
                        offerImage.setImage(image);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid image URL: " + offer.getPackageImage());
                    // Set a default image if the provided image is invalid
                    setDefaultImage();
                }
            } else {
                // Set a default image if no image is provided
                setDefaultImage();
            }

            offerName.setText(offer.getName());
        }
    }

    // Helper method to set the default image
    private void setDefaultImage() {
        try {
            Image defaultImage = new Image(getClass().getResourceAsStream("/tn/esprit/atlas/assets/default-image.png"));
            offerImage.setImage(defaultImage);
        } catch (NullPointerException e) {
            System.err.println("Default image not found in the classpath.");
            // Optionally, set a placeholder or leave the ImageView empty
            offerImage.setImage(null);
        }
    }

    // Handle the Reserve button click event
    @FXML
    private void handleReserveButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/user/booking/reservation-view.fxml"));
            Parent reservationForm = loader.load();

            // Pass the offer and bookingContent to the ReservationFormController
            ReservationFormController reservationFormController = loader.getController();
            reservationFormController.setOffer(offer);
            reservationFormController.setBookingContent(bookingContent);

            // Replace the current content with the reservation form
            bookingContent.getChildren().setAll(reservationForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}