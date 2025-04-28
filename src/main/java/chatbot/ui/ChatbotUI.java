package chatbot.ui;

import chatbot.ChatbotEngine;
import chatbot.Message;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ChatbotUI extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private ChatbotEngine chatbotEngine;
    private List<MessageBubble> messageBubbles;
    private JPanel chatPanel;
    private JScrollPane scrollPane;

    public ChatbotUI() {
        setTitle("Customer Support Chatbot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new BorderLayout());

        messageBubbles = new ArrayList<>();
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(chatPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Send");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        chatbotEngine = new ChatbotEngine("src/data/faqs.json", this::displayMessage);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = inputField.getText().trim();
                if (!userInput.isEmpty()) {
                    chatbotEngine.processUserInput(userInput);
                    inputField.setText("");
                }
            }
        });

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendButton.doClick();
            }
        });

        setVisible(true);
    }

    private void displayMessage(Message message) {
        SwingUtilities.invokeLater(() -> {
            MessageBubble bubble = new MessageBubble(message.getContent(), message.getSender().equals("User"));
            messageBubbles.add(bubble);
            chatPanel.add(bubble);
            chatPanel.revalidate();
            chatPanel.repaint();

            // Scroll to the bottom
            JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
            verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatbotUI::new);
    }
}