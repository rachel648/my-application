package com.example.yogademoapp;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;




public class EmailSender {

    public static void sendEmail(String recipientEmail, String groupName) {
        String senderEmail = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getEmail() : null;

        // Ensure sender email is valid
        if (senderEmail == null) {
            Log.e("EmailSender", "Sender email is null. Cannot send email.");
            return;
        }

        final String senderPassword = "YOUR_APP_PASSWORD";  // You can use an app-specific password here

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, senderPassword);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Thank You for Joining " + groupName);
            message.setText("Thank you for joining the " + groupName + " group.\n\nWe have a meeting at 10 PM.\nJoin here: https://fake-meeting-link.com");

            Transport.send(message);
            Log.d("EmailSender", "Email sent successfully to: " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            Log.e("EmailSender", "Error sending email: " + e.getMessage());
        }
    }
}

