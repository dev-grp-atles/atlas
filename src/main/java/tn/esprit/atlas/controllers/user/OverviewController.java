package tn.esprit.atlas.controllers.user;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OverviewController {

    @FXML
    private VBox overviewController;

    @FXML
    private TextField weather_input;

    @FXML
    private DatePicker weather_date;

    @FXML
    private Button weatherSearchButton;

    @FXML
    private Label weather_display; // Label to display the weather data

    @FXML
    private ImageView weather_image; // ImageView to display the weather icon

    private static final String API_URL = "https://api.open-meteo.com/v1/forecast?"; // Base URL
    private static final String NOMINATIM_API_URL = "https://nominatim.openstreetmap.org/search?";

    private final OkHttpClient client = new OkHttpClient.Builder().build();

    @FXML
    public void initialize() {
        // Initialization code here if needed
        // Example for debugging connection leaks, uncomment if needed:
        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
    }

    @FXML
    private void handleWeatherSearch() {
        String city = weather_input.getText();
        LocalDate date = weather_date.getValue();

        if (city != null && !city.isEmpty() && date != null) {
            getWeather(city, date);
        } else {
            weather_display.setText("Please enter a city and select a date.");
        }
    }

    private void getWeather(String city, LocalDate date) {
        // 1. Geocode the city to get latitude and longitude
        geocodeCity(city, (latitude, longitude) -> {
            // This code will be executed after successful geocoding
            if (latitude != null && longitude != null) {
                // 2. Call the weather API with the obtained coordinates
                fetchWeather(latitude, longitude, date);
            } else {
                javafx.application.Platform.runLater(() -> weather_display.setText("Could not find coordinates for the city."));
            }
        });
    }

    private void geocodeCity(String city, GeocodingCallback callback) {
        // Perform the Geocoding API Call (Nominatim)
        HttpUrl.Builder urlBuilder = HttpUrl.parse(NOMINATIM_API_URL).newBuilder();
        urlBuilder.addQueryParameter("q", city);
        urlBuilder.addQueryParameter("format", "jsonv2");
        urlBuilder.addQueryParameter("limit", "1");

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "YourAppName") // Required by Nominatim
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() -> weather_display.setText("Error geocoding the city: " + e.getMessage()));
                callback.onResult(null, null); // Inform that geocoding failed
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {  // Ensure response body is closed.
                    if (response.isSuccessful()) {
                        try {
                            String responseBodyString = responseBody.string();
                            JSONArray jsonArray = new JSONArray(responseBodyString);

                            if (jsonArray.length() > 0) {
                                JSONObject result = jsonArray.getJSONObject(0);
                                double latitude = result.getDouble("lat");
                                double longitude = result.getDouble("lon");
                                callback.onResult(latitude, longitude); // Inform of successful geocoding
                            } else {
                                javafx.application.Platform.runLater(() -> weather_display.setText("City not found."));
                                callback.onResult(null, null); // Inform that geocoding failed
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            javafx.application.Platform.runLater(() -> weather_display.setText("Error parsing geocoding data."));
                            callback.onResult(null, null); // Inform that geocoding failed
                        }
                    } else {
                        javafx.application.Platform.runLater(() -> weather_display.setText("Geocoding API request failed: " + response.code() + " " + response.message()));
                        callback.onResult(null, null); // Inform that geocoding failed
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    javafx.application.Platform.runLater(() -> weather_display.setText("Error processing the geocoding response."));
                    callback.onResult(null, null);
                }
            }
        });
    }

    private void fetchWeather(double latitude, double longitude, LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(formatter);

        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL).newBuilder();
        urlBuilder.addQueryParameter("latitude", String.valueOf(latitude));
        urlBuilder.addQueryParameter("longitude", String.valueOf(longitude));
        urlBuilder.addQueryParameter("daily", "weathercode,temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min");
        urlBuilder.addQueryParameter("timezone", "auto");
        urlBuilder.addQueryParameter("start_date", formattedDate);
        urlBuilder.addQueryParameter("end_date", formattedDate);  // Request only the specified date

        String apiUrl = urlBuilder.build().toString();


        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() -> weather_display.setText("Error fetching weather data: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) { // Ensure response body is closed.
                    if (response.isSuccessful()) {
                        try {
                            String responseBodyString = responseBody.string();
                            JSONObject jsonResponse = new JSONObject(responseBodyString);

                            // Extract daily data
                            JSONObject dailyData = jsonResponse.getJSONObject("daily");

                            // Extract relevant data. The indexes are important because the API returns arrays.
                            int weatherCodeInt = dailyData.getJSONArray("weathercode").getInt(0); // Get as Integer
                            String weatherCode = String.valueOf(weatherCodeInt); // Convert to String

                            double tempMax = dailyData.getJSONArray("temperature_2m_max").getDouble(0);
                            double tempMin = dailyData.getJSONArray("temperature_2m_min").getDouble(0);
                            double apparentTempMax = dailyData.getJSONArray("apparent_temperature_max").getDouble(0);
                            double apparentTempMin = dailyData.getJSONArray("apparent_temperature_min").getDouble(0);

                            // Convert weather code to description and icon path
                            String[] weatherInfo = getWeatherDescription(weatherCode);
                            String description = weatherInfo[0];
                            String iconPath = weatherInfo[1];

                            String weatherData = "Weather in " + weather_input.getText() + " on " + formattedDate + ":\n" + // Using city from input
                                    "Description: " + description + "\n" +
                                    "Max Temperature: " + tempMax + " °C\n" +
                                    "Min Temperature: " + tempMin + " °C\n" +
                                    "Max Apparent Temperature: " + apparentTempMax + " °C\n" +
                                    "Min Apparent Temperature: " + apparentTempMin + " °C";

                            // Update the UI with the weather data and icon
                            javafx.application.Platform.runLater(() -> {
                                weather_display.setText(weatherData);
                                try {
                                    weather_image.setImage(new Image(getClass().getResourceAsStream(iconPath)));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    weather_display.setText(weather_display.getText() + "\nError loading image: " + e.getMessage());
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            javafx.application.Platform.runLater(() -> weather_display.setText("Error parsing weather data."));
                        }
                    } else {
                        javafx.application.Platform.runLater(() -> weather_display.setText("Weather API request failed: " + response.code() + " " + response.message()));
                    }
                }  catch (Exception e) {
                    e.printStackTrace();
                    javafx.application.Platform.runLater(() -> weather_display.setText("Error processing the weather response."));
                }
            }
        });
    }

    // Simple mapping for weather codes (expand as needed)
    private String[] getWeatherDescription(String weatherCode) {
        switch (weatherCode) {
            case "0": return new String[]{"Clear sky", "/tn/esprit/atlas/assets/icons/sun.png"};
            case "1": case "2": case "3": return new String[]{"Mainly clear, partly cloudy, and overcast", "/tn/esprit/atlas/assets/icons/cloudy.png"};
            case "45": case "48": return new String[]{"Fog and depositing rime fog", "/tn/esprit/atlas/assets/icons/cloudy.png"};
            case "51": case "53": case "55": return new String[]{"Drizzle: Light, moderate, and dense intensity", "/tn/esprit/atlas/assets/icons/rainy-day.png"};
            case "56": case "57": return new String[]{"Freezing Drizzle: Light and dense intensity", "/tn/esprit/atlas/assets/icons/rainy-day.png"};
            case "61": case "63": case "65": return new String[]{"Rain: Slight, moderate and heavy intensity", "/tn/esprit/atlas/assets/icons/rainy-day.png"};
            case "66": case "67": return new String[]{"Freezing Rain: Light and heavy intensity", "/tn/esprit/atlas/assets/icons/rainy-day.png"};
            case "71": case "73": case "75": return new String[]{"Snow fall: Slight, moderate, and heavy intensity", "/tn/esprit/atlas/assets/icons/rainy-day.png"};
            case "77": return new String[]{"Snow grains", "/tn/esprit/atlas/assets/icons/rainy-day.png"};
            case "80": case "81": case "82": return new String[]{"Rain showers: Slight, moderate, and violent", "/tn/esprit/atlas/assets/icons/rainy-day.png"};
            case "85": case "86": return new String[]{"Snow showers slight and heavy", "/tn/esprit/atlas/assets/icons/rainy-day.png"};
            case "95": case "96": case "99": return new String[]{"Thunderstorm: Slight or moderate", "/tn/esprit/atlas/assets/icons/storm.png"};
            default: return new String[]{"Unknown", "/tn/esprit/atlas/assets/icons/sun.png"};
        }
    }

    // Callback interface for asynchronous geocoding
    interface GeocodingCallback {
        void onResult(Double latitude, Double longitude);
    }
}