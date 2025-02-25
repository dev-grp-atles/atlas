package tn.esprit.atlas.controllers.admin.offer;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.atlas.controllers.admin.AdminDashboardController;
import tn.esprit.atlas.entities.Offer;
import tn.esprit.atlas.services.OfferService;

public class OfferController {
    @FXML
    private ListView<Offer> offerListView;

    private OfferService offerService = new OfferService();
    private AdminDashboardController dashboardController;

    @FXML
    private Button update_button;
    @FXML
    private Button delete_button;

    private Offer selectedOffer;

    @FXML
    private void initialize() {
        loadOffers();

        update_button.setVisible(false);
        delete_button.setVisible(false);

        offerListView.setCellFactory(lv -> new ListCell<Offer>() {
            @Override
            protected void updateItem(Offer offer, boolean empty) {
                super.updateItem(offer, empty);
                if (empty || offer == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox row = new HBox();
                    row.setSpacing(10);
                    row.setAlignment(Pos.CENTER_LEFT);
                    row.setMaxWidth(Double.MAX_VALUE);

                    Label nameLabel = new Label(offer.getName());
                    nameLabel.setMaxWidth(offerListView.getWidth() * 0.2);
                    nameLabel.setMinWidth(offerListView.getWidth() * 0.2);
                    nameLabel.setWrapText(true);

                    Label descriptionLabel = new Label(offer.getDescription());
                    descriptionLabel.setMaxWidth(offerListView.getWidth() * 0.2);
                    descriptionLabel.setMinWidth(offerListView.getWidth() * 0.2);
                    descriptionLabel.setWrapText(true);

                    Label priceLabel = new Label(String.format("%.2f", offer.getPrice()));
                    priceLabel.setMaxWidth(offerListView.getWidth() * 0.1);
                    priceLabel.setMinWidth(offerListView.getWidth() * 0.1);
                    priceLabel.setWrapText(true);

                    row.getChildren().addAll(nameLabel, descriptionLabel, priceLabel);
                    row.prefWidthProperty().bind(offerListView.widthProperty().subtract(40));
                    setGraphic(row);
                }
            }
        });

        offerListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedOffer = newSelection;
                update_button.setVisible(true);
                delete_button.setVisible(true);
                update_button.setText("Update " + newSelection.getName());
                delete_button.setText("Delete " + newSelection.getName());
            } else {
                update_button.setVisible(false);
                delete_button.setVisible(false);
                selectedOffer = null;
            }
        });
    }

    public void setDashboardController(AdminDashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void loadOffers() {
        offerListView.getItems().clear();
        offerListView.getItems().addAll(offerService.getAllOffers());
    }

    @FXML
    private void handleDeleteOffer() {
        if (selectedOffer == null) {
            showAlert("Error", "Please select an offer to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Offer");
        confirmationAlert.setHeaderText("Are you sure you want to delete this offer?");
        confirmationAlert.setContentText("Offer: " + selectedOffer.getName());

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                offerService.deleteOffer(selectedOffer.getPackageld());
                loadOffers();
                showAlert("Success", "Offer deleted successfully.");
            }
        });
    }

    @FXML
    private void handleGoToAddOffer() {
        if (dashboardController != null) {
            dashboardController.goToAddOffer();
        }
    }

    @FXML
    private void handleGoToUpdateOffer() {
        if (dashboardController != null && selectedOffer != null) {
            dashboardController.goToUpdateOffer(selectedOffer);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}