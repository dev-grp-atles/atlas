package tn.esprit.atlas.controllers.hotel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import tn.esprit.atlas.controllers.review.AddReviewController;
import tn.esprit.atlas.entities.Hotel;
import tn.esprit.atlas.services.HotelService;
import javafx.beans.binding.Bindings;
import java.util.stream.Collectors;

import java.io.IOException;
import java.util.*;

public class ViewHotelsClientSideController {

    @FXML private GridPane hotelsGrid;
    @FXML private Label headerLabel;

    @FXML private TextField searchField;

    // Filter components
    @FXML private CheckBox fiveStar;
    @FXML private CheckBox fourStar;
    @FXML private CheckBox threeStar;
    @FXML private CheckBox wifiCheck;
    @FXML private CheckBox poolCheck;
    @FXML private CheckBox tvCheck;
    @FXML private CheckBox acCheck;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private TextField minRoomsField;
    @FXML
    private HBox searchContainer;

    private List<Hotel> hotels;
    private final HotelService hotelService = new HotelService();

    @FXML
    public void initialize() {
        setupInputValidation();
        refreshHotelData();
        hotelsGrid.setFocusTraversable(false);

        // listener to the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterHotelsByName(newValue);
        });

        searchField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                searchContainer.getStyleClass().add("search-focused");
            } else {
                searchContainer.getStyleClass().remove("search-focused");
            }
        });
    }

    private void filterHotelsByName(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            displayHotels();
            return;
        }

        List<Hotel> filtered = hotels.stream()
                .filter(h -> h.getName().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());

        setHotels(filtered);
    }

    private void setupInputValidation() {
        // Numeric validation for price fields
        minPriceField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) minPriceField.setText(oldVal);
        });

        maxPriceField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) maxPriceField.setText(oldVal);
        });

        // Numeric validation for rooms field
        minRoomsField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) minRoomsField.setText(oldVal);
        });
    }

    @FXML
    private void handleApplyFilters() {
        try {
            Map<String, Object> filters = new HashMap<>();

            // Star Rating
            List<Float> ratings = new ArrayList<>();
            if (fiveStar.isSelected()) ratings.add(4.5f);
            if (fourStar.isSelected()) ratings.add(3.5f);
            if (threeStar.isSelected()) ratings.add(2.5f);
            if (!ratings.isEmpty()) filters.put("minRating", Collections.min(ratings));

            // Price Range
            if (!maxPriceField.getText().isEmpty()) {
                double maxPrice = Double.parseDouble(maxPriceField.getText());
                if (maxPrice > 0) filters.put("maxPrice", maxPrice);
            }

            // Amenities
            List<String> amenities = new ArrayList<>();
            if (wifiCheck.isSelected()) amenities.add("Free WiFi");
            if (poolCheck.isSelected()) amenities.add("Swimming Pool");
            if (tvCheck.isSelected()) amenities.add("TV");
            if (acCheck.isSelected()) amenities.add("Air Conditioning");
            if (!amenities.isEmpty()) filters.put("facilities", amenities);

            // Minimum Rooms
            if (!minRoomsField.getText().isEmpty()) {
                int minRooms = Integer.parseInt(minRoomsField.getText());
                if (minRooms > 0) filters.put("minRooms", minRooms);
            }

            List<Hotel> filtered = hotelService.advancedSearch(filters);
            setHotels(filtered);

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numerical values", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Filter Error", "Failed to apply filters: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleResetFilters() {
        fiveStar.setSelected(false);
        fourStar.setSelected(false);
        threeStar.setSelected(false);
        wifiCheck.setSelected(false);
        poolCheck.setSelected(false);
        tvCheck.setSelected(false);
        acCheck.setSelected(false);
        minPriceField.clear();
        maxPriceField.clear();
        minRoomsField.clear();
        refreshHotelData();
    }

    private void refreshHotelData() {
        this.hotels = hotelService.getAll();
        displayHotels();
    }

    private void displayHotels() {
        hotelsGrid.getChildren().clear();

        if (hotels == null || hotels.isEmpty()) {
            headerLabel.setText("No properties found");
            return;
        }

        String city = !hotels.isEmpty() ? hotels.get(0).getCity() : "";
        headerLabel.setText(String.format("%s: %d properties found", city, hotels.size()));

        int row = 0;
        int col = 0;
        final int COLUMNS = 3;

        for (Hotel hotel : hotels) {
            VBox hotelCard = createHotelCard(hotel);
            hotelsGrid.add(hotelCard, col % COLUMNS, row);
            col++;
            if (col % COLUMNS == 0) row++;
        }
    }

    private VBox createHotelCard(Hotel hotel) {
        VBox card = new VBox();
        card.getStyleClass().add("hotel-card");

        // Action Icons
        HBox actionBox = new HBox(10);
        actionBox.getStyleClass().add("action-icons");

        // Favorite Button - standard size
        Button favoriteButton = createIconButton("/tn/esprit/atlas/assets/icons/save.png", 22, 28);
        favoriteButton.setOnAction(_ -> handleFavoriteHotel(hotel));

        // Add Review Button - standard size
        Button addReviewButton = createIconButton("/tn/esprit/atlas/assets/icons/plusIcon.png", 22, 28);
        addReviewButton.setOnAction(_ -> handleAddReview(hotel));

        // Star Icon Button - larger width, smaller height
        Button viewReviewsButton = createIconButton("/tn/esprit/atlas/assets/icons/star-icon.png", 28, 28);
        viewReviewsButton.setOnAction(_ -> handleViewReviews(hotel));

        actionBox.getChildren().addAll(favoriteButton, addReviewButton, viewReviewsButton);


        // Header Section
        HBox headerBox = new HBox(15);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        Label titleLabel = new Label(hotel.getName());
        titleLabel.getStyleClass().add("hotel-title");
        Label ratingLabel = new Label(String.format("%.1f/5", hotel.getRating()));
        ratingLabel.getStyleClass().add("rating-label");
        headerBox.getChildren().addAll(titleLabel, ratingLabel);

        // Image Section with Clipping
        ImageView imageView = new ImageView();
        imageView.getStyleClass().add("hotel-image");

        try {
            Image image = hotel.getImageUrl() != null && !hotel.getImageUrl().isEmpty()
                    ? new Image(hotel.getImageUrl())
                    : new Image(getClass().getResourceAsStream("/tn/esprit/atlas/assets/default-hotel.jpg"));
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }

        // Apply clipping rectangle
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(imageView.fitWidthProperty());
        clip.heightProperty().bind(imageView.fitHeightProperty());
        clip.setArcWidth(16);
        clip.setArcHeight(16);
        imageView.setClip(clip);

        // Set image dimensions
        imageView.setFitWidth(380);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(false);

        // Location Section
        Label locationLabel = new Label(hotel.getAddress());
        locationLabel.getStyleClass().add("location-info");

        // Price Section
        VBox priceBox = new VBox(5);
        priceBox.getStyleClass().add("price-section");
        Label priceLabel = new Label(String.format("TND %.2f", hotel.getPricePerNight()));
        priceLabel.getStyleClass().add("price-label");
        Label roomsLabel = new Label(String.format("/night • %d rooms left", hotel.getAvailableRooms()));
        roomsLabel.getStyleClass().add("per-night");
        priceBox.getChildren().addAll(priceLabel, roomsLabel);

        card.getChildren().addAll(actionBox, headerBox, imageView, locationLabel, priceBox);

        return card;
    }


    private void handleViewReviews(Hotel hotel) {
        try {
            // Load the HotelReviews-view.fxml page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/review/HotelReviews-view.fxml"));
            Parent root = loader.load();

            // Pass the selected hotel to the HotelReviewsController (if needed)
            HotelReviewsController controller = loader.getController();
            controller.setHotel(hotel);  // Assuming you have a setter in your controller for the hotel

            // Open the HotelReviews view in a new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Reviews for " + hotel.getName());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open the reviews page: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Button createIconButton(String iconPath, double width, double height) {
        Button button = new Button();
        try {
            ImageView icon = new ImageView(new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream(iconPath))
            ));
            // Set custom dimensions based on parameters
            icon.setFitWidth(width);
            icon.setFitHeight(height);
            icon.setPreserveRatio(true); // Maintain aspect ratio
            button.setGraphic(icon);
        } catch (Exception e) {
            System.err.println("Error loading icon: " + iconPath);
            button.setText("!");
        }
        button.getStyleClass().add("icon-button");
        return button;
    }

    private void handleFavoriteHotel(Hotel hotel) {
        // Implement logic to add the hotel to the client's favorites
        System.out.println("Hotel added to favorites: " + hotel.getName());
    }

    private void handleAddReview(Hotel hotel) {
        try {
            // Load the AddReview view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/review/AddReview-view.fxml"));
            Parent root = loader.load();

            // Pass the selected hotel to the AddReviewController
            AddReviewController controller = loader.getController();
            controller.setHotel(hotel);

            // Open the AddReview view in a new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Review for " + hotel.getName());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); // Print the full stack trace
            showAlert("Error", "Could not open Add Review page: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
        displayHotels();
    }
}