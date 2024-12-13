package com.nguyengiap.security.config.websocket_config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.nguyengiap.security.database_model.notification.NotificationTableService;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final NotificationTableService notificationTableService;

    public WebSocketConfig(NotificationTableService notificationTableService) {
        this.notificationTableService = notificationTableService;
    }

    /*
     * http://localhost:8080/notifications
     * @param WebSocketHandlerRegistry
     */

    @Override
    public void registerWebSocketHandlers(@SuppressWarnings("null") WebSocketHandlerRegistry registry) {
        registry.addHandler(getNotificationWebSocketHandler(), "/notifications")
                .setAllowedOrigins("*");
    }

    @Bean
    NotificationWebSocketHandler getNotificationWebSocketHandler() {
        return new NotificationWebSocketHandler(notificationTableService);
    }
}

