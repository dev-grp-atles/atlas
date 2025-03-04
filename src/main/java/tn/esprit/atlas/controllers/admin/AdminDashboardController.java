package tn.esprit.atlas.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.atlas.entities.AirLine;
import tn.esprit.atlas.entities.User;
import tn.esprit.atlas.services.AirLineService;
import tn.esprit.atlas.utils.UserSession;
import tn.esprit.atlas.controllers.user.UserController;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AdminDashboardController {
    @FXML
    private Label role;
    @FXML
    private Label title_username;
    @FXML
    private Label users_count;

    @FXML
    private HBox airlines_button;

    @FXML
    private Button handleaddairline_button;

    @FXML
    private Button addairline_selectLogoButton;

    @FXML
    private TextField addairline_nameField;
    @FXML
    private TextField addairline_countryField;

    @FXML
    private ImageView logoImageView;

    @FXML
    private HBox update_airline; // Add this for the update form

    @FXML
    private TextField updateairline_nameField; // Add this for the update form
    @FXML
    private TextField updateairline_countryField; // Add this for the update form
    @FXML
    private ImageView update_logoImageView; // Add this for the update form
    @FXML
    private Button updateairline_selectLogoButton; // Add this for the update form
    @FXML
    private Button handleupdateairline_button; // Add this for the update form

    private File uploadedLogoFile;

    // ListView for airlines
    @FXML
    private VBox airlinesContainer;
    @FXML
    private ListView<AirLine> airlineListView;

    private ObservableList<AirLine> airlineList = FXCollections.observableArrayList();

    @FXML
    private Label airlinesview_count;

    //interfaces
    @FXML
    private HBox overview;
    @FXML
    private HBox airlines;
    @FXML
    private HBox add_airline;

    private List<User> users; // List to store all users
    private AirLineService airLineService = new AirLineService();

    private User user = UserSession.getUser();

    private int selectedAirlineId; // Add this variable to store the selected airline's ID

    @FXML
    private void initialize() {
        // Set the role and username in the UI
        role.setText(user.getRole());
        title_username.setText(user.getName() + " " + user.getSurname());
        title_username.setStyle("-fx-font-weight: bold");

        airlinesview_count.setText(String.valueOf(airlineList.size()));
        System.out.println(airlineList.size());

        // Fetch all users and store them in the list
        fetchAllUsers();
        users_count.setText(String.valueOf(users.size()));
        overview.setVisible(true);
        airlines.setVisible(false);
        add_airline.setVisible(false);
        update_airline.setVisible(false); // Ensure the update form is hidden initially

        // Set a custom cell factory for the ListView
        airlineListView.setCellFactory(param -> new ListCell<AirLine>() {
            @Override
            protected void updateItem(AirLine airLine, boolean empty) {
                super.updateItem(airLine, empty);
                if (empty || airLine == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create a GridPane to layout the data in a table-like format
                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(10); // Horizontal gap between columns
                    gridPane.setVgap(5);  // Vertical gap between rows

                    // Set column constraints to match the header
                    ColumnConstraints col1 = new ColumnConstraints();
                    col1.setPrefWidth(315); // Same as header
                    ColumnConstraints col2 = new ColumnConstraints();
                    col2.setPrefWidth(325); // Same as header
                    ColumnConstraints col3 = new ColumnConstraints();
                    col3.setPrefWidth(200); // Same as header
                    ColumnConstraints col4 = new ColumnConstraints();
                    col4.setHgrow(Priority.ALWAYS); // Allow the last column to grow
                    gridPane.getColumnConstraints().addAll(col1, col2, col3, col4);

                    // Add labels for each column
                    Label nameLabel = new Label(airLine.getNom());
                    Label countryLabel = new Label(airLine.getPays());
                    Label logoLabel = new Label(airLine.getLogo() != null ? airLine.getLogo() : "null");

                    // Add the labels to the GridPane
                    gridPane.add(nameLabel, 0, 0);    // Column 0, Row 0
                    gridPane.add(countryLabel, 1, 0); // Column 1, Row 0
                    gridPane.add(logoLabel, 2, 0);     // Column 2, Row 0

                    // Add Update and Delete buttons
                    Button updateButton = new Button("Update");
                    Button deleteButton = new Button("Delete");

                    // Increase the size of the buttons
                    updateButton.setPrefWidth(100); // Set preferred width
                    updateButton.setPrefHeight(30); // Set preferred height
                    deleteButton.setPrefWidth(100); // Set preferred width
                    deleteButton.setPrefHeight(30); // Set preferred height

                    // Style the buttons (optional)
                    updateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                    deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                    // Add event handlers for the buttons
                    updateButton.setOnAction(event -> handleGoToUpdateAirline(airLine));
                    deleteButton.setOnAction(event -> handleDeleteAirline(airLine));

                    // Create an HBox to hold the buttons and align them to the far right
                    HBox buttonBox = new HBox(10, updateButton, deleteButton);
                    buttonBox.setAlignment(Pos.CENTER_RIGHT); // Align buttons to the right
                    HBox.setHgrow(buttonBox, Priority.ALWAYS); // Allow the HBox to grow

                    // Add the buttonBox to the GridPane
                    gridPane.add(buttonBox, 3, 0); // Column 3, Row 0

                    // Set the GridPane as the graphic for the cell
                    setGraphic(gridPane);
                }
            }
        });

        // Load data into the ListView
        loadAirlines();
    }

    private void loadAirlines() {
        airlineList.clear(); // Clear existing data
        airlineList.addAll(airLineService.getall()); // Fetch data from the service
        airlineListView.setItems(airlineList); // Set data to the ListView
    }

    // Method to fetch all users
    private void fetchAllUsers() {
        UserController userController = new UserController();
        users = userController.listUsers(); // Fetch the list of users

        // Print the users to verify (optional)
        users.forEach(u -> System.out.println(u.getName() + " - " + u.getEmail()));
    }

    @FXML
    private void handleUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Logo");
        uploadedLogoFile = fileChooser.showOpenDialog(addairline_selectLogoButton.getScene().getWindow());

        if (uploadedLogoFile != null) {
            // Display the selected image in the ImageView
            Image image = new Image(uploadedLogoFile.toURI().toString());
            logoImageView.setImage(image);
            System.out.println("Logo selected: " + uploadedLogoFile.getName());
        } else {
            System.out.println("No logo selected.");
        }
    }

    @FXML
    private void handleGoToAirlines() {
        overview.setVisible(false);
        airlines.setVisible(true);
        add_airline.setVisible(false);
        update_airline.setVisible(false); // Hide the update form
    }

    @FXML
    private void handleGoToAddAirlines() {
        overview.setVisible(false);
        airlines.setVisible(false);
        add_airline.setVisible(true);
        update_airline.setVisible(false); // Hide the update form
    }

    @FXML
    private void handleGoToUpdateAirline(AirLine airLine) {
        // Store the selected airline's ID
        selectedAirlineId = airLine.getAirline_id(); // Set the selectedAirlineId here
        System.out.println("Selected Airline ID: " + selectedAirlineId); // Debug statement

        // Populate the update form fields with the selected airline's data
        updateairline_nameField.setText(airLine.getNom());
        updateairline_countryField.setText(airLine.getPays());

        // Load the logo if it exists
        if (airLine.getLogo() != null && !airLine.getLogo().isEmpty()) {
            Image image = new Image(new File(airLine.getLogo()).toURI().toString());
            update_logoImageView.setImage(image);
            uploadedLogoFile = new File(airLine.getLogo());
        } else {
            update_logoImageView.setImage(null);
            uploadedLogoFile = null;
        }

        // Switch to the update airline form
        overview.setVisible(false);
        airlines.setVisible(false);
        add_airline.setVisible(false);
        update_airline.setVisible(true);
    }

    @FXML
    private void handleAddAirline() {
        // Trim inputs to remove leading and trailing spaces
        String name = addairline_nameField.getText().trim();
        String country = addairline_countryField.getText().trim();

        // Validate inputs
        if (name.isEmpty() || country.isEmpty()) {
            // Show error message or alert
            showAlert("Error", "Please fill all fields. Fields cannot be empty or contain only spaces.");
            return;
        }

        if (uploadedLogoFile == null) {
            // Show error message or alert
            showAlert("Error", "Please select a logo.");
            return;
        }

        // Get the absolute file path of the uploaded logo
        String logoPath = uploadedLogoFile.getAbsolutePath();

        // Create the AirLine object
        AirLine airLine = new AirLine();
        airLine.setNom(name);
        airLine.setPays(country);
        airLine.setLogo(logoPath);

        // Add the airline to the database
        airLineService.addAirline(airLine);
        System.out.println("Airline added successfully!");

        // Clear fields after adding
        addairline_nameField.clear();
        addairline_countryField.clear();
        logoImageView.setImage(null); // Clear the logo preview
        uploadedLogoFile = null; // Reset the uploaded file

        // Refresh the ListView
        loadAirlines();

        // Switch back to the airlines view
        handleGoToAirlines();
    }

    @FXML
    private void handleUpdateAirline() {
        // Trim inputs to remove leading and trailing spaces
        String name = updateairline_nameField.getText().trim();
        String country = updateairline_countryField.getText().trim();

        // Validate inputs
        if (name.isEmpty() || country.isEmpty()) {
            // Show error message or alert
            showAlert("Error", "Please fill all fields. Fields cannot be empty or contain only spaces.");
            return;
        }

        // Get the absolute file path of the uploaded logo
        String logoPath = uploadedLogoFile != null ? uploadedLogoFile.getAbsolutePath() : null;

        // Create the AirLine object
        AirLine airLine = new AirLine();
        airLine.setAirline_id(selectedAirlineId); // Use the selectedAirlineId here
        airLine.setNom(name);
        airLine.setPays(country);
        airLine.setLogo(logoPath);

        // Update the airline in the database
        airLineService.updateAirline(airLine);
        System.out.println("Airline updated successfully!");

        // Clear fields after updating
        updateairline_nameField.clear();
        updateairline_countryField.clear();
        update_logoImageView.setImage(null); // Clear the logo preview
        uploadedLogoFile = null; // Reset the uploaded file

        // Refresh the ListView
        loadAirlines();

        // Switch back to the airlines view
        handleGoToAirlines();
    }

    @FXML
    private void handleDeleteAirline(AirLine airLine) {
        // Confirm deletion (optional)
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Airline");
        alert.setHeaderText("Are you sure you want to delete this airline?");
        alert.setContentText("This action cannot be undone.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            // Delete the airline from the database
            airLineService.deleteAirline(airLine);
            System.out.println("Airline deleted successfully!");

            // Refresh the ListView
            loadAirlines();
        }
    }

    public void loadScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) title_username.getScene().getWindow(); // Or any other component's scene reference
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle any potential IOExceptions
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