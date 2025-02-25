module tn.esprit.atlas.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires jbcrypt;

    // Export main package
    exports tn.esprit.atlas.main;

    // Open entities for reflection
    opens tn.esprit.atlas.entities to javafx.base, javafx.fxml;

    // Open main package for FXML
    opens tn.esprit.atlas.main to javafx.fxml;

    // Exports and opens for admin controllers
    exports tn.esprit.atlas.controllers.admin;
    opens tn.esprit.atlas.controllers.admin to javafx.fxml;

    // Exports and opens for admin.user controllers
    exports tn.esprit.atlas.controllers.admin.user to javafx.fxml;
    opens tn.esprit.atlas.controllers.admin.user to javafx.fxml;

    // Exports and opens for general controllers
    exports tn.esprit.atlas.controllers to javafx.fxml;
    opens tn.esprit.atlas.controllers to javafx.fxml;

    // Exports and opens for user controllers
    exports tn.esprit.atlas.controllers.user to javafx.fxml;
    opens tn.esprit.atlas.controllers.user to javafx.fxml;

    // Exports and opens for airline controllers
    exports tn.esprit.atlas.controllers.admin.airline to javafx.fxml;
    opens tn.esprit.atlas.controllers.admin.airline to javafx.fxml;

    // Exports and opens for offer controllers
    exports tn.esprit.atlas.controllers.admin.offer to javafx.fxml;
    opens tn.esprit.atlas.controllers.admin.offer to javafx.fxml;

    // Exports and opens flight controllers
    exports tn.esprit.atlas.controllers.admin.flight to javafx.fxml;
    opens tn.esprit.atlas.controllers.admin.flight to javafx.fxml;

    // Exports and opens reservation controllers
    exports tn.esprit.atlas.controllers.admin.reservation to javafx.fxml;
    opens tn.esprit.atlas.controllers.admin.reservation to javafx.fxml;

    // Exports and opens for user.auth controllers
    exports tn.esprit.atlas.controllers.user.auth to javafx.fxml;
    opens tn.esprit.atlas.controllers.user.auth to javafx.fxml;

    // Add this line to export and open the user.booking package
    exports tn.esprit.atlas.controllers.user.booking to javafx.fxml;
    opens tn.esprit.atlas.controllers.user.booking to javafx.fxml;
}