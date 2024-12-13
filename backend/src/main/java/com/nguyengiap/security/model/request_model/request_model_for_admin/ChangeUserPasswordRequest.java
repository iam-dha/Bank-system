package com.nguyengiap.security.model.request_model.request_model_for_admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeUserPasswordRequest {
    private String account;
    private String password;
}
