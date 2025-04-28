package chatbot;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FAQAnswerer {
    private List<JSONObject> faqs;

    public FAQAnswerer(String filePath) {
        loadFAQs(filePath);
    }

    private void loadFAQs(String filePath) {
        faqs = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("FAQ file not found: " + filePath);
            }
            InputStreamReader reader = new InputStreamReader(inputStream);
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            jsonArray.forEach(faq -> faqs.add((JSONObject) faq));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.err.println("Error loading FAQs from " + filePath);
        }
    }

    // Matching method (keep same)
    public String findAnswer(String query) {
        String queryLower = query.toLowerCase();
        List<JSONObject> matchingFAQs = faqs.stream()
                .filter(faq -> {
                    String question = ((String) faq.get("question")).toLowerCase();
                    String topic = ((String) faq.get("topic")).toLowerCase();
                    return question.contains(queryLower) || topic.contains(queryLower);
                })
                .collect(Collectors.toList());
    
        if (!matchingFAQs.isEmpty()) {
            return (String) matchingFAQs.get(0).get("answer");
        } else {
            return "Sorry, I don't have an answer to that question.";
        }
    }
    

    private boolean containsAnyWord(String text, String query) {
        String[] queryWords = query.split("\\s+");
        for (String word : queryWords) {
            if (text.contains(word)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getFAQsByTopic(String topic) {
        return faqs.stream()
                .filter(faq -> ((String) faq.get("topic")).equalsIgnoreCase(topic))
                .map(faq -> (String) faq.get("question"))
                .collect(Collectors.toList());
    }
}
