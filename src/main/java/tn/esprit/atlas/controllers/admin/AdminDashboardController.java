package tn.esprit.atlas.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tn.esprit.atlas.controllers.admin.airline.AddAirlineController;
import tn.esprit.atlas.controllers.admin.airline.AirlineController;
import tn.esprit.atlas.controllers.admin.airline.UpdateAirlineController;
import tn.esprit.atlas.controllers.admin.flight.AddFlightController;
import tn.esprit.atlas.controllers.admin.flight.FlightController;
import tn.esprit.atlas.controllers.admin.flight.UpdateFlightController;
import tn.esprit.atlas.controllers.admin.offer.AddOfferController;
import tn.esprit.atlas.controllers.admin.offer.OfferController;
import tn.esprit.atlas.controllers.admin.offer.UpdateOfferController;
import tn.esprit.atlas.controllers.admin.reservation.ReservationController;
import tn.esprit.atlas.controllers.admin.user.AdminUserController;
import tn.esprit.atlas.controllers.admin.user.UpdateUserController;
import tn.esprit.atlas.entities.AirLine;
import tn.esprit.atlas.entities.Flight;
import tn.esprit.atlas.entities.Offer;
import tn.esprit.atlas.entities.User;
import tn.esprit.atlas.utils.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardController {
    @FXML
    private StackPane contentPane;
    @FXML
    private Label title_username;
    @FXML
    private Label role;

    @FXML
    private HBox overviewButton;
    @FXML
    private HBox usersButton;
    @FXML
    private HBox reservationsButton;
    @FXML
    private HBox offersButton;
    @FXML
    private HBox flightsButton;
    @FXML
    private HBox airlinesButton;
    @FXML
    private HBox hotelsButton;

    @FXML
    private ImageView profile_image;

    @FXML
    private Button home_button;

    private User user = UserSession.getUser();

    @FXML
    public void initialize() {
        title_username.setText(user.getName());
        role.setText(user.getRole());
        title_username.setStyle("-fx-font-weight: bold");

        // Set the profile image dynamically
        if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
            try {
                // Convert file path to URL if it's a file path
                String imagePath = user.getProfileImage();
                if (imagePath.startsWith("C:")) { // Check if it's a file path
                    imagePath = "file:" + imagePath; // Add the file protocol
                }
                Image image = new Image(imagePath, 60, 60, true, true);
                profile_image.setImage(image);
            } catch (IllegalArgumentException e) {
                // Handle invalid URL or file path
                System.err.println("Invalid image path: " + user.getProfileImage());
                e.printStackTrace();
                // Set a default image if the provided image path is invalid
                Image defaultImage = new Image(getClass().getResourceAsStream("/images/default.jpeg"), 60, 60, true, true);
                profile_image.setImage(defaultImage);
            }
        } else {
            // Set a default image if no profile image is available
            Image defaultImage = new Image(getClass().getResourceAsStream("/images/default.jpeg"), 60, 60, true, true);
            profile_image.setImage(defaultImage);
        }

        // Load the overview interface by default
        loadOverview();
        setActiveButton(overviewButton); // Set the overview button as active by default
    }

    @FXML
    private void loadOverview() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/admin/overview.fxml"));
            Parent overviewView = loader.load();
            contentPane.getChildren().setAll(overviewView); // Set the overview as the default view
            setActiveButton(overviewButton); // Set the overview button as active
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGoToHome() {
        try {
            // Load the home-view.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/home-view.fxml"));
            Parent homeView = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) home_button.getScene().getWindow();

            // Set the new scene to the stage
            Scene scene = new Scene(homeView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleGoToAirlines() {
        loadView("/tn/esprit/atlas/views/admin/airline/airlines-view.fxml", this);
        setActiveButton(airlinesButton); // Set the airlines button as active
    }

    public void goToAddAirline() {
        loadView("/tn/esprit/atlas/views/admin/airline/addairline-view.fxml", this);
        setActiveButton(airlinesButton); // Set the airlines button as active
    }

    public void goToUpdateAirline(AirLine selectedAirline) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/admin/airline/updateairline-view.fxml"));
        try {
            Parent view = loader.load();
            UpdateAirlineController updateAirlineController = loader.getController();
            updateAirlineController.setDashboardController(this);
            updateAirlineController.setSelectedAirline(selectedAirline);
            contentPane.getChildren().setAll(view);
            setActiveButton(airlinesButton); // Set the airlines button as active
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleGoToFlights() {
        loadView("/tn/esprit/atlas/views/admin/flight/flights-view.fxml", this);
        setActiveButton(flightsButton); // Set the flights button as active
    }

    public void goToAddFlight() {
        loadView("/tn/esprit/atlas/views/admin/flight/addflight-view.fxml", this);
        setActiveButton(flightsButton); // Set the flights button as active
    }

    public void goToUpdateFlight(Flight selectedFlight) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/admin/flight/updateflight-view.fxml"));
        try {
            Parent view = loader.load();
            UpdateFlightController updateFlightController = loader.getController();
            updateFlightController.setDashboardController(this);
            updateFlightController.setSelectedFlight(selectedFlight);
            contentPane.getChildren().setAll(view);
            setActiveButton(flightsButton); // Set the flights button as active
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleGoToReservations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/admin/reservation/reservations-view.fxml"));
            Parent root = loader.load();

            ReservationController controller = loader.getController();
            controller.setDashboardController(this);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);
            setActiveButton(reservationsButton); // Set the reservations button as active
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleGoToOffers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/admin/offer/offers-view.fxml"));
            Parent root = loader.load();

            OfferController controller = loader.getController();
            controller.setDashboardController(this);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);
            setActiveButton(offersButton); // Set the offers button as active
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToAddOffer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/admin/offer/addoffer-view.fxml"));
            Parent view = loader.load();

            AddOfferController controller = loader.getController();
            controller.setDashboardController(this);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);
            setActiveButton(offersButton); // Set the offers button as active
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToUpdateOffer(Offer offer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/admin/offer/updateoffer-view.fxml"));
            Parent view = loader.load();

            UpdateOfferController controller = loader.getController();
            controller.setDashboardController(this);
            controller.setSelectedOffer(offer);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);
            setActiveButton(offersButton); // Set the offers button as active
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleGoToUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/admin/user/users-view.fxml"));
            Parent view = loader.load();

            AdminUserController adminUserController = loader.getController();
            adminUserController.setDashboardController(this); // Set the dashboardController

            contentPane.getChildren().setAll(view);
            setActiveButton(usersButton); // Set the users button as active
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToUpdateUser(User selectedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/admin/user/updateuser-view.fxml"));
            Parent view = loader.load();

            UpdateUserController updateUserController = loader.getController();
            updateUserController.setDashboardController(this);
            updateUserController.setSelectedUser(selectedUser);

            contentPane.getChildren().setAll(view);
            setActiveButton(usersButton); // Set the users button as active
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlPath, AdminDashboardController dashboardController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            Object controller = loader.getController();

            if (controller instanceof AirlineController) {
                ((AirlineController) controller).setDashboardController(dashboardController);
            } else if (controller instanceof AddAirlineController) {
                ((AddAirlineController) controller).setDashboardController(dashboardController);
            } else if (controller instanceof FlightController) {
                ((FlightController) controller).setDashboardController(dashboardController);
            } else if (controller instanceof AddFlightController) {
                ((AddFlightController) controller).setDashboardController(dashboardController);
            }

            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to set the active button
    private void setActiveButton(HBox activeButton) {
        // Remove the active class from all buttons
        overviewButton.getStyleClass().remove("sidenav-button-active");
        usersButton.getStyleClass().remove("sidenav-button-active");
        reservationsButton.getStyleClass().remove("sidenav-button-active");
        offersButton.getStyleClass().remove("sidenav-button-active");
        flightsButton.getStyleClass().remove("sidenav-button-active");
        airlinesButton.getStyleClass().remove("sidenav-button-active");
        hotelsButton.getStyleClass().remove("sidenav-button-active");

        // Add the active class to the clicked button
        activeButton.getStyleClass().add("sidenav-button-active");
    }
}