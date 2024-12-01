package com.nguyengiap.security.auth.model.request_model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BankingRequest {
    private String fromAccount;
    private String toAccount;
    private Integer fund;
    private String message;
}
