package tn.esprit.atlas.controllers.admin.offer;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.text.Text;
import java.io.File;
import tn.esprit.atlas.controllers.admin.AdminDashboardController;
import tn.esprit.atlas.entities.Offer;
import tn.esprit.atlas.services.OfferService;

public class OfferController {
    @FXML
    private ListView<Offer> offerListView;
    @FXML
    private Label offersview_count;
    @FXML
    private Button update_button;
    @FXML
    private Button delete_button;
    @FXML
    private Button addoffer_button;
    @FXML
    private GridPane headerGrid;
    @FXML
    private HBox manipulation_buttons;
    @FXML
    private VBox offersContainer;
    @FXML
    private VBox offersListContainer;

    private OfferService offerService = new OfferService();
    private AdminDashboardController dashboardController;
    private Offer selectedOffer;

    @FXML
    private void initialize() {
        loadOffers();

        // Set up header grid properly with all necessary columns
        setupHeaderGrid();

        // Hide manipulation buttons initially
        update_button.setVisible(false);
        delete_button.setVisible(false);

        // Configure the cell factory for the ListView
        offerListView.setCellFactory(lv -> new ListCell<Offer>() {
            @Override
            protected void updateItem(Offer offer, boolean empty) {
                super.updateItem(offer, empty);
                if (empty || offer == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create a row container
                    HBox row = new HBox();
                    row.setSpacing(15);
                    row.setAlignment(Pos.CENTER_LEFT);
                    row.setMaxWidth(Double.MAX_VALUE);
                    row.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: #f0f0f0; -fx-border-radius: 5;");

                    // Create and configure image view with proper styling and error handling
                    VBox imageContainer = createImageViewContainer(offer);

                    // Create text labels for offer details
                    VBox nameContainer = createLabelContainer(offer.getName(), "Name", 0.2);
                    VBox descContainer = createLabelContainer(offer.getDescription(), "Description", 0.3);
                    VBox priceContainer = createLabelContainer(String.format("%.2f DT", offer.getPrice()), "Price", 0.15);
                    VBox durationContainer = createLabelContainer(offer.getDuration() + " days", "Duration", 0.15);
                    VBox seatsContainer = createLabelContainer(String.valueOf(offer.getAvailableSeats()), "Available Seats", 0.15);

                    // Add all components to the row
                    row.getChildren().addAll(imageContainer, nameContainer, descContainer, priceContainer, durationContainer, seatsContainer);

                    // Bind the width of the row to the ListView width
                    row.prefWidthProperty().bind(offerListView.widthProperty().subtract(20));

                    setGraphic(row);
                }
            }
        });

        // Set up the selection listener for the ListView
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

    private void setupHeaderGrid() {
        // Clear existing children
        headerGrid.getChildren().clear();

        // Add column headers with appropriate styling
        Label imageLabel = new Label("Image");
        Label nameLabel = new Label("Name");
        Label descLabel = new Label("Description");
        Label priceLabel = new Label("Price");
        Label durationLabel = new Label("Duration");
        Label seatsLabel = new Label("Available Seats");

        // Apply bold styling to all headers
        String headerStyle = "-fx-font-weight: bold; -fx-font-size: 14px;";
        imageLabel.setStyle(headerStyle);
        nameLabel.setStyle(headerStyle);
        descLabel.setStyle(headerStyle);
        priceLabel.setStyle(headerStyle);
        durationLabel.setStyle(headerStyle);
        seatsLabel.setStyle(headerStyle);

        // Add labels to the grid at specific column indices
        headerGrid.add(imageLabel, 0, 0);
        headerGrid.add(nameLabel, 1, 0);
        headerGrid.add(descLabel, 2, 0);
        headerGrid.add(priceLabel, 3, 0);
        headerGrid.add(durationLabel, 4, 0);
        headerGrid.add(seatsLabel, 5, 0);

        // Configure column constraints
        headerGrid.getColumnConstraints().clear();
        addColumnConstraint(headerGrid, 0.1); // Image
        addColumnConstraint(headerGrid, 0.2); // Name
        addColumnConstraint(headerGrid, 0.3); // Description
        addColumnConstraint(headerGrid, 0.15); // Price
        addColumnConstraint(headerGrid, 0.15); // Duration
        addColumnConstraint(headerGrid, 0.15); // Seats
    }

    private void addColumnConstraint(GridPane grid, double percentWidth) {
        ColumnConstraints constraint = new ColumnConstraints();
        constraint.setPercentWidth(percentWidth * 100);
        grid.getColumnConstraints().add(constraint);
    }

    private VBox createImageViewContainer(Offer offer) {
        // Create the image view for the offer image
        ImageView imageView = new ImageView();

        try {
            // Try to load the image from the path
            String imagePath = offer.getPackageImage();
            Image image;

            // Check if the path is a URL or a file path
            if (imagePath.startsWith("http") || imagePath.startsWith("https")) {
                // Load from URL
                image = new Image(imagePath, 80, 80, true, true);
            } else {
                // Load from file path
                File file = new File(imagePath);
                if (file.exists()) {
                    image = new Image(file.toURI().toString(), 80, 80, true, true);
                } else {
                    throw new Exception("File does not exist: " + imagePath);
                }
            }

            imageView.setImage(image);
        } catch (Exception e) {
            // If loading fails, try to load a default "no image" placeholder
            try {
                String defaultImagePath = "/tn/esprit/atlas/assets/icons/offers_icon.png";
                Image defaultImage = new Image(getClass().getResourceAsStream(defaultImagePath), 80, 80, true, true);
                imageView.setImage(defaultImage);
            } catch (Exception ex) {
                System.err.println("Could not load default image: " + ex.getMessage());
            }
        }

        // Configure image view properties
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 3, 0, 0, 0);");

        // Create a container for the image with styling
        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setStyle("-fx-padding: 5; -fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");
        imageContainer.setPrefWidth(100);

        return imageContainer;
    }

    private VBox createLabelContainer(String text, String title, double widthPercent) {
        // Create title text with styling
        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-size: 10px; -fx-fill: #666666;");

        // Create value label with styling
        Label valueLabel = new Label(text);
        valueLabel.setWrapText(true);
        valueLabel.setStyle("-fx-font-size: 13px;");

        // Create container
        VBox container = new VBox(5, titleText, valueLabel);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPrefWidth(widthPercent * offerListView.getWidth());
        container.setMinWidth(100);

        return container;
    }

    public void setDashboardController(AdminDashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void loadOffers() {
        offerListView.getItems().clear();
        offerListView.getItems().addAll(offerService.getAllOffers());

        // Update the offers count label
        offersview_count.setText(String.valueOf(offerListView.getItems().size()));
    }

    @FXML
    private void handleDeleteOffer() {
        if (selectedOffer == null) {
            showAlert("Error", "Please select an offer to delete.", Alert.AlertType.ERROR);
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
                showAlert("Success", "Offer deleted successfully.", Alert.AlertType.INFORMATION);
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

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}