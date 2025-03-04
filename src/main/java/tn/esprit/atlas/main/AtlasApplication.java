package tn.esprit.atlas.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AtlasApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the main interface
        Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/atlas/views/user/signup-view.fxml"));
        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/tn/esprit/atlas/css/style.css").toExternalForm());

        // Bind scene dimensions to stage dimensions
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            root.prefWidth((double) newVal);
        });
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            root.prefHeight((double) newVal);
        });

        // Load the application icon
        Image icon = new Image(getClass().getResourceAsStream("/tn/esprit/atlas/assets/ATLAS_LOGO.png"));
        primaryStage.getIcons().add(icon);

        // Set the stage to maximized (full screen)
        primaryStage.setMaximized(true);

        // Set the scene to the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Atlas Application");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}