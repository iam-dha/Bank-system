package com.nguyengiap.security.model.request_model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateEmailRequest {
    private String account;
    private boolean isRequest;
    private String newEmail;
    private String otp;

    public boolean getIsRequest() {
        return isRequest;
    }

}

