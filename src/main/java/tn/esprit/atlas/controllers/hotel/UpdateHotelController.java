package tn.esprit.atlas.controllers.hotel;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.atlas.entities.Hotel;
import tn.esprit.atlas.services.HotelService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class UpdateHotelController {

    @FXML private TextField hotelNameField;
    @FXML private TextField hotelAddressField;
    @FXML private TextField hotelRatingField;
    @FXML private TextField hotelRoomsField;
    @FXML private TextField hotelRentField;
    @FXML private TextField hotelImageField;
    @FXML private ImageView imageView;
    @FXML private TextArea hotelFacilitiesField;
    @FXML private TextField checkInField;
    @FXML private TextField checkOutField;
    @FXML private TextField hotelContactField;
    @FXML private TextField hotelCityField;
    @FXML private TextField hotelLatitudeField;
    @FXML private TextField hotelLongitudeField;

    private Hotel hotel;
    private String newImagePath;
    private final HotelService hotelService = new HotelService();

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        populateFields();
    }

    private void populateFields() {
        if (hotel != null) {
            hotelNameField.setText(hotel.getName());
            hotelAddressField.setText(hotel.getAddress());
            hotelRatingField.setText(String.valueOf(hotel.getRating()));
            hotelRoomsField.setText(String.valueOf(hotel.getAvailableRooms()));
            hotelRentField.setText(String.valueOf(hotel.getPricePerNight()));
            hotelImageField.setText(hotel.getImageUrl()); // Populate image URL
            hotelFacilitiesField.setText(String.join(",", hotel.getFacilities()));
            checkInField.setText(hotel.getCheckInTime());
            checkOutField.setText(hotel.getCheckOutTime());
            hotelContactField.setText(hotel.getContactNumber());
            hotelCityField.setText(hotel.getCity());
            hotelLatitudeField.setText(String.valueOf(hotel.getLatitude()));
            hotelLongitudeField.setText(String.valueOf(hotel.getLongitude()));

            if (hotel.getImageUrl() != null && !hotel.getImageUrl().isEmpty()) {
                imageView.setImage(new Image(hotel.getImageUrl()));
            }
        }
    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Hotel Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());
        if (selectedFile != null) {
            try {
                Path destDir = Paths.get("src/main/resources/tn/esprit/atlas/assets/hotel_images");
                if (!Files.exists(destDir)) {
                    Files.createDirectories(destDir);
                }

                Path source = selectedFile.toPath();
                Path destination = destDir.resolve(selectedFile.getName());
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);

                newImagePath = "file:" + destination.toString().replace("\\", "/");
                imageView.setImage(new Image(newImagePath));
                hotelImageField.setText(newImagePath); // Update text field

            } catch (IOException e) {
                showAlert("Error uploading image", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleUpdateHotel() {
        try {
            // Update hotel properties
            hotel.setName(hotelNameField.getText());
            hotel.setAddress(hotelAddressField.getText());
            hotel.setRating(Float.parseFloat(hotelRatingField.getText()));
            hotel.setAvailableRooms(Integer.parseInt(hotelRoomsField.getText()));
            hotel.setPricePerNight(Double.parseDouble(hotelRentField.getText()));
            hotel.setFacilities(List.of(hotelFacilitiesField.getText().split(",")));
            hotel.setCheckInTime(checkInField.getText());
            hotel.setCheckOutTime(checkOutField.getText());
            hotel.setContactNumber(hotelContactField.getText());
            hotel.setCity(hotelCityField.getText());
            hotel.setLatitude(Double.parseDouble(hotelLatitudeField.getText()));
            hotel.setLongitude(Double.parseDouble(hotelLongitudeField.getText()));

            // Image URL handling (manual input takes priority)
            if (!hotelImageField.getText().isEmpty()) {
                hotel.setImageUrl(hotelImageField.getText());
            } else if (newImagePath != null) {
                hotel.setImageUrl(newImagePath);
            }

            hotelService.update(hotel);
            Stage stage = (Stage) hotelNameField.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numerical values", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Update Error", "Failed to update hotel: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}