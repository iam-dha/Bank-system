package com.nguyengiap.security.model.request_model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BankingRequestOTP {
    private String toAccount;
    private double fund;
    private String message;
    private String otp;
}