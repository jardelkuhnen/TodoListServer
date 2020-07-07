package com.tasks.emailsender.service;

import com.tasks.emailsender.property.EmailProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@Service
public class SendEmailService {

    private final EmailProperties emailProperties;

    @Autowired
    public SendEmailService(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    public Boolean sendEmail(String destination, String subject, String text) {

        log.info("Sending email from " + emailProperties.getEmail() + " to " + destination);

        Session session = configureSession();

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailProperties.getEmail()));

            Address[] toUser = InternetAddress.parse(destination);
            Address[] userCC = InternetAddress.parse(emailProperties.getEmail());

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setRecipients(Message.RecipientType.CC, userCC);
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);

            log.info("Sended email to " + destination);

        } catch (Exception e) {
            log.error("Error while sending email: " + e.getMessage());
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    private Session configureSession() {

        Properties props = new Properties();

        /** Parâmetros de conexão com servidor Gmail */
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailProperties.getEmail(), emailProperties.getPassword());
                    }
                });

        /** Ativa Debug para sessão */
        session.setDebug(true);

        return session;
    }

}
