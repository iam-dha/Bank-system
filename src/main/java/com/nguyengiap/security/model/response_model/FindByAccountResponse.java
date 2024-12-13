package com.nguyengiap.security.model.response_model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FindByAccountResponse {
    private String account;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    private double fund;
}
