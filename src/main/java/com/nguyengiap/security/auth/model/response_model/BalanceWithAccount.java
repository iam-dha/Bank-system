package com.nguyengiap.security.auth.model.response_model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceWithAccount {
    private String account;
    @Getter
    private Integer fund;
}