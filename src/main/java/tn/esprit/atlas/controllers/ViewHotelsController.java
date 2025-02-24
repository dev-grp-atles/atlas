package tn.esprit.atlas.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import tn.esprit.atlas.entities.Hotel;
import tn.esprit.atlas.services.HotelService;

import java.util.List;
import java.util.Optional;

public class ViewHotelsController {

    @FXML
    private GridPane hotelsGrid;
    @FXML
    private Label headerLabel;

    private List<Hotel> hotels;
    private HotelService hotelService = new HotelService(); // Initialize HotelService

    public void initialize() {
        // Disable focus traversal for the grid and its children
        hotelsGrid.setFocusTraversable(false);
        for (javafx.scene.Node node : hotelsGrid.getChildren()) {
            node.setFocusTraversable(false);
        }
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
        displayHotels();
    }

    private void displayHotels() {
        try {
            hotelsGrid.getChildren().clear();

            if (hotels == null || hotels.isEmpty()) {
                headerLabel.setText("No properties found");
                return;
            }

            String city = hotels.get(0).getCity();
            headerLabel.setText(String.format("%s: %d properties found", city, hotels.size()));

            int row = 0;
            int col = 0;

            for (Hotel hotel : hotels) {
                VBox hotelCard = createHotelCard(hotel);
                hotelsGrid.add(hotelCard, col, row);

                col = (col + 1) % 3;
                if (col == 0) row++;
            }
        } catch (Exception e) {
            System.err.println("Error displaying hotels: " + e.getMessage());
            headerLabel.setText("Error loading hotels");
        }
    }

    private VBox createHotelCard(Hotel hotel) {
        VBox card = new VBox();
        card.getStyleClass().add("hotel-card");

        // Header Section
        HBox headerBox = new HBox();
        headerBox.setSpacing(15);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(hotel.getName());
        titleLabel.getStyleClass().add("hotel-title");

        Label ratingLabel = new Label(String.format("%.1f/5", hotel.getRating()));
        ratingLabel.getStyleClass().add("rating-label");

        headerBox.getChildren().addAll(titleLabel, ratingLabel);

        // Image Section with Clipping for Rounded Corners
        ImageView imageView = new ImageView();
        if (hotel.getImageUrl() != null) {
            imageView.setImage(new Image(hotel.getImageUrl()));
        }
        imageView.setFitWidth(300);
        imageView.setFitHeight(180);

        // Clipping Rectangle for rounded corners
        Rectangle clip = new Rectangle(300, 180);
        clip.setArcWidth(16); // Adjust for smoother rounding
        clip.setArcHeight(16);
        imageView.setClip(clip);

        // Location Section
        Label locationLabel = new Label(hotel.getAddress());
        locationLabel.getStyleClass().add("location-info");

        // Price Section
        VBox priceBox = new VBox();
        priceBox.getStyleClass().add("price-section");

        Label priceLabel = new Label(String.format("TND %.2f", hotel.getPricePerNight()));
        priceLabel.getStyleClass().add("price-label");

        Label roomsLabel = new Label(String.format("/night • %d rooms left", hotel.getAvailableRooms()));
        roomsLabel.getStyleClass().add("per-night");

        priceBox.getChildren().addAll(priceLabel, roomsLabel);

        // Action Icons
        HBox actionBox = new HBox();
        actionBox.getStyleClass().add("action-icons");

        Button editButton = createIconButton("/tn/esprit/atlas/assets/icons/editIcon.png");
        Button deleteButton = createIconButton("/tn/esprit/atlas/assets/icons/deleteIcon.png");

        // Add event handler for delete button
        deleteButton.setOnAction(event -> handleDeleteHotel(hotel));

        actionBox.getChildren().addAll(editButton, deleteButton);

        // Assemble Card
        card.getChildren().addAll(
                actionBox,
                headerBox,
                imageView,
                locationLabel,
                priceBox
        );

        return card;
    }

    private Button createIconButton(String iconPath) {
        Button button = new Button();
        try {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
            icon.setFitWidth(20);
            icon.setFitHeight(20);
            button.setGraphic(icon);
        } catch (Exception e) {
            System.err.println("Error loading icon: " + iconPath);
            button.setText("!");
        }
        button.getStyleClass().add("icon-button");
        return button;
    }

    private void handleEditHotel(Hotel hotel) {
        System.out.println("Editing hotel: " + hotel.getName());
    }

    private void handleDeleteHotel(Hotel hotel) {
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Hotel");
        alert.setHeaderText("Are you sure you want to delete this hotel?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed deletion
            hotelService.delete(hotel); // Call the delete method from HotelService
            System.out.println("Hotel deleted: " + hotel.getName());

            // Refresh the hotel list after deletion
            hotels.remove(hotel); // Remove from the local list
            displayHotels(); // Refresh the UI
        }
    }
}