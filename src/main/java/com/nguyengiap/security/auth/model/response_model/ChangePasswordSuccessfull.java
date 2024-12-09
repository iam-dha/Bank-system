package com.nguyengiap.security.auth.model.response_model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ChangePasswordSuccessfull {
    private String message;
    private String newPassword;
}
