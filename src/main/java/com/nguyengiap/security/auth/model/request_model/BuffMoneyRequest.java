package com.nguyengiap.security.auth.model.request_model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuffMoneyRequest {
    private String account;
    private Integer fund;
} 