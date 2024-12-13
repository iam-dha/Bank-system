package com.nguyengiap.security.model.request_model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OnlyAccountRequest {
    private String account;
    private String email;
}
