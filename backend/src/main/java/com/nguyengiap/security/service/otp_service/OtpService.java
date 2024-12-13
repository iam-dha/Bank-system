package com.nguyengiap.security.service.otp_service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nguyengiap.security.service.email_service.EmailService;

import java.util.*;

@Service
@AllArgsConstructor
public class OtpService {
    private final Map<String, String> otpStorage = new HashMap<>();
    private final Random random = new Random();
    @Autowired
    private final EmailService emailService;

    public String generateOtp(String email, String subject) {
        String otp = String.format("%06d", random.nextInt(999999));
        otpStorage.put(email, otp);

        emailService.sendEmail(email, subject, "Mã xác thực OTP:" + otp);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                otpStorage.remove(email);
            }
        }, 30 * 60 * 1000);

        return otp;
    }

    public boolean validOtp(String email, String otp) {
        String storedOtp = otpStorage.get(email);
        return storedOtp != null && storedOtp.equals(otp);
    }
}
