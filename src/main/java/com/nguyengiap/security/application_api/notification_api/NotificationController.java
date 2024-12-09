package com.nguyengiap.security.application_api.notification_api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

import com.nguyengiap.security.service.notification_service.NotificationDTO;
import com.nguyengiap.security.service.notification_service.NotificationService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/auth")
@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/push")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationDTO request) {
        notificationService.sendNotificationToUser(request.getAccount(), request.getTitle(), request.getContent());
        return ResponseEntity.ok().build();
    }
}
