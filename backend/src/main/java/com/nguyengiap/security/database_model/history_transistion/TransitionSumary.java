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
public class TransitionSumary {
    @Id
    private int month;
    @Column(columnDefinition = "integer default 0")
    private long expense;
    @Column(columnDefinition = "integer default 0")
    private long income;
}

