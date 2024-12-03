package com.nguyengiap.security.service;

import com.nguyengiap.security.config.MailConfig;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

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
