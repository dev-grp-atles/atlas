package tn.esprit.atlas.controllers.user.booking;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import tn.esprit.atlas.controllers.user.booking.BookFlightController;

import java.io.IOException;

public class BookingController {

    @FXML
    private StackPane bookingContent; // The StackPane where views will be loaded

    // Method to load the "Book Flight" view
    @FXML
    private void loadBookFlightView() {
        loadView("/tn/esprit/atlas/views/user/booking/bookflight-view.fxml");
    }

    // Method to load the "Book Hotel" view
    @FXML
    private void loadBookHotelView() {
        loadView("/tn/esprit/atlas/views/user/booking/bookhotel-view.fxml");
    }

    // Method to load the "Offers" view
    @FXML
    private void loadOffersView() {
        loadView("/tn/esprit/atlas/views/user/booking/offers-view.fxml");
    }

    // Helper method to load a view into the bookingContent StackPane
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            // Pass the bookingContent to the appropriate controller
            if (fxmlPath.equals("/tn/esprit/atlas/views/user/booking/bookflight-view.fxml")) {
                BookFlightController bookFlightController = loader.getController();
                bookFlightController.setBookingContent(bookingContent);
            } else if (fxmlPath.equals("/tn/esprit/atlas/views/user/booking/offers-view.fxml")) {
                OffersController offersController = loader.getController();
                offersController.setBookingContent(bookingContent);
            }

            // Replace the current content with the new view
            bookingContent.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Initialize the default view (e.g., Book Flight)
    @FXML
    private void initialize() {
        loadBookFlightView(); // Load the default view when the controller is initialized
    }
}