package com.nguyengiap.security.service.user_session_service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nguyengiap.security.database_model.active_user.UserSession;
import com.nguyengiap.security.database_model.active_user.UserSessionRepository;

@Service
public class UserSessionService {
    @Autowired
    private UserSessionRepository sessionRepository;

    public void saveUserSession(String account, String firstName, String lastName, String email, String phoneNumber, long balance, LocalDateTime endTime) {
        UserSession session = new UserSession();
        session.setAccount(account);
        session.setFirstName(firstName);
        session.setLastName(lastName);
        session.setEmail(email);
        session.setPhoneNumber(phoneNumber);
        session.setBalance(balance);
        session.setEndTime(endTime);
        sessionRepository.save(session);
    }

    public List<UserSession> getUserSession() {
        return sessionRepository.findAll();
    }
}