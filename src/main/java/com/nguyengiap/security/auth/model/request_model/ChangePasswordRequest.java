package com.nguyengiap.security.auth.model.request_model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChangePasswordRequest {
    private String account;
    private String currentPassword;
    private String newPassword;
}
