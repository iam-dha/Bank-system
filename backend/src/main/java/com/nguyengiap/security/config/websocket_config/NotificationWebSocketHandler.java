package com.nguyengiap.security.config.websocket_config;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.nguyengiap.security.database_model.notification.NotiticationTable;
import com.nguyengiap.security.database_model.notification.NotificationTableService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;

@Slf4j
public class NotificationWebSocketHandler extends TextWebSocketHandler {
    private final NotificationTableService notificationTableService;

    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    public NotificationWebSocketHandler(NotificationTableService notificationTableService) {
        this.notificationTableService = notificationTableService;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        String userId = getUserIdFromSession(session);
        userSessions.put(userId, session);
        List<NotiticationTable> notiticationTables = notificationTableService.getMessageWithAccount(userId);
        if (notiticationTables != null && !notiticationTables.isEmpty()) {
            for (NotiticationTable notiticationTable : notiticationTables) {
                sendNotificationToUser(userId, notiticationTable.getMessage());
            }
            notificationTableService.deleteMessageWithAccount(userId);
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        String userId = getUserIdFromSession(session);
        userSessions.remove(userId);
    }

    public void sendNotificationToUser(String account, String message) {
        WebSocketSession session = userSessions.get(account);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            NotiticationTable notiticationTable = new NotiticationTable(account, message);
            notificationTableService.saveMessage(notiticationTable);
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