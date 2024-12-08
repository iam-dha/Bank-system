package com.nguyengiap.security.auth.model.response_model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnauthorizedAccount {
    private int status;
    private String message;
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
