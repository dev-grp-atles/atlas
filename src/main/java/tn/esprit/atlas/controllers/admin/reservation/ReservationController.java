package tn.esprit.atlas.controllers.admin.reservation;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import tn.esprit.atlas.controllers.admin.AdminDashboardController;
import tn.esprit.atlas.entities.Reservation;
import tn.esprit.atlas.services.ReservationService;

public class ReservationController {

    @FXML
    private ListView<Reservation> reservationListView;

    @FXML
    private Button delete_button;

    private ReservationService reservationService = new ReservationService();
    private AdminDashboardController dashboardController;

    @FXML
    private void initialize() {
        loadReservations();

        // Hide the delete button initially
        delete_button.setVisible(false);

        // Set a custom cell factory to format the ListView items
        reservationListView.setCellFactory(lv -> new ListCell<Reservation>() {
            @Override
            protected void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);

                if (empty || reservation == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create a layout to display the reservation details
                    HBox row = new HBox(10); // Spacing between elements
                    row.setAlignment(Pos.CENTER_LEFT);

                    // Add labels for each field
                    Label nameLabel = new Label(reservation.getPrenom() + " " + reservation.getNom());
                    Label emailLabel = new Label(reservation.getEmail());
                    Label roomLabel = new Label(reservation.getTypeChambre());
                    Label datesLabel = new Label(reservation.getDateArrivee() + " to " + reservation.getDateDepart());
                    Label totalLabel = new Label(reservation.getMontantTotal() + "DT");

                    // Set fixed width for each label to align columns
                    nameLabel.setPrefWidth(210);
                    emailLabel.setPrefWidth(240);
                    roomLabel.setPrefWidth(200);
                    datesLabel.setPrefWidth(300);
                    totalLabel.setPrefWidth(100);

                    // Add all labels to the row
                    row.getChildren().addAll(nameLabel, emailLabel, roomLabel, datesLabel, totalLabel);

                    // Set the row as the graphic for the cell
                    setGraphic(row);
                }
            }
        });

        // Listen for selection changes in the ListView
        reservationListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                delete_button.setVisible(true);
            } else {
                delete_button.setVisible(false);
            }
        });
    }

    public void setDashboardController(AdminDashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    // Load all reservations into the ListView
    public void loadReservations() {
        reservationListView.getItems().clear();
        reservationListView.getItems().addAll(reservationService.getAllReservations());
    }

    // Handle the delete button action
    @FXML
    private void handleDeleteReservation() {
        Reservation selectedReservation = reservationListView.getSelectionModel().getSelectedItem();

        if (selectedReservation == null) {
            showAlert("Error", "Please select a reservation to delete.");
            return;
        }

        // Confirm deletion
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Reservation");
        confirmationAlert.setHeaderText("Are you sure you want to delete this reservation?");
        confirmationAlert.setContentText("Reservation: " + selectedReservation.getPrenom() + " " + selectedReservation.getNom());

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                reservationService.deleteReservation(selectedReservation.getId());
                loadReservations(); // Refresh the list
                showAlert("Success", "Reservation deleted successfully.");
            }
        });
    }

    // Show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}