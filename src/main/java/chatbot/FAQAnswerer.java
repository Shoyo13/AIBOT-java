package chatbot;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FAQAnswerer {
    private List<JSONObject> faqs;

    public FAQAnswerer(String filePath) {
        loadFAQs(filePath);
    }

    private void loadFAQs(String filePath) {
        faqs = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(filePath)) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            jsonArray.forEach(faq -> faqs.add((JSONObject) faq));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.err.println("Error loading FAQs from " + filePath);
        }
    }

    public String findAnswer(String query) {
        // Simple keyword-based matching (can be enhanced with NLP)
        String queryLower = query.toLowerCase();
        List<JSONObject> matchingFAQs = faqs.stream()
                .filter(faq -> ((String) faq.get("question")).toLowerCase().contains(queryLower) ||
                               ((String) faq.get("topic")).toLowerCase().contains(queryLower))
                .collect(Collectors.toList());

        if (!matchingFAQs.isEmpty()) {
            // For simplicity, return the first matching answer
            return (String) matchingFAQs.get(0).get("answer");
        } else {
            return "Sorry, I don't have an answer to that question.";
        }
    }

    // Method to get FAQs by topic (using Streams)
    public List<String> getFAQsByTopic(String topic) {
        return faqs.stream()
                .filter(faq -> ((String) faq.get("topic")).equalsIgnoreCase(topic))
                .map(faq -> (String) faq.get("question"))
                .collect(Collectors.toList());
    }
}