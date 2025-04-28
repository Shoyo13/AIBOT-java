package chatbot.ui;

import chatbot.ChatbotEngine;
import chatbot.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChatbotUI extends JFrame {
    private JTextField inputField;
    private JButton sendButton;
    private ChatbotEngine chatbotEngine;
    private List<MessageBubble> messageBubbles;
    private JPanel chatPanel;
    private JScrollPane scrollPane;
    private JPanel quickFAQPanel;
    private boolean greeted = false;

    public ChatbotUI() {
        setTitle("Customer Support Chatbot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 650);
        setLayout(new BorderLayout());

        messageBubbles = new ArrayList<>();

        // 🔥 Dark Mode toggle inside a top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JToggleButton darkModeToggle = new JToggleButton("Dark Mode");
        darkModeToggle.setFocusable(false);
        darkModeToggle.addActionListener(e -> toggleDarkMode(darkModeToggle.isSelected()));
        topPanel.add(darkModeToggle);
        add(topPanel, BorderLayout.NORTH);

        // 🔥 Center chat area
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(chatPanel);
        add(scrollPane, BorderLayout.CENTER);

        // 🔥 Bottom panel = input + FAQ buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        JPanel inputArea = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Send");
        inputArea.add(inputField, BorderLayout.CENTER);
        inputArea.add(sendButton, BorderLayout.EAST);

        quickFAQPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] quickFAQs = {"Shipping Costs", "Shipping Time", "Return Policy", "Reset Password", "Payment Methods", "Track Order", "Contact Support"};
        for (String faq : quickFAQs) {
            JButton button = new JButton(faq);
            button.setFocusable(false);
            button.addActionListener(e -> {
                inputField.setText(faq);
                sendButton.doClick(); // auto-send
            });
            quickFAQPanel.add(button);
        }

        bottomPanel.add(inputArea, BorderLayout.NORTH);
        bottomPanel.add(quickFAQPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        chatbotEngine = new ChatbotEngine("faqs.json", this::displayMessage);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUserInput();
            }
        });

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendButton.doClick();
            }
        });

        setVisible(true);

        greetOnce(); // auto-greet user after 1 sec
    }

    private void handleUserInput() {
        String userInput = inputField.getText().trim();
        if (!userInput.isEmpty()) {
            chatbotEngine.processUserInput(userInput);
            inputField.setText("");
        }
    }

    private void greetOnce() {
        if (!greeted) {
            greeted = true;
            Timer greetingTimer = new Timer(1000, e -> {
                displayMessage(new Message("Bot", "Hello! 👋 How can I help you today?"));
                ((Timer) e.getSource()).stop();
            });
            greetingTimer.setRepeats(false);
            greetingTimer.start();
        }
    }

    private void displayMessage(Message message) {
        SwingUtilities.invokeLater(() -> {
            String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
            MessageBubble bubble = new MessageBubble(message.getContent() + " (" + currentTime + ")", message.getSender().equals("User"));
            messageBubbles.add(bubble);
            chatPanel.add(bubble);
            chatPanel.revalidate();
            chatPanel.repaint();

            JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
            verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        });
    }

    // 🔥 Dark Mode toggle implementation
    private void toggleDarkMode(boolean darkMode) {
        Color background = darkMode ? Color.DARK_GRAY : Color.WHITE;
        Color foreground = darkMode ? Color.WHITE : Color.BLACK;

        chatPanel.setBackground(background);
        inputField.setBackground(background);
        inputField.setForeground(foreground);
        sendButton.setBackground(darkMode ? Color.GRAY : Color.LIGHT_GRAY);
        sendButton.setForeground(foreground);
        quickFAQPanel.setBackground(background);

        for (Component c : quickFAQPanel.getComponents()) {
            if (c instanceof JButton) {
                c.setBackground(darkMode ? Color.GRAY : Color.LIGHT_GRAY);
                c.setForeground(foreground);
            }
        }
        for (MessageBubble bubble : messageBubbles) {
            bubble.setBackground(background);
            bubble.setForeground(foreground);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatbotUI::new);
    }
}
