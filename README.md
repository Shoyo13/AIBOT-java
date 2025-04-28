#  Customer Support Chatbot

A Java-based desktop chatbot that answers customer support questions using:
- Preloaded FAQ knowledge (from `faqs.json`)
- Gemini Pro API fallback for natural language queries

Built using Java Swing for the interface and Maven for project management.

---

## 🚀 Features

- Instant answers from FAQ database
- Smart fallback using Gemini NLP API when FAQ doesn't match
- Beautiful user-friendly chat bubbles
- Non-blocking message handling (multi-threaded)
- Modular code (easy to extend)
- Licensed under MIT — free for anyone to use and modify

---

## 🛠️ Technologies Used

- Java 15
- Java Swing
- Maven
- JSON.simple (for FAQ parsing)
- org.json (for parsing Gemini API responses)
- Google Gemini API

---

## 📂 Project Structure

```
CustomerSupportChatbot/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── chatbot/
│   │   │   │   ├── ChatbotEngine.java
│   │   │   │   ├── FAQAnswerer.java
│   │   │   │   ├── Message.java
│   │   │   │   └── UserQueryProcessor.java
│   │   │   └── chatbot/ui/
│   │   │       ├── ChatbotUI.java
│   │   │       └── MessageBubble.java
│   │   └── resources/
│   │       ├── faqs.json
│   │       └── config.properties
├── LICENSE
├── pom.xml
└── README.md
```

---

## ⚙️ How to Build and Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/CustomerSupportChatbot.git
   cd CustomerSupportChatbot
   ```

2. **Configure Gemini API Key**
   - Create a file: `src/main/resources/config.properties`
   - Add your key:
     ```
     gemini.api.key=YOUR_GEMINI_API_KEY
     ```

3. **Prepare your FAQ Database**
   - Make sure `src/main/resources/faqs.json` exists.
   - Example `faqs.json`:

```json
[
    {
      "topic": "Shipping",
      "question": "What are your shipping costs?",
      "answer": "Our standard shipping costs ₹50. Express shipping is ₹100."
    },
    {
      "topic": "Shipping",
      "question": "How long does shipping take?",
      "answer": "Standard shipping usually takes 3-5 business days within India."
    },
    {
      "topic": "Returns",
      "question": "What is your return policy?",
      "answer": "We offer a 30-day return policy for unused items."
    },
    {
      "topic": "Account",
      "question": "How do I reset my password?",
      "answer": "You can reset your password by clicking the 'Forgot Password' link on the login page."
    },
    {
      "topic": "Account",
      "question": "How do I create an account?",
      "answer": "You can create an account by clicking the 'Sign Up' button on our website."
    },
    {
      "topic": "Payment",
      "question": "What payment methods do you accept?",
      "answer": "We accept credit cards, debit cards, and PayPal."
    },
    {
      "topic": "Payment",
      "question": "Is my payment information secure?",
      "answer": "Yes, we use SSL encryption to protect your payment information."
    }
]
```

4. **Build the project**
   ```bash
   mvn clean install
   ```

5. **Run the chatbot**
   ```bash
   mvn exec:java
   ```

✅ Your chatbot window will open!

---

## 💬 Sample Questions to Try

- What are your shipping costs?
- How long does shipping take?
- How do I reset my password?
- What is your return policy?
- Is my payment information secure?

---

## 📜 License

This project is licensed under the [MIT License](LICENSE).

You are free to use, modify, and distribute this project as you wish!

---

# 👋 Thank You

If you find this project useful, feel free to ⭐ star the repository and share it with others!
