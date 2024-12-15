package com.nguyengiap.security.model.response_model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActiveUserResponse {
    private String account;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private long balance;
}
