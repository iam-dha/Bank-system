package com.nguyengiap.security.service.email_service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.nguyengiap.security.config.mail_config.MailConfig;

@Service
@AllArgsConstructor
public class EmailService {
    @Autowired
    final MailConfig mailConfig;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("giaphocdoi@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailConfig.javaMailSender().send(message);
        } catch (Exception e) {
            System.out.print("Error sending email" + e.getMessage());
        }
    }

}
