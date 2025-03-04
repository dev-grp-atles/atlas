package tn.esprit.atlas.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainController {

    @FXML
    private Button switchToSignInButton;
    @FXML
    private Button switchToReservationButton;
    @FXML
    private Button switchToVolsButton;
    @FXML
    private Button switchToAirlinesButton;


    @FXML
    private void switchToSignIn() throws IOException {
        // Load the sign-in view
        Parent signInRoot = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/user/signin-view.fxml"));

        // Get the current scene
        Scene currentScene = switchToSignInButton.getScene();

        System.out.println("the Sign in was clicked");

        // Set the new root to the existing scene
        currentScene.setRoot(signInRoot);
    }

    @FXML
    private void switchToReservation() throws IOException {
        // Load the sign-in view
        Parent reservationRoot = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/reservation-view.fxml"));

        // Get the current scene
        Scene currentScene = switchToReservationButton.getScene();

        System.out.println("the resezrvation was clicked");


        // Set the new root to the existing scene
        currentScene.setRoot(reservationRoot);
    }

    @FXML
    private void switchToVols() throws IOException {
        // Load the sign-in view
        Parent volsRoot = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/vols-view.fxml"));

        // Get the current scene
        Scene currentScene = switchToVolsButton.getScene();

        // Set the new root to the existing scene
        currentScene.setRoot(volsRoot);
    }

    @FXML
    private void switchToAirlines() throws IOException {
        // Load the sign-in view
        Parent airlinesRoot = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/airlines-view.fxml"));

        // Get the current scene
        Scene currentScene = switchToAirlinesButton.getScene();

        // Set the new root to the existing scene
        currentScene.setRoot(airlinesRoot);
    }

    @FXML
    private Button switchToForumButton; // Add this line

    @FXML
    private void switchToForum() throws IOException {
        // Load the forum view
        Parent forumRoot = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/community/forum_view.fxml"));

        // Get the current scene
        Scene currentScene = switchToForumButton.getScene();

        System.out.println("The Forum button was clicked");

        // Set the new root to the existing scene
        currentScene.setRoot(forumRoot);
    }

    //src/main/resources/tn/esprit/atlas/views/community/forum_view.fxml
    //src/main/resources/tn/esprit/atlas/views/community/forum_view.fxml
}
