package com.nguyengiap.security.database_model.notification;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_notification")
public class NotiticationTable {
    @Id
    @GeneratedValue
    private Long id;
    private String account;
    private String message;

    public NotiticationTable(String account, String message) {
        this.account = account;
        this.message = message;
    }
}
