package tn.esprit.atlas.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import tn.esprit.atlas.entities.Hotel;

import java.util.List;

public class ViewHotelsController {

    @FXML
    private GridPane hotelGrid;

    // This method will be called to set the hotel details dynamically
    public void setHotelDetails(List<Hotel> hotels) {
        // Clear any existing content from the GridPane
        hotelGrid.getChildren().clear();

        int row = 0;
        int column = 0;

        // Iterate over the list of hotels
        for (Hotel hotel : hotels) {
            // Create a VBox for each hotel card
            VBox hotelCard = createHotelCard(hotel);

            // Add the hotel card to the grid at the correct position
            hotelGrid.add(hotelCard, column, row);

            // Move to the next column or row (wrap to next row when necessary)
            column++;
            if (column > 2) { // Assuming you want 3 cards per row
                column = 0;
                row++;
            }
        }
    }

    // Create a card for each hotel
    private VBox createHotelCard(Hotel hotel) {
        // Create the image for the card (assuming you have a method to get a hotel image)
        ImageView imageView = new ImageView(new Image(hotel.getImageUrl()));  // Replace with actual image URL or path
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);

        // Create the name label
        Label nameLabel = new Label(hotel.getName());

        // Create the address label
        Label addressLabel = new Label(hotel.getAddress());

        // Create the rating label
        Label ratingLabel = new Label("Rating: " + hotel.getRating());

        // Create the price per night label
        Label priceLabel = new Label("Price: $" + hotel.getPricePerNight());

        // Create a VBox to hold the image and text
        VBox hotelCard = new VBox(imageView, nameLabel, addressLabel, ratingLabel, priceLabel);
        hotelCard.setSpacing(10);

        // Add some basic styling to the VBox to make it look like a card
        hotelCard.setStyle("-fx-border-color: #ccc; -fx-padding: 10; -fx-background-color: #fff;");

        return hotelCard;
    }
}
