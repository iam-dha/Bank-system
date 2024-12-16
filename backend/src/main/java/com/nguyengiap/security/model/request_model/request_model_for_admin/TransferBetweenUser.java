package com.nguyengiap.security.model.request_model.request_model_for_admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransferBetweenUser {
    private String fromAccount;
    private String toAccount;
    private String message;
    private long fund;
}