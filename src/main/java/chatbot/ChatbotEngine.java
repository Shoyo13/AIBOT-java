package chatbot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javax.swing.Timer;

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

            // ✅ Show "Bot is typing..." first
            Message typingMessage = new Message("Bot", "Bot is typing...");
            messageConsumer.accept(typingMessage);

            Timer timer = new Timer(1500, e -> {
                String faqResponse = faqAnswerer.findAnswer(userInput);

                if (!faqResponse.contains("Sorry")) {
                    messageConsumer.accept(new Message("Bot", faqResponse));
                } else {
                    queryProcessor.processQueryWithNLP(userInput)
                            .thenAccept(nlpResponse -> {
                                messageConsumer.accept(new Message("Bot", nlpResponse));
                            })
                            .exceptionally(ex -> {
                                messageConsumer.accept(new Message("Bot", "Sorry, I encountered an error processing your request."));
                                ex.printStackTrace();
                                return null;
                            });
                }
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    public void shutdown() {
        threadPool.shutdown();
    }
}
