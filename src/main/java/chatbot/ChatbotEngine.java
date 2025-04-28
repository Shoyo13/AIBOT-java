package chatbot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ChatbotEngine {
    private final FAQAnswerer faqAnswerer;
    private final UserQueryProcessor queryProcessor;
    private final ExecutorService threadPool;
    private final Consumer<Message> messageConsumer; // Callback to update UI

    public ChatbotEngine(String faqFilePath, Consumer<Message> messageConsumer) {
        this.faqAnswerer = new FAQAnswerer(faqFilePath);
        this.queryProcessor = new UserQueryProcessor();
        this.messageConsumer = messageConsumer;
        this.threadPool = Executors.newFixedThreadPool(
            Math.max(2, Runtime.getRuntime().availableProcessors())
        );
    }

    public void processUserInput(String userInput) {
        threadPool.submit(() -> {
            messageConsumer.accept(new Message("User", userInput));

            String faqResponse = faqAnswerer.findAnswer(userInput);
            if (!faqResponse.contains("Sorry")) {
                messageConsumer.accept(new Message("Bot", faqResponse));
            } else {
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
