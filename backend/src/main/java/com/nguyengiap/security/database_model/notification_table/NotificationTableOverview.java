package com.nguyengiap.security.database_model.notification_table;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_notificationOverview")
public class NotificationTableOverview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String message;
    private String imageUrl;
    private String dateTime;
}
