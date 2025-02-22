package tn.esprit.atlas.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.atlas.entities.AirLine;
import tn.esprit.atlas.services.AirLineService;

import java.io.IOException;

public class AirlinesController {

    @FXML
    private TextField idField;

    @FXML
    private TextField nomField;

    @FXML
    private TextField paysField;

    @FXML
    private TextField logoField;

    @FXML
    private ListView<AirLine> airlineListView;

    @FXML
    private Button addAirlineButton;

    private ObservableList<AirLine> airlineList = FXCollections.observableArrayList();
    private AirLineService airLineService = new AirLineService();

    @FXML
    public void initialize() {
        // Load airlines from the database
        loadAirlinesFromDatabase();

        // Set a custom cell factory to display airlines with buttons
        airlineListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(AirLine airline, boolean empty) {
                super.updateItem(airline, empty);
                if (empty || airline == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label airlineLabel = new Label(airline.getNom() + " - " + airline.getPays());

                    Button modifyButton = new Button("Modify");
                    modifyButton
                            .setStyle("-fx-background-color: #0162ab; -fx-padding: 5 15 5 15; -fx-text-fill: white;");
                    modifyButton.setOnAction(event -> handleModifyAirline(airline));

                    Button deleteButton = new Button("Delete");
                    deleteButton
                            .setStyle("-fx-background-color: #ef4444; -fx-padding: 5 15 5 15; -fx-text-fill: white;");
                    deleteButton.setOnAction(event -> handleDeleteAirline(airline));

                    HBox hbox = new HBox(10, airlineLabel, modifyButton, deleteButton);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void loadAirlinesFromDatabase() {
        airlineList.clear();
        airlineList.addAll(airLineService.getall());
        airlineListView.setItems(airlineList);
    }

    private void handleModifyAirline(AirLine airline) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/tn/esprit/atlas/views/updateairline-view.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modify Airline");

            // Listen for window close and refresh the list
            stage.setOnHidden(event -> loadAirlinesFromDatabase());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteAirline(AirLine airline) {
        airLineService.supprimer(airline);
        loadAirlinesFromDatabase();
    }

    @FXML
    private void handleListClick() {
        AirLine selectedAirline = airlineListView.getSelectionModel().getSelectedItem();
        if (selectedAirline != null) {
            idField.setText(String.valueOf(selectedAirline.getAirline_id()));
            nomField.setText(selectedAirline.getNom());
            paysField.setText(selectedAirline.getPays());
            logoField.setText(selectedAirline.getLogo());
        }
    }

    @FXML
    private void handleGoToAddAirlines() {
        loadScene("/tn/esprit/atlas/views/addairline-view.fxml");
    }

    private void loadScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) airlineListView.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        idField.clear();
        nomField.clear();
        paysField.clear();
        logoField.clear();
    }



}

