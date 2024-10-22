package com.servlet;

import jakarta.mail.*; // Updated import to Jakarta Mail
import jakarta.mail.internet.*; // Updated import to Jakarta Mail
import java.util.Properties;

public class MailUtility {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USER = "tsai73963@gmail.com";
    private static final String SMTP_PASSWORD = "feld oaio pcak plxo";
    
    public static void sendOrderConfirmation(String toEmail, String userName, String orderDetails) throws MessagingException {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SMTP_USER));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject("Order Confirmation");

        String emailContent = "<html><body>"
                + "<h3>Hello " + userName + ",</h3>"
                + "<p>Thank you for your order! Here are the details:</p>"
                + "<p>" + orderDetails + "</p>"
                + "<p>We will notify you once your order is shipped.</p>"
                + "<br><p>Best Regards,</p>"
                + "<p>The RevShop Team</p>"
                + "</body></html>";

        message.setContent(emailContent, "text/html");

        Transport.send(message);
        System.out.println("Order confirmation email sent successfully to " + toEmail);
    }
}
