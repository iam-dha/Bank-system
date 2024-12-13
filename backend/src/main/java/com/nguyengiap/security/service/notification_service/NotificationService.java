package com.nguyengiap.security.service.notification_service;

import org.springframework.stereotype.Service;

import com.nguyengiap.security.config.websocket_config.NotificationWebSocketHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationWebSocketHandler notificationWebSocketHandler;

    public void sendNotificationToUser(String account, String title, String content) {
        NotificationDTO notification = new NotificationDTO(account, title, content);
        String notificationMessage = convertToJson(notification);
        notificationWebSocketHandler.sendNotificationToUser(account, notificationMessage);
    }

    private String convertToJson(NotificationDTO notification) {
        try {
            return new ObjectMapper().writeValueAsString(notification);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting notification to JSON", e);
        }
    }
}

