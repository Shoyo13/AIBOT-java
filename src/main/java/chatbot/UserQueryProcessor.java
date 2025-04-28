package chatbot;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class UserQueryProcessor {
    private String geminiApiKey;
    
    private String getGeminiApiUrl() {
        return "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + geminiApiKey;
    }
    
    public UserQueryProcessor() {
        // Initialize the API key
        loadApiKey();
    }

    private void loadApiKey() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
                geminiApiKey = properties.getProperty("gemini.api.key");
    
                if (geminiApiKey == null || geminiApiKey.isEmpty()) {
                    throw new IllegalStateException("Gemini API key not found in config.properties. Please add gemini.api.key=YOUR_API_KEY");
                }
            } else {
                throw new IllegalStateException("config.properties file not found in src/main/resources. Please create this file and add gemini.api.key=YOUR_API_KEY");
            }
        } catch (IOException e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load API key from config.properties.", e);
        }
    }
    

    public CompletableFuture<String> processQueryWithNLP(String query) {
        // Improved error handling and input validation
        if (geminiApiKey == null || geminiApiKey.equals("AIzaSyDSqyDlCYWvk6-mX59W0yyI31_D3EdeC4I")) {
            System.err.println("Error: Gemini API key is not configured correctly.  Please follow the instructions to set up your API key.");
            CompletableFuture<String> future = new CompletableFuture<>();
            future.complete("Sorry, I am not configured to answer that question right now.");
            return future;
        }

        String prompt = "Answer the following customer support query: " + query;
        String requestBody = String.format("""
            {
              "contents": [{
                "parts": [{"text": "%s"}]
              }]
            }
            """, prompt);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(getGeminiApiUrl()))

                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::extractResponse)
                .exceptionally(e -> {  // Handle exceptions during the API call
                    System.err.println("Error during Gemini API call: " + e.getMessage());
                    e.printStackTrace(); // Log the full exception
                    return "Sorry, I encountered an error communicating with the AI service.";
                });
    }

    private String extractResponse(String jsonResponse) {
        // Improved JSON parsingRobust JSON parsing logic using a library like org.json
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (jsonObject.has("candidates")) {
                JSONArray candidates = jsonObject.getJSONArray("candidates");
                if (candidates.length() > 0) {
                    JSONObject candidate = candidates.getJSONObject(0);
                    if (candidate.has("content")) {
                        JSONObject content = candidate.getJSONObject("content");
                         if (content.has("parts")) {
                            JSONArray parts = content.getJSONArray("parts");
                            if(parts.length() > 0){
                                JSONObject part = parts.getJSONObject(0);
                                if(part.has("text")){
                                    return part.getString("text");
                                }
                            }
                         }
                    }
                }
            }
        } catch (JSONException e) {
            System.err.println("Error parsing Gemini API response: " + e.getMessage());
            e.printStackTrace();
            return "Sorry, I couldn't understand the response from the AI service.";
        }
        return "Sorry, I couldn't get a more specific answer.";
    }
}