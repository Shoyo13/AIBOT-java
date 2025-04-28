package chatbot.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MessageBubble extends JPanel {
    private JLabel messageLabel;

    public MessageBubble(String message, boolean isUser) {
        setLayout(new FlowLayout(isUser ? FlowLayout.RIGHT : FlowLayout.LEFT));
        setBorder(new EmptyBorder(5, 10, 5, 10));
        setOpaque(false);

        JPanel bubble = new JPanel();
        bubble.setBackground(isUser ? new Color(204, 229, 255) : new Color(240, 240, 240));
        bubble.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        bubble.setLayout(new BorderLayout());

        messageLabel = new JLabel("<html><body style='width: 250px'>" + message + "</body></html>");
        bubble.add(messageLabel, BorderLayout.CENTER);

        add(bubble);
    }
}