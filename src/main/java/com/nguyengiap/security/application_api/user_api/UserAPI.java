package com.nguyengiap.security.application_api.user_api;

import com.nguyengiap.security.config.jwt_config.JwtService;
import com.nguyengiap.security.database_model.user.User;
import com.nguyengiap.security.model.request_model.ChangePasswordRequest;
import com.nguyengiap.security.model.request_model.UpdateAddressRequest;
import com.nguyengiap.security.model.request_model.UpdateEmailRequest;
import com.nguyengiap.security.model.request_model.UpdatePhoneNumberRequest;
import com.nguyengiap.security.model.response_model.BalanceWithAccount;
import com.nguyengiap.security.model.response_model.FindByAccountResponse;
import com.nguyengiap.security.model.response_model.UnauthorizedAccount;
import com.nguyengiap.security.service.otp_service.OtpService;
import com.nguyengiap.security.service.user_service.UserService;

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

    @Autowired
    private JwtService jwtService;

    @Autowired
    private OtpService otpService;

    @PostMapping("/information")
    public ResponseEntity<?> getInformation(
            @RequestHeader("Authorization") String token) {
        final String account = jwtService.extractUserName(token.substring(7));
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
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, @RequestHeader("Authorization") String token) {
        final String account = jwtService.extractUserName(token.substring(7));
        Optional<User> user = userService.findByAccount(account);
        if (user.isPresent()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            // Lấy mật khẩu đã mã hóa từ cơ sở dữ liệu
            String encodedPassword = user.get().getPassword();

            // So sánh mật khẩu hiện tại
            if (passwordEncoder.matches(request.getCurrentPassword(), encodedPassword)) {
                // Mã hóa mật khẩu mới trước khi lưu vào DB
                String newEncodedPassword = passwordEncoder.encode(request.getNewPassword());
                userService.changePassword(account, newEncodedPassword);

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
    public ResponseEntity<?> checkProfileBalance(@RequestHeader("Authorization") String token) {
        final String account = jwtService.extractUserName(token.substring(7));
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

    @PostMapping("/update-phonenumber")
    public ResponseEntity<?> updatePhoneNumber(@RequestBody UpdatePhoneNumberRequest request, @RequestHeader("Authorization") String token) {
        String account = jwtService.extractUserName(token.substring(7));
        Optional<User> user = userService.findByAccount(account);
        if (!user.isPresent()) {
            return ResponseEntity.status(401)
                    .body(UnauthorizedAccount.builder().status(401).message("Account not found").build());
        } else {
            userService.updatePhoneNumber(account, request.getPhoneNumber());
            return ResponseEntity
                    .ok(UnauthorizedAccount.builder().status(200).message("Update Phone Number Successful").build());
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            // Extract account from token
            String account = jwtService.extractUserName(token.substring(7));
            
            // Generate expired token to invalidate current token
            String invalidToken = jwtService.generateExpiredToken(account);
            
            // Return success response
            return ResponseEntity.ok(UnauthorizedAccount.builder()
                    .status(200)
                    .message("Logout Successful")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(UnauthorizedAccount.builder()
                            .status(401)
                            .message("Invalid token")
                            .build());
        }
    }

    @PostMapping("/request-update-email")
    public ResponseEntity<?> requestUpdateEmail(@RequestHeader("Authorization") String token) {
        String account = jwtService.extractUserName(token.substring(7));
        Optional<User> user = userService.findByAccount(account);

        if (!user.isPresent()) {
            return ResponseEntity.status(401)
                    .body(UnauthorizedAccount.builder().status(401).message("Account not found").build());
        } else {
            otpService.generateOtp(user.get().getEmail(), "Update Email");
            return ResponseEntity
                    .ok(UnauthorizedAccount.builder().status(200).message("Request Update Email Successful").build());
        }
    }

    @PostMapping("/update-email")
    public ResponseEntity<?> updateEmail(@RequestBody UpdateEmailRequest request,
            @RequestHeader("Authorization") String token) {
        String account = jwtService.extractUserName(token.substring(7));
        Optional<User> user = userService.findByAccount(account);
        if (!user.isPresent()) {
            return ResponseEntity.status(401)
                    .body(UnauthorizedAccount.builder().status(401).message("Account not found").build());
        } else {
            if (!request.getIsRequest()) {
                otpService.generateOtp(request.getNewEmail(), "Update Email");
                return ResponseEntity.ok(
                        UnauthorizedAccount.builder().status(200).message("Request Update Email Successful").build());
            } else {
                if (otpService.validOtp(request.getNewEmail(), request.getOtp())) {
                    userService.updateEmail(account, request.getNewEmail());
                    return ResponseEntity
                            .ok(UnauthorizedAccount.builder().status(200).message("Update Email Successful").build());
                } else {
                    return ResponseEntity.status(401)
                            .body(UnauthorizedAccount.builder().status(401).message("OTP is incorrect").build());
                }
            }
        }
    }

    @PostMapping("/update-address")
    public ResponseEntity<?> updateAddress(@RequestBody UpdateAddressRequest request,
            @RequestHeader("Authorization") String token) {
        String account = jwtService.extractUserName(token.substring(7));
        Optional<User> user = userService.findByAccount(account);
        if (!user.isPresent()) {
            return ResponseEntity.status(401)
                    .body(UnauthorizedAccount.builder().status(401).message("Account not found").build());
        } else {
            userService.updateAddress(account, request.getAddress());
            return ResponseEntity
                    .ok(UnauthorizedAccount.builder().status(200).message("Update Address Successful").build());
        }
    }
}
