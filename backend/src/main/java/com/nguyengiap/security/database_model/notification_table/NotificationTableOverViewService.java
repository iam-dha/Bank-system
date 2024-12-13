package com.nguyengiap.security.database_model.notification_table;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationTableOverViewService {
    @Autowired
    private NotificationTableOverviewRepository notificationTableOverviewRepository;

    public void saveNotification(NotificationTableOverview notificationTableOverview) {
        notificationTableOverviewRepository.save(notificationTableOverview);
    }

    public List<NotificationTableOverview> findLatest10Notifications() {
        return notificationTableOverviewRepository.findLatest10Notifications();
    }
}
