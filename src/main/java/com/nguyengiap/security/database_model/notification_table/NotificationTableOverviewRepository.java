package com.nguyengiap.security.database_model.notification_table;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface NotificationTableOverviewRepository extends JpaRepository<NotificationTableOverview, Integer> {
    @Query("SELECT n FROM NotificationTableOverview n ORDER BY TO_DATE(n.dateTime, 'DD/MM/YYYY') DESC")
    List<NotificationTableOverview> findLatest10Notifications();
}
