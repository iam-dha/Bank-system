package com.nguyengiap.security.auth.model.request_model.response_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceWithAccount {
    private String account;
    private Integer fund;
}
