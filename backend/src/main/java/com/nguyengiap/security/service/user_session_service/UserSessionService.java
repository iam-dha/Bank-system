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

    public void saveUserSession(String username, String token) {
        UserSession session = new UserSession();
        session.setUsername(username);
        session.setToken(token);
        session.setLastActivity(LocalDateTime.now());
        session.setActive(true);
        sessionRepository.save(session);
    }

    public void deactivateSession(String token) {
        sessionRepository.findByToken(token)
            .ifPresent(session -> {
                session.setActive(false);
                sessionRepository.save(session);
            });
    }

    public List<UserSession> getActiveSessions() {
        return sessionRepository.findByIsActiveTrue();
    }

    public void updateLastActivity(String token) {
        sessionRepository.findByToken(token)
            .ifPresent(session -> {
                session.setLastActivity(LocalDateTime.now());
                sessionRepository.save(session);
            });
    }
}