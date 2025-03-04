package tn.esprit.atlas.controllers.user;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AiGuideController {

    @FXML
    private VBox chatBox; // Container for chat messages

    @FXML
    private TextField userInput; // Text field for user input

    @FXML
    private Button sendButton; // Button to send messages

    @FXML
    private ScrollPane chatScrollPane; // ScrollPane to ensure chat is scrollable

    private boolean isFirstMessage = true; // Flag to check if it's the first message

    // Replace with your actual Gemini API endpoint and API key
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + "AIzaSyD6ifcOkBrTATXt6aqhBRYG5HEjhrMDFZQ"; // Replace with actual Gemini API endpoint

    private final OkHttpClient httpClient = new OkHttpClient(); // HTTP client for API calls

    @FXML
    public void initialize() {
        // Start the conversation
        if (isFirstMessage) {
            addAiMessage("Hi! I'm your travel assistant. I can help you plan your trip. Where are you planning to go?");
            isFirstMessage = false;
        }

        // Keep ScrollPane scrolled to bottom when content changes
        chatBox.heightProperty().addListener((observable, oldValue, newValue) -> {
            chatScrollPane.layout();
            chatScrollPane.setVvalue(1.0);
        });

        // Add input validation and controls
        setupInputControls();
    }

    private void setupInputControls() {
        // 1. Input Length Validation
        userInput.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > 500) { // Limit input to 500 characters
                userInput.setText(userInput.getText().substring(0, 500));
                showTooltip("Maximum input length is 500 characters.");
            }
        });

        // 2. Input Format Validation (e.g., no special characters)
        userInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[\\p{L}\\p{N}\\s.,!?]*")) { // Allow letters, numbers, spaces, and basic punctuation
                userInput.setText(oldValue);
                showTooltip("Special characters are not allowed.");
            }
        });

        // 3. Enter Key to Send Message
        userInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSendMessage();
            }
        });

        // 4. Disable Send Button if Input is Empty
        userInput.textProperty().addListener((observable, oldValue, newValue) -> {
            sendButton.setDisable(newValue.trim().isEmpty());
        });

        // 5. Input Placeholder Text
        userInput.setPromptText("Type your message here...");

        // 6. Input Focus Listener
        userInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Remove the blue border when focused
                userInput.setStyle("-fx-border-color: transparent;");
            } else {
                userInput.setStyle("-fx-border-color: transparent;"); // Remove highlight when not focused
            }
        });

        // 7. Input Tooltip
        Tooltip tooltip = new Tooltip("Type your message here. Press Enter to send.");
        userInput.setTooltip(tooltip);
    }

    private void showTooltip(String message) {
        Tooltip tooltip = new Tooltip(message);
        tooltip.setAutoHide(true);
        tooltip.show(userInput.getScene().getWindow());
    }

    @FXML
    private void handleSendMessage() {
        String userMessage = userInput.getText().trim();
        if (!userMessage.isEmpty()) {
            // Add the user's message to the chat
            addUserMessage(userMessage);

            // Clear the input field
            userInput.clear();

            // Send the user's message to Gemini AI and get a response
            sendMessageToGemini(userMessage);
        }
    }

    private void sendMessageToGemini(String userMessage) {
        // Create the request body (JSON format)
        JSONObject jsonBody = new JSONObject();
        JSONObject contents = new JSONObject();
        JSONArray parts = new JSONArray();
        JSONObject part = new JSONObject();
        part.put("text", userMessage);
        parts.put(part);
        contents.put("parts", parts);
        JSONArray contentArray = new JSONArray();
        contentArray.put(contents);
        jsonBody.put("contents", contentArray);

        // Build the HTTP request
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

        Request request = new Request.Builder()
                .url(GEMINI_API_URL)
                .post(body)
                .build();

        // Send the request asynchronously
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle API call failure
                e.printStackTrace();
                Platform.runLater(() -> addAiMessage("Sorry, I'm having trouble connecting to the server. Please try again later."));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Parse the API response
                    String responseBody = response.body().string();
                    String aiResponse = parseGeminiResponse(responseBody);

                    // Add the AI's response to the chat
                    Platform.runLater(() -> addAiMessage(aiResponse));
                } else {
                    // Handle API error
                    Platform.runLater(() -> addAiMessage("Sorry, I couldn't process your request. Please try again."));
                }
            }
        });
    }

    private String parseGeminiResponse(String responseBody) {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray candidates = jsonResponse.getJSONArray("candidates");
            JSONObject candidate = candidates.getJSONObject(0);
            JSONObject content = candidate.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            JSONObject part = parts.getJSONObject(0);
            return part.getString("text");
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing response. Please try again.";
        }
    }

    private void addUserMessage(String message) {
        addMessage(message, true);
    }

    private void addAiMessage(String message) {
        addMessage(message, false);
    }

    private void addMessage(String message, boolean isUser) {
        HBox messageContainer = new HBox();
        messageContainer.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        messageContainer.setPadding(new javafx.geometry.Insets(5, 10, 5, 10));

        TextFlow textFlow = createTextFlow(message, isUser); // Pass isUser flag
        textFlow.setStyle("-fx-background-color: " + (isUser ? "#0589ed" : "#ECECEC") + "; -fx-padding: 10px; -fx-background-radius: 10px;");
        textFlow.setPrefWidth(400);
        textFlow.setMaxWidth(400);

        messageContainer.getChildren().add(textFlow);
        Platform.runLater(() -> chatBox.getChildren().add(messageContainer));
    }

    private TextFlow createTextFlow(String message, boolean isUser) {
        TextFlow textFlow = new TextFlow();
        Pattern pattern = Pattern.compile("\\*\\*(.*?)\\*\\*");
        Matcher matcher = pattern.matcher(message);

        int lastIndex = 0;
        while (matcher.find()) {
            // Add the text before the bold text
            String regularText = message.substring(lastIndex, matcher.start());
            Text regular = new Text(regularText);
            if (isUser) {
                regular.setFill(Color.WHITE); // Set text color to white for user messages
            }
            textFlow.getChildren().add(regular);

            // Add the bold text
            String boldText = matcher.group(1);
            Text bold = new Text(boldText);
            bold.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));  // Or any other font/size you prefer
            if (isUser) {
                bold.setFill(Color.WHITE); // Set text color to white for user messages
            }
            textFlow.getChildren().add(bold);

            lastIndex = matcher.end();
        }

        // Add the remaining text after the last bold text
        String remainingText = message.substring(lastIndex);
        Text remaining = new Text(remainingText);
        if (isUser) {
            remaining.setFill(Color.WHITE); // Set text color to white for user messages
        }
        textFlow.getChildren().add(remaining);

        return textFlow;
    }
}