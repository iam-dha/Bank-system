package com.nguyengiap.security.application_api.admin_api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddFundToAccount {
    private String account;
    private long fund;
}
