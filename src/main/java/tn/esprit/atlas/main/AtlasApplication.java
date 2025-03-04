package tn.esprit.atlas.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class AtlasApplication extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        // Load the main signup interface
        loadMainView();

        // Load the application icon
        Image icon = new Image(getClass().getResourceAsStream("/tn/esprit/atlas/assets/ATLAS_LOGO.png"));
        primaryStage.getIcons().add(icon);

        // Set the stage to maximized (full screen)
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Atlas Application");

        primaryStage.show();
    }

    private void loadMainView() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/user/signup-view.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/tn/esprit/atlas/css/style.css").toExternalForm());

        primaryStage.setScene(scene);
    }

    public void loadViewHotels() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/hotel/view-hotels-view.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/tn/esprit/atlas/css/viewHotels.css").toExternalForm());

        primaryStage.setScene(scene);
    }


    public void loadViewHotelsClientSide() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/hotel/view-hotels-clientside-view.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/tn/esprit/atlas/css/viewHotelsClientSide.css").toExternalForm());

        primaryStage.setScene(scene);
    }

    public void loadAddReview() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/review/AddReview-view.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/tn/esprit/atlas/css/review.css").toExternalForm());

        primaryStage.setScene(scene);
    }

    public void loadReviews() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/review/HotelReviews-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/tn/esprit/atlas/css/hotel-reviews.css").toExternalForm());
        primaryStage.setScene(scene);
    }



    public static void main(String[] args) {
        launch(args);
    }
}
