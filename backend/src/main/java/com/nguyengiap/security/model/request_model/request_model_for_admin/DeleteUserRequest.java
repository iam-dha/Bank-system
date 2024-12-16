package com.nguyengiap.security.model.request_model.request_model_for_admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserRequest {
    private String account;
}
