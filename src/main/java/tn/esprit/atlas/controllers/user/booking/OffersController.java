package tn.esprit.atlas.controllers.user.booking;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import tn.esprit.atlas.entities.Offer;
import tn.esprit.atlas.services.OfferService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OffersController implements Initializable {

    @FXML
    private GridPane offersGrid; // Use GridPane instead of VBox

    private OfferService offerService = new OfferService();
    private StackPane bookingContent; // Reference to the main booking content area

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadOffers(); // Load offers when the view is initialized
    }

    // Set the bookingContent
    public void setBookingContent(StackPane bookingContent) {
        this.bookingContent = bookingContent;
    }

    private void loadOffers() {
        List<Offer> offers = offerService.getAllOffers(); // Fetch all offers

        // Clear the container before adding new offers
        offersGrid.getChildren().clear();

        // Display each offer in a card format
        int column = 0;
        int row = 0;
        for (Offer offer : offers) {
            // Create a card container (VBox)
            VBox offerCard = new VBox(); // No spacing, we'll handle padding in styles
            offerCard.setStyle(
                    "-fx-background-color: #ffffff; " + // White background
                            "-fx-background-radius: 5px; " + // Less rounded corners
                            "-fx-padding: 10px;" // Padding for the entire card
            );
            offerCard.setPrefWidth(250); // Set fixed width for the card
            offerCard.setPrefHeight(350); // Set fixed height for the card

            // Add an image (if available)
            ImageView imageView = new ImageView();
            imageView.setFitWidth(230); // Image takes the full width of the card minus padding
            imageView.setFitHeight(130); // Fixed height for the image
            imageView.setPreserveRatio(false); // Stretch the image to fit the width
            imageView.setStyle(
                    "-fx-border-radius: 5px 5px 0 0;" // Rounded upper corners (less rounded)
            );

            // Load the image (ensure it's a valid URL or file path)
            String packageImage = offer.getPackageImage();
            if (packageImage != null && !packageImage.isEmpty()) {
                try {
                    // Convert file path to URL if necessary
                    if (packageImage.startsWith("C:")) {
                        // If the path starts with "C:", treat it as a file path
                        packageImage = new File(packageImage).toURI().toString();
                    }
                    Image image = new Image(packageImage);
                    imageView.setImage(image);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid image URL: " + packageImage);
                    // Optionally, set a default image
                    imageView.setImage(new Image(getClass().getResourceAsStream("/tn/esprit/atlas/assets/default-image.png")));
                }
            } else {
                // Set a default image if no image is provided
                imageView.setImage(new Image(getClass().getResourceAsStream("/tn/esprit/atlas/assets/default-image.png")));
            }

            // Add the image to the card
            offerCard.getChildren().add(imageView);

            // Add offer name
            Label nameLabel = new Label(offer.getName());
            nameLabel.setStyle(
                    "-fx-font-size: 16px; " + // Font size
                            "-fx-font-weight: bold; " + // Bold text
                            "-fx-padding: 10px 0 0 0;" // Padding on the top
            );
            nameLabel.setFont(Font.font("Segoe UI", 16)); // Modern font (Segoe UI)
            offerCard.getChildren().add(nameLabel);

            // Add destinations
            Label destinationsLabel = new Label(offer.getDestinations());
            destinationsLabel.setStyle(
                    "-fx-font-size: 14px; " + // Font size
                            "-fx-padding: 5px 0 0 0;" // Padding on the top
            );
            destinationsLabel.setFont(Font.font("Segoe UI", 14)); // Modern font (Segoe UI)
            offerCard.getChildren().add(destinationsLabel);

            // Add duration below the destination
            Label durationLabel = new Label(offer.getDuration() + " Days"); // Duration in days
            durationLabel.setStyle(
                    "-fx-font-size: 14px; " + // Font size
                            "-fx-padding: 5px 0 10px 0;" // Padding on the top and bottom
            );
            durationLabel.setFont(Font.font("Segoe UI", 14)); // Modern font (Segoe UI)
            offerCard.getChildren().add(durationLabel);

            // Add price above the button
            Label priceLabel = new Label(offer.getPrice() + " DT"); // Price in DT
            priceLabel.setStyle(
                    "-fx-font-size: 16px; " + // Font size
                            "-fx-font-weight: bold; " + // Bold text
                            "-fx-padding: 10px 0 5px 0;" // Padding on the top and bottom
            );
            priceLabel.setFont(Font.font("Segoe UI", 16)); // Modern font (Segoe UI)
            offerCard.getChildren().add(priceLabel);

            // Add a spacer to push the button to the bottom
            VBox spacer = new VBox();
            VBox.setVgrow(spacer, Priority.ALWAYS); // Spacer takes up remaining space
            offerCard.getChildren().add(spacer);

            // Add a "View Offer" button
            Button viewOfferButton = new Button("View Offer");
            viewOfferButton.setStyle(
                    "-fx-background-color: #4CAF50; " + // Green background
                            "-fx-text-fill: white; " + // White text
                            "-fx-font-size: 14px; " + // Font size
                            "-fx-padding: 10px; " + // Padding
                            "-fx-max-width: Infinity; " + // Take the full width
                            "-fx-background-radius: 5px; " + // Rounded corners
                            "-fx-border-radius: 5px;" // Rounded corners
            );
            viewOfferButton.setFont(Font.font("Segoe UI", 14)); // Modern font (Segoe UI)
            viewOfferButton.setOnAction(event -> {
                // Handle the "View Offer" button action here
                loadOfferDetails(offer);
            });

            // Add the button to the card
            offerCard.getChildren().add(viewOfferButton);

            // Add the card to the grid
            offersGrid.add(offerCard, column, row);

            // Update column and row for the next card
            column++;
            if (column == 3) { // 3 cards per row
                column = 0;
                row++;
            }
        }
    }

    // Load the offer details view
    private void loadOfferDetails(Offer offer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/user/booking/offerdetails-view.fxml"));
            Parent offerDetailsView = loader.load();

            // Pass the selected offer and bookingContent to the OfferDetailsController
            OfferDetailsController offerDetailsController = loader.getController();
            offerDetailsController.setOffer(offer);
            offerDetailsController.setBookingContent(bookingContent);

            // Replace the current content with the offer details view
            offersGrid.getChildren().setAll(offerDetailsView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}