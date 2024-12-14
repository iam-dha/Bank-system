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
    private double month;
    @Column(columnDefinition = "integer default 0")
    private double expense;
    @Column(columnDefinition = "integer default 0")
    private double income;
}

