module tn.esprit.atlas.main {
    requires javafx.fxml;
    requires mysql.connector.j;
    requires jbcrypt;
    requires com.google.protobuf;
    requires java.mail; // For JavaMail API
    requires org.apache.pdfbox; // Add PDFBox module
    requires okhttp3; // Add this for OkHttp
    requires org.json;
    requires java.desktop;

    // Add Google API client dependencies
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.jackson2;
    requires jdk.httpserver;
    requires com.google.gson;
    requires javafx.web;
    requires jdk.jsobject;
    requires java.net.http;

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

    // Exports and opens for user.booking controllers
    exports tn.esprit.atlas.controllers.user.booking to javafx.fxml;
    opens tn.esprit.atlas.controllers.user.booking to javafx.fxml;

    // Open utils package for JavaMail reflection
    opens tn.esprit.atlas.utils to java.mail;
}