package tn.esprit.atlas.controllers.user;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;

import java.io.IOException;

public class DistanceCalculatorController {

    @FXML
    private TextField originField;

    @FXML
    private TextField destinationField;

    @FXML
    private Label distanceLabel;

    private final OkHttpClient client = new OkHttpClient.Builder().build(); // Best practice to build the client

    @FXML
    private void calculateDistance() {
        String origin = originField.getText();
        String destination = destinationField.getText();

        if (origin.isEmpty() || destination.isEmpty()) {
            distanceLabel.setText("Please enter both origin and destination.");
            return;
        }

        // Properly encode the URL parameters
        String encodedOrigin = origin.replace(" ", "%20");
        String encodedDestination = destination.replace(" ", "%20");

        String url = String.format("https://driving-distance-calculator-between-two-points.p.rapidapi.com/data?origin=%s&destination=%s",
                encodedOrigin,
                encodedDestination);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-key", "cd86511e41msh311bad91dbad7a7p1dde97jsn15ae0f0db89c")
                .addHeader("x-rapidapi-host", "driving-distance-calculator-between-two-points.p.rapidapi.com")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                try (ResponseBody responseBody = response.body()) {
                    String responseBodyString = responseBody.string();
                    System.out.println("API Response: " + responseBodyString); // Log the response for debugging

                    JSONObject json = new JSONObject(responseBodyString);

                    if (json.has("distance_in_kilometers") && json.has("travel_time")) {
                        double distance = json.getDouble("distance_in_kilometers");
                        String travelTime = json.getString("travel_time");

                        String formattedDistance = String.format("Distance: %.2f km", distance);
                        String formattedTravelTime = String.format("Travel Time: %s", travelTime);

                        distanceLabel.setText(formattedDistance + "\n" + formattedTravelTime); // Combine distance and travel time
                    } else {
                        distanceLabel.setText("Distance or travel time data not found in the response.");
                    }
                }
            } else {
                distanceLabel.setText("Failed to calculate distance. Please try again.");
                System.err.println("API Request Failed: " + response.code() + " " + response.message()); // Log error details
            }
        } catch (IOException e) {
            e.printStackTrace();
            distanceLabel.setText("An error occurred. Please check your connection.");
        } catch (Exception e) {  //Catch JSON parsing exceptions and other unexpected problems
            e.printStackTrace();
            distanceLabel.setText("An unexpected error occurred while processing the response.");
        }
    }
}