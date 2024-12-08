package com.nguyengiap.security.auth.model.request_model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ForgetPasswordOtpRequest {
    private String account;
    private String newPassword;
    private String otp;
}