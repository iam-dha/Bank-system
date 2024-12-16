package com.nguyengiap.security.service.user_session_service;

import java.util.List;
import org.springframework.stereotype.Component;
import com.nguyengiap.security.database_model.active_user.UserSession;
import com.nguyengiap.security.database_model.active_user.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

@Component
public class SessionCleanupTask {
    @Autowired
    private UserSessionRepository sessionRepository;

    @Scheduled(fixedRate = 60000) 
    public void cleanupExpiredSessions() {
        List<UserSession> sessions = sessionRepository.findAll();
        for (UserSession session : sessions) {
            if (session.getEndTime().isBefore(java.time.LocalDateTime.now())) {
                sessionRepository.delete(session);
            }
        }
    }
}
