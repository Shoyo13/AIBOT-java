package chatbot;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FAQAnswerer {
    private List<JSONObject> faqs;
    private Map<String, String> keywordToQuestion;

    public FAQAnswerer(String filePath) {
        loadFAQs(filePath);
        setupKeywordMappings();
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

    private void setupKeywordMappings() {
        keywordToQuestion = new HashMap<>();
        keywordToQuestion.put("Shipping Costs", "What are your shipping costs?");
        keywordToQuestion.put("Shipping Time", "How long does shipping take?");
        keywordToQuestion.put("Reset Password", "How do I reset my password?");
        keywordToQuestion.put("Return Policy", "What is your return policy?");
        keywordToQuestion.put("Payment Methods", "What payment methods are accepted?");
        keywordToQuestion.put("Track Order", "How can I track my order?");
        keywordToQuestion.put("Contact Support", "How can I contact support?");
    }

    public String findAnswer(String query) {
        String queryLower = query.toLowerCase();
        String mappedQuery = keywordToQuestion.getOrDefault(query, query);
        String mappedQueryLower = mappedQuery.toLowerCase();
        String[] queryWords = mappedQueryLower.split("\\s+");

        JSONObject bestMatch = null;
        int highestScore = 0;

        for (JSONObject faq : faqs) {
            String question = ((String) faq.get("question")).toLowerCase();
            String topic = ((String) faq.get("topic")).toLowerCase();
            String combined = question + " " + topic;

            int score = 0;
            for (String word : queryWords) {
                if (combined.contains(word)) {
                    score++;
                }
            }

            if (score > highestScore) {
                highestScore = score;
                bestMatch = faq;
            }
        }

        if (bestMatch != null && highestScore > 0) {
            return (String) bestMatch.get("answer");
        } else {
            return "Sorry, I am not configured to answer that question right now.";
        }
    }

    public List<String> getFAQsByTopic(String topic) {
        List<String> faqsByTopic = new ArrayList<>();
        for (JSONObject faq : faqs) {
            if (((String) faq.get("topic")).equalsIgnoreCase(topic)) {
                faqsByTopic.add((String) faq.get("question"));
            }
        }
        return faqsByTopic;
    }
}
