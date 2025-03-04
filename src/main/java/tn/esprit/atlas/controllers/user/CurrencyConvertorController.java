package tn.esprit.atlas.controllers.user;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurrencyConvertorController {

    @FXML
    private TextField amountField;

    @FXML
    private ComboBox<String> fromCurrency;

    @FXML
    private ComboBox<String> toCurrency;

    @FXML
    private Label resultLabel;

    @FXML
    public void initialize() {
        // Populate the ComboBoxes with valid currency codes
        fromCurrency.getItems().addAll("USD", "EUR", "GBP", "JPY", "CAD", "TND"); // Add TND
        toCurrency.getItems().addAll("USD", "EUR", "GBP", "JPY", "CAD", "TND"); // Add TND

        // Set default values
        fromCurrency.setValue("USD");
        toCurrency.setValue("EUR");
    }

    @FXML
    private void convertCurrency() {
        String amount = amountField.getText();
        String from = fromCurrency.getValue();
        String to = toCurrency.getValue();

        if (amount.isEmpty() || from == null || to == null) {
            resultLabel.setText("Please fill all fields.");
            return;
        }

        try {
            double convertedAmount = convert(Double.parseDouble(amount), from, to);
            resultLabel.setText(String.format("%.2f %s = %.2f %s", Double.parseDouble(amount), from, convertedAmount, to));
        } catch (NumberFormatException e) {
            resultLabel.setText("Invalid amount.");
        } catch (IOException | InterruptedException e) {
            resultLabel.setText("Error during conversion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private double convert(double amount, String from, String to) throws IOException, InterruptedException {
        String apiKey = "df7f61d658dae80142221623"; // Replace with your valid API key
        String url = String.format("https://v6.exchangerate-api.com/v6/%s/pair/%s/%s/%.0f", apiKey, from, to, amount);

        System.out.println("API URL: " + url); // Log the URL for debugging

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("API Response: " + response.body()); // Log the response for debugging

        if (response.statusCode() == 200) {
            JSONObject jsonResponse = new JSONObject(response.body());
            if (jsonResponse.getString("result").equals("success")) {
                return jsonResponse.getDouble("conversion_result");
            } else {
                throw new IOException("API error: " + jsonResponse.getString("error-type"));
            }
        } else {
            // Log the response body for debugging
            System.out.println("API Error Response: " + response.body());
            throw new IOException("Failed to convert currency: " + response.statusCode());
        }
    }
}
