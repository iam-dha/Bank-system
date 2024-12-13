package com.nguyengiap.security.database_model.history_transistion;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_transitionHistory")
public class TransitionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fromAccount;
    private String toAccount;
    private int balance;
    private String dateTime;
    private String time;
    private String fromUserName;
    private String toUserName;
    private String message;
}
