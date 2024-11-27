package com.nguyengiap.security.auth.model.request_model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String account;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Integer fund;
}
