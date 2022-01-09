package com.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author Martin
 */
public class SendMail {
    /** main
     * @param title Titre du mail
     * @param txt contenu du mail
     * @param to destinataire
     * Envoie un mail au destinataire avec les différentes informations
     */
    public static void main(String title, String txt, String to) {

        final String username = "projetassociationapp3@gmail.com";
        final String password = "ProjetAPP3!";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("projetassociationapp3@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject(title);
            message.setText(txt);

            Transport.send(message);

            System.out.println("Un email vient d'être envoyé avec succès.");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'envoie du mail.");
        }
    }

}