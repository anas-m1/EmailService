package com.example.emailservice.Consumers;

import com.example.emailservice.dtos.SendEmailDTO;
import com.example.emailservice.utils.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

@Service
public class SendEmailConsumer {

    ObjectMapper objectMapper=new ObjectMapper();

    @Value("${gmailSenderAppPassword}")
    String gmailSenderAppPassword;

    @KafkaListener(topics="sendEmail", groupId = "emailService")
    public void sendEmail(String message) {
        System.out.println("Sending email: " + message);
        // Implement actual email sending logic here
        SendEmailDTO sendEmailDTO;
        try {
            sendEmailDTO = objectMapper.readValue(message, SendEmailDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("anas.me.xyz@gmail.com", gmailSenderAppPassword);
            }
        };
        Session session = Session.getInstance(props, auth);

        EmailUtil.sendEmail(session, sendEmailDTO.getTo(), sendEmailDTO.getSubject(), sendEmailDTO.getBody());
    }

}