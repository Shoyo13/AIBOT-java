package chatbot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ChatbotEngine {
    private final FAQAnswerer faqAnswerer;
    private final UserQueryProcessor queryProcessor;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(2); // For handling tasks concurrently
    private final Consumer<Message> messageConsumer; // Callback to update UI

    public ChatbotEngine(String faqFilePath, Consumer<Message> messageConsumer) {
        this.faqAnswerer = new FAQAnswerer(faqFilePath);
        this.queryProcessor = new UserQueryProcessor();
        this.messageConsumer = messageConsumer;
    }

    public void processUserInput(String userInput) {
        threadPool.submit(() -> {
            messageConsumer.accept(new Message("User", userInput));

            // First, try to answer from FAQs
            String faqResponse = faqAnswerer.findAnswer(userInput);
            if (!faqResponse.contains("Sorry")) {
                messageConsumer.accept(new Message("Bot", faqResponse));
            } else {
                // If not found in FAQs, use NLP (on a separate thread to avoid blocking UI)
                queryProcessor.processQueryWithNLP(userInput)
                        .thenAccept(nlpResponse -> {
                            messageConsumer.accept(new Message("Bot", nlpResponse));
                        })
                        .exceptionally(e -> {
                            messageConsumer.accept(new Message("Bot", "Sorry, I encountered an error processing your request."));
                            e.printStackTrace();
                            return null;
                        });
            }
        });
    }

    public void shutdown() {
        threadPool.shutdown();
    }
}