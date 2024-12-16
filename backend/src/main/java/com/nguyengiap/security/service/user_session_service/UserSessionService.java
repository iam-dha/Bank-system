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

    public void saveUserSession(String account, LocalDateTime endTime) {
        UserSession session = new UserSession();
        session.setAccount(account);
        session.setEndTime(endTime);
        sessionRepository.save(session);
    }

    public List<UserSession> getUserSession() {
        return sessionRepository.findAll();
    }
}