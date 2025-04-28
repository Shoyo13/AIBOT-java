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

        JPanel bubble = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Rounded corners
                g2.dispose();
            }
        };

        bubble.setBackground(isUser ? new Color(204, 229, 255) : new Color(240, 240, 240));
        bubble.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        bubble.setLayout(new BorderLayout());

        messageLabel = new JLabel("<html><body style='width: 250px'>" + message + "</body></html>");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        bubble.add(messageLabel, BorderLayout.CENTER);

        add(bubble);
    }
}
