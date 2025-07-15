package devassistant;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import com.google.gson.*;

public class GPTClient {

    private static final String API_KEY = System.getenv("OPENROUTER_API_KEY");
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    public static String ask(String prompt) {
        if (API_KEY == null || API_KEY.isEmpty()) {
            System.err.println("❌ OPENROUTER_API_KEY not set in environment.");
            return "";
        }

        try {
            HttpClient client = HttpClient.newHttpClient();

            JsonObject message = new JsonObject();
            message.addProperty("role", "user");
            message.addProperty("content", prompt);

            JsonArray messages = new JsonArray();
            messages.add(message);

            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", "google/gemma-3-4b-it:free");
            requestBody.add("messages", messages);
            requestBody.addProperty("temperature", 0.2);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("HTTP-Referer", "https://devassistant.local") // required by OpenRouter
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("[GPT API Response]");
            System.out.println(response.body());

            JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();

            if (!responseJson.has("choices")) {
                System.err.println("❌ GPT API Error: " + response.body());
                return "[Error: GPT response malformed or failed]";
            }

            return responseJson
                    .getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error communicating with OpenRouter API.";
        }
    }
    public static String cleanCodeBlock(String response) {
    // Removes Markdown-style triple backticks and optional language hints like ```java
    return response
            .replaceAll("(?s)```(?:java)?\\n?", "")  // removes starting ```java or ```
            .replaceAll("```", "")                   // removes ending ```
            .trim();                                 // trims extra whitespace
}

}
