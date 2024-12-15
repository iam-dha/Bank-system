package com.nguyengiap.security.model.request_model.request_model_for_admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserEmail {
    private String account;
    private String email;
}
