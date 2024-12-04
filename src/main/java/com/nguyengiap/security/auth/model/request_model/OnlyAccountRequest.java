package com.nguyengiap.security.auth.model.request_model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OnlyAccountRequest {
    private String account;
}
