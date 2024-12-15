package com.nguyengiap.security.model.response_model;

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
    private long fund;
}