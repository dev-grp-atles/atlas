package tn.esprit.atlas.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

public class EmailService {

    private static final String SMTP_HOST = "smtp.gmail.com"; // Replace with your SMTP host
    private static final String SMTP_PORT = "587"; // Replace with your SMTP port
    private static final String EMAIL_USERNAME = "imem.playz@gmail.com"; // Replace with your email
    private static final String EMAIL_PASSWORD = "naellkfjcwwcckrt"; // Replace with your email password

    public static void sendEmail(String toEmail, String subject, String body) {
        // Set up mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            Transport.send(message);
            System.out.println("✅ Email sent successfully to: " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("❌ Failed to send email: " + e.getMessage());
        }
    }
}