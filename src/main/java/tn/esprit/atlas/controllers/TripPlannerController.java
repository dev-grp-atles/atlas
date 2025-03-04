package tn.esprit.atlas.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class TripPlannerController {

    @FXML
    private TextField destinationField;

    @FXML
    private FlowPane tripPlanContainer; // Changed to FlowPane to allow wrapping

    private final OkHttpClient client = new OkHttpClient();
    private static final String OPENTRIPMAP_API_KEY = "5ae2e3f221c38a28845f05b615af32c49f5ce35a5a75a8ed5ffe6422";
    private static final String OPENCAGE_API_KEY = "0c0621caf56b4fb0b6229e9bcac17317";

    @FXML
    private void generateTripPlan() {
        String destination = destinationField.getText().trim();
        if (!destination.isEmpty()) {
            double[] coordinates = getLatLongFromDestination(destination);
            if (coordinates != null) {
                double latitude = coordinates[0];
                double longitude = coordinates[1];
                String tripPlan = fetchPOIFromOpenTripMap(latitude, longitude);
                displayTripPlan(tripPlan);
            } else {
                tripPlanContainer.getChildren().clear();
                Text errorText = new Text("Failed to fetch coordinates for the destination.");
                tripPlanContainer.getChildren().add(errorText);
            }
        } else {
            tripPlanContainer.getChildren().clear();
            Text errorText = new Text("Please enter a destination.");
            tripPlanContainer.getChildren().add(errorText);
        }
    }

    private double[] getLatLongFromDestination(String destination) {
        String url = "https://api.opencagedata.com/geocode/v1/json?q=" + destination + "&key=" + OPENCAGE_API_KEY;
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
                JSONArray results = jsonObject.getJSONArray("results");

                if (results.length() > 0) {
                    JSONObject firstResult = results.getJSONObject(0);
                    JSONObject geometry = firstResult.getJSONObject("geometry");
                    return new double[]{geometry.getDouble("lat"), geometry.getDouble("lng")};
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String fetchPOIFromOpenTripMap(double latitude, double longitude) {
        int radius = 5000;
        int limit = 10;
        String url = "https://api.opentripmap.com/0.1/en/places/radius?lat=" + latitude + "&lon=" + longitude + "&radius=" + radius + "&limit=" + limit + "&apikey=" + OPENTRIPMAP_API_KEY;
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
                JSONArray features = jsonObject.getJSONArray("features");
                StringBuilder tripPlan = new StringBuilder();

                for (int i = 0; i < features.length(); i++) {
                    JSONObject feature = features.getJSONObject(i);
                    JSONObject properties = feature.getJSONObject("properties");
                    String name = properties.optString("name", "Unknown");
                    String xid = properties.optString("xid", "");
                    String address = xid.isEmpty() ? "Unknown" : fetchPOIDetails(xid);

                    tripPlan.append("Name: ").append(name).append("\n");
                    tripPlan.append("Address: ").append(address).append("\n\n");
                }
                return tripPlan.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Failed to fetch trip plan.";
    }

    private String fetchPOIDetails(String xid) {
        String url = "https://api.opentripmap.com/0.1/en/places/xid/" + xid + "?apikey=" + OPENTRIPMAP_API_KEY;
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                if (jsonObject.has("address")) {
                    JSONObject addressObj = jsonObject.getJSONObject("address");

                    // Check multiple fields to get the best available address
                    String road = addressObj.optString("road", "");
                    String houseNumber = addressObj.optString("house_number", "");
                    String suburb = addressObj.optString("suburb", "");
                    String city = addressObj.optString("city", "Unknown");
                    String country = addressObj.optString("country", "Unknown");

                    // Construct the full address with the available details
                    String fullAddress = (houseNumber.isEmpty() ? "" : houseNumber + " ") +
                            (road.isEmpty() ? "" : road + ", ") +
                            (suburb.isEmpty() ? "" : suburb + ", ") +
                            city + ", " + country;

                    return fullAddress.trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    private void displayTripPlan(String tripPlan) {
        tripPlanContainer.getChildren().clear();
        String[] entries = tripPlan.split("\n\n");

        for (int i = 0; i < entries.length; i++) {
            String entry = entries[i];
            String[] parts = entry.split("\n");

            VBox entryBox = new VBox();
            entryBox.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 10; -fx-padding: 10; -fx-spacing: 5;");
            TextFlow textFlow = new TextFlow();

            Text stepText = new Text((i + 1) + ". ");
            stepText.setStyle("-fx-font-weight: bold;");

            Text nameText = new Text(parts[0] + "\n");

            textFlow.getChildren().add(stepText);
            textFlow.getChildren().add(nameText);

            if (parts.length > 1) { // Check before accessing parts[1]
                Text addressText = new Text(parts[1]);
                textFlow.getChildren().add(addressText);
            } else {
                Text errorText = new Text("Address not available.");
                textFlow.getChildren().add(errorText);
            }

            entryBox.getChildren().add(textFlow);
            tripPlanContainer.getChildren().add(entryBox);
        }
    }

}