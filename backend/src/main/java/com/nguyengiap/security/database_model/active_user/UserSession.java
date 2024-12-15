package com.nguyengiap.security.database_model.active_user;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_sessions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSession {
    @Id
    @Column(nullable = false, unique = true)
    private String account;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private long balance;

    private LocalDateTime endTime;
}