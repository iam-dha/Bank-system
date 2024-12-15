package com.nguyengiap.security.service.user_session_service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import com.nguyengiap.security.database_model.active_user.UserSession;
import com.nguyengiap.security.database_model.active_user.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.List;

@Component
public class SessionCleanupTask {
    @Autowired
    private UserSessionRepository sessionRepository;

    @Scheduled(fixedRate = 300000)
    public void cleanupExpiredSessions() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(30);
        List<UserSession> sessions = sessionRepository.findByIsActiveTrue();
        
        sessions.stream()
            .filter(session -> session.getLastActivity().isBefore(cutoffTime))
            .forEach(session -> {
                session.setActive(false);
                sessionRepository.save(session);
            });
    }
}
