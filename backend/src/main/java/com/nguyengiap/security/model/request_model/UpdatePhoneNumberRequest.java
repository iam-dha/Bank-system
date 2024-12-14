package com.nguyengiap.security.model.request_model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePhoneNumberRequest {
    private String account;
    private String phoneNumber;
}
