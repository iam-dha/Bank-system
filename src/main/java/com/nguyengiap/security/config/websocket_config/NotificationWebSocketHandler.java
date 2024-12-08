package com.nguyengiap.security.config.websocket_config;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;

@Slf4j
public class NotificationWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        String userId = getUserIdFromSession(session);
        userSessions.put(userId, session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        String userId = getUserIdFromSession(session);
        userSessions.remove(userId);
    }

    public void sendNotificationToUser(String userId, String message) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getUserIdFromSession(WebSocketSession session) {
        if (session.getUri() == null || session.getUri().getQuery() == null) {
            return "default-user";
        }
        String query = session.getUri().getQuery();
        if (query.contains("=")) {
            return query.split("=")[1];
        }
        return "default-user";
    }

}