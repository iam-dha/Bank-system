package com.nguyengiap.security.model.request_model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
}
