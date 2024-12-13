package com.nguyengiap.security.application_api.admin_api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class TranferBetweenUser {
    private String fromAccount;
    private String toAccount;
    private double fund;
}