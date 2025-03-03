module tn.esprit.atlas.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires jbcrypt;

    // Export packages
    exports tn.esprit.atlas.main;
    exports tn.esprit.atlas.entities;
    exports tn.esprit.atlas.controllers;
    exports tn.esprit.atlas.controllers.admin;
    exports tn.esprit.atlas.controllers.user;
    exports tn.esprit.atlas.controllers.hotel;
    exports tn.esprit.atlas.controllers.review;
    exports tn.esprit.atlas.services;

    // Open packages to JavaFX (for FXML loading and reflection)
    opens tn.esprit.atlas.entities to javafx.base;
    opens tn.esprit.atlas.main to javafx.fxml;
    opens tn.esprit.atlas.controllers.admin to javafx.fxml;
    opens tn.esprit.atlas.controllers to javafx.fxml;
    opens tn.esprit.atlas.controllers.user to javafx.fxml;
    opens tn.esprit.atlas.controllers.hotel to javafx.fxml;
    opens tn.esprit.atlas.controllers.review to javafx.fxml; // Add this line
    opens tn.esprit.atlas.views.review to javafx.fxml;
}