package com.nguyengiap.security.application_api;

import com.nguyengiap.security.auth.model.request_model.ChangePasswordRequest;
import com.nguyengiap.security.auth.model.response_model.BalanceWithAccount;
import com.nguyengiap.security.auth.model.response_model.FindByAccountResponse;
import com.nguyengiap.security.auth.model.response_model.UnauthorizedAccount;
import com.nguyengiap.security.database_model.user.User;
import com.nguyengiap.security.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserAPI {
    @Autowired
    private UserService userService;

    @GetMapping("/information")
    public ResponseEntity<?> getInformation(
            @RequestParam String account) {
        Optional<User> users = userService.findByAccount(account);
        if (!users.isPresent()) {
            return ResponseEntity.status(401)
                    .body(UnauthorizedAccount.builder().status(401).message("Account not found").build());
        }

        FindByAccountResponse response = FindByAccountResponse.builder()
                .account(users.get().getAccount())
                .address(users.get().getAddress())
                .email(users.get().getEmail())
                .firstName(users.get().getFirstName())
                .lastName(users.get().getLastName())
                .phoneNumber(users.get().getPhoneNumber())
                .fund(users.get().getFund())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        Optional<User> user = userService.findByAccount(request.getAccount());
        if (user.isPresent()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            // Lấy mật khẩu đã mã hóa từ cơ sở dữ liệu
            String encodedPassword = user.get().getPassword();

            // So sánh mật khẩu hiện tại
            if (passwordEncoder.matches(request.getCurrentPassword(), encodedPassword)) {
                // Mã hóa mật khẩu mới trước khi lưu vào DB
                String newEncodedPassword = passwordEncoder.encode(request.getNewPassword());
                userService.changePassword(request.getAccount(), newEncodedPassword);

                return ResponseEntity.status(200)
                        .body(UnauthorizedAccount.builder().status(200).message("Change Password Successful").build());
            } else {
                return ResponseEntity.status(401)
                        .body(UnauthorizedAccount.builder().status(401).message("Password is incorrect").build());
            }
        } else {
            return ResponseEntity.status(401)
                    .body(UnauthorizedAccount.builder().status(401).message("Account not found").build());
        }
    }

    @GetMapping("/check-profile-balance")
    public ResponseEntity<?> checkProfileBalance(@RequestParam String account) {
        Optional<BalanceWithAccount> balance = userService.findBalanceByAccount(account);
        if (!balance.isPresent()) {
            return ResponseEntity.status(401)
                    .body(UnauthorizedAccount.builder()
                            .status(404)
                            .message("Not found")
                            .build());
        }
        return ResponseEntity.ok(balance.get());
    }
}
