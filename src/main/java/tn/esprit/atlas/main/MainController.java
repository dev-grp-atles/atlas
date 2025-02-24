package tn.esprit.atlas.main;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import tn.esprit.atlas.controllers.ViewHotelsController;
import tn.esprit.atlas.services.HotelService;
import tn.esprit.atlas.entities.Hotel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.Node;
import java.io.File;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public TableColumn hotelAvailableRoomsColumn;
    public TableColumn hotelPricePerNightColumn;
    public TableColumn hotelFacilitiesColumn;
    public TableColumn hotelCheckInTimeColumn;
    public TableColumn hotelCheckOutTimeColumn;
    public TableColumn hotelContactNumberColumn;
    public TableView hotelTableView;
    @FXML
    private Button switchToSignInButton;
    @FXML
    private Button switchToReservationButton;
    @FXML
    private Button switchToVolsButton;
    @FXML
    private Button switchToAirlinesButton;

    // Hotel Management Buttons
    @FXML
    private Button addHotelButton;
    @FXML
    private Button updateHotelButton;
    @FXML
    private Button deleteHotelButton;
    @FXML
    private Button viewHotelsButton;

    // Review Management Buttons
    @FXML
    private Button addReviewButton;
    @FXML
    private Button updateReviewButton;
    @FXML
    private Button deleteReviewButton;
    @FXML
    private Button viewReviewsButton;

    @FXML
    private ImageView imageView;

    @FXML
    private File uploadedImageFile;

    // FXML fields for hotel form
    @FXML
    private TextField hotelNameField;
    @FXML
    private TextField hotelAddressField;
    @FXML
    private TextField hotelRatingField;
    @FXML
    private TextField hotelImageField;
    @FXML
    private TextField hotelRoomsField;
    @FXML
    private TextField hotelRentField;
    @FXML
    private TextArea hotelFacilitiesField;
    @FXML
    private TextField checkInField;
    @FXML
    private TextField checkOutField;
    @FXML
    private TextField hotelContactField;
    @FXML
    private TextField hotelCityField;
    @FXML
    private TextField hotelLatitudeField;
    @FXML
    private TextField hotelLongitudeField;

    @FXML
    private TableView<Hotel> hotelTable;

    @FXML
    private TableColumn<Hotel, String> hotelNameColumn;

    @FXML
    private TableColumn<Hotel, String> hotelAddressColumn;

    @FXML
    private TableColumn<Hotel, Float> hotelRatingColumn;

    @FXML
    private TableColumn<Hotel, String> hotelCityColumn;

    @FXML
    private TableColumn<Hotel, Double> hotelPriceColumn;

    @FXML
    private TableColumn<Hotel, String> hotelImageColumn;

    @FXML
    private TextField searchField;

    @FXML
    private Button viewDetailsButton;

    private HotelService hotelService = new HotelService();

    // Hotel form submission handler
    @FXML
    private void handleSubmitHotelForm() {
        try {
            // Retrieve values from the form fields
            String name = hotelNameField.getText();
            String address = hotelAddressField.getText();
            Hotel hotel = getHotel(name, address);

            // Add hotel using HotelService
            hotelService.add(hotel);

            // Show success message
            showAlert("Hotel Added", "Hotel added successfully!", AlertType.INFORMATION);

            // Clear the form fields after submission (optional)
            clearFormFields();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for rating, available rooms, price, latitude, and longitude.", AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to add hotel. Please try again.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private Hotel getHotel(String name, String address) {
        float rating = Float.parseFloat(hotelRatingField.getText());
        String imageUrl = hotelImageField.getText();
        int availableRooms = Integer.parseInt(hotelRoomsField.getText());
        double pricePerNight = Double.parseDouble(hotelRentField.getText());
        String facilities = hotelFacilitiesField.getText();
        String checkInTime = checkInField.getText();
        String checkOutTime = checkOutField.getText();
        String contactNumber = hotelContactField.getText();
        String city = hotelCityField.getText();
        double latitude = Double.parseDouble(hotelLatitudeField.getText());
        double longitude = Double.parseDouble(hotelLongitudeField.getText());

        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setAddress(address);
        hotel.setRating(rating);
        hotel.setImageUrl(imageUrl);
        hotel.setAvailableRooms(availableRooms);
        hotel.setPricePerNight(pricePerNight);
        hotel.setFacilities(List.of(facilities.split(",")));
        hotel.setCheckInTime(checkInTime);
        hotel.setCheckOutTime(checkOutTime);
        hotel.setContactNumber(contactNumber);
        hotel.setCity(city);
        hotel.setLatitude(latitude);
        hotel.setLongitude(longitude);
        return hotel;
    }

    // Method to show alert messages
    private void showAlert(String title, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Helper method to clear form fields (optional)
    private void clearFormFields() {
        hotelNameField.clear();
        hotelAddressField.clear();
        hotelRatingField.clear();
        hotelImageField.clear();
        hotelRoomsField.clear();
        hotelRentField.clear();
        hotelFacilitiesField.clear();
        checkInField.clear();
        checkOutField.clear();
        hotelContactField.clear();
        hotelCityField.clear();
        hotelLatitudeField.clear();
        hotelLongitudeField.clear();
    }

    // Navigation Methods
    @FXML
    private void switchToSignIn() throws IOException {
        Parent signInRoot = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/signin-view.fxml"));
        Scene currentScene = switchToSignInButton.getScene();
        currentScene.setRoot(signInRoot);
    }

    @FXML
    private void switchToReservation() throws IOException {
        Parent reservationRoot = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/reservation-view.fxml"));
        Scene currentScene = switchToReservationButton.getScene();
        currentScene.setRoot(reservationRoot);
    }

    @FXML
    private void switchToVols() throws IOException {
        Parent volsRoot = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/vols-view.fxml"));
        Scene currentScene = switchToVolsButton.getScene();
        currentScene.setRoot(volsRoot);
    }

    @FXML
    private void switchToAirlines() throws IOException {
        Parent airlinesRoot = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/airlines-view.fxml"));
        Scene currentScene = switchToAirlinesButton.getScene();
        currentScene.setRoot(airlinesRoot);
    }

    // Hotel Management Methods
    @FXML
    private void handleAddHotel() throws IOException {
        Parent addHotelRoot = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/hotel/add-hotel-view.fxml"));
        Scene addHotelScene = new Scene(addHotelRoot);
        Stage currentStage = (Stage) addHotelButton.getScene().getWindow();
        currentStage.setScene(addHotelScene);
        currentStage.show();
    }

    @FXML
    private void handleUpdateHotel() {
        System.out.println("Updating hotel...");
    }

    @FXML
    private void handleDeleteHotel() {
        System.out.println("Deleting hotel...");
    }

    @FXML
    private void handleViewHotels(ActionEvent event) {
        // Fetch the list of hotels from your service or database
        List<Hotel> hotels = hotelService.getAll(); // Assuming this fetches all hotels

        // Switch to the view-hotels-view.fxml and pass the hotels list
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/hotel/view-hotels-view.fxml"));
            Parent hotelsRoot = loader.load();

            ViewHotelsController controller = loader.getController();
            controller.setHotels(hotels);  // Use the correct method name

            Scene hotelsScene = new Scene(hotelsRoot);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Ensuring proper stage retrieval
            currentStage.setScene(hotelsScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    // Review Management Methods
    @FXML
    private void handleAddReview() {
        System.out.println("Adding review...");
    }

    @FXML
    private void handleUpdateReview() {
        System.out.println("Updating review...");
    }

    @FXML
    private void handleDeleteReview() {
        System.out.println("Deleting review...");
    }

    @FXML
    private void handleViewReviews() {
        System.out.println("Viewing reviews...");
    }

    @FXML
    private void handleUploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        uploadedImageFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (uploadedImageFile != null) {
            Image image = new Image(uploadedImageFile.toURI().toString());
            imageView.setImage(image);
            hotelImageField.setText(uploadedImageFile.toURI().toString());
        }
    }

    @FXML
    private void handleSearch(ActionEvent actionEvent) {
        String query = searchField.getText();
        List<Hotel> searchResults = hotelService.search(query);
        hotelTable.getItems().setAll(searchResults);
    }

    @FXML
    private void handleViewDetails(ActionEvent actionEvent) {
        Hotel selectedHotel = hotelTable.getSelectionModel().getSelectedItem();
        if (selectedHotel != null) {
            System.out.println("Hotel details: " + selectedHotel);
        } else {
            showAlert("Selection Error", "Please select a hotel to view details.", AlertType.WARNING);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
