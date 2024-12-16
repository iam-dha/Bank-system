package com.nguyengiap.security.model.request_model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FakeBillRequest {
    private String fromAccount;
    private String toAccount;
    private long balance;
    private String dateTime;
    private String message;
}
