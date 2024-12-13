package com.nguyengiap.security.service.notification_service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private String account;
    private String title;
    private String content;
}