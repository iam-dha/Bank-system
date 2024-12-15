package com.nguyengiap.security.application_api.auth_api;

import com.nguyengiap.security.database_model.history_transistion.TransitionHistory;
import com.nguyengiap.security.database_model.notification_table.NotificationTableOverViewService;
import com.nguyengiap.security.database_model.notification_table.NotificationTableOverview;
import com.nguyengiap.security.database_model.user.User;
import com.nguyengiap.security.model.request_model.AuthenticationRequest;
import com.nguyengiap.security.model.request_model.BuffMoneyRequest;
import com.nguyengiap.security.model.request_model.FakeBillRequest;
import com.nguyengiap.security.model.request_model.ForgetPasswordOtpRequest;
import com.nguyengiap.security.model.request_model.OnlyAccountRequest;
import com.nguyengiap.security.model.request_model.RegisterRequest;
import com.nguyengiap.security.model.request_model.RegisterRequestOtp;
import com.nguyengiap.security.model.response_model.ChangePasswordSuccessfull;
import com.nguyengiap.security.model.response_model.UnauthorizedAccount;
import com.nguyengiap.security.service.auth_service.AuthenticationService;
import com.nguyengiap.security.service.otp_service.OtpService;
import com.nguyengiap.security.service.transition_history_service.TransitionHistoryService;
import com.nguyengiap.security.service.user_service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private final AuthenticationService authenticationService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final OtpService otpService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final TransitionHistoryService transitionHistoryService;

    @Autowired
    private final NotificationTableOverViewService notificationTableOverviewService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {
        Optional<User> user = userService.findByAccount(request.getAccount());
        if (user.isPresent()) {
            return ResponseEntity.status(401)
                    .body(UnauthorizedAccount.builder().status(401).message("Account is exist").build());
        }

        Optional<User> userByEmail = userService.findByEmail(request.getEmail());
        if (userByEmail.isPresent()) {
            return ResponseEntity.status(401)
                    .body(UnauthorizedAccount.builder().status(401).message("Email is exist").build());
        }

        otpService.generateOtp(request.getEmail(), "Mã xác thực đăng ký.");
        return ResponseEntity.ok(UnauthorizedAccount.builder().status(200).message("Success to send otp").build());
    }

    @PostMapping("/register-otp")
    public ResponseEntity<?> registerOtp(
            @RequestBody RegisterRequestOtp request) {
        if (otpService.validOtp(request.getEmail(), request.getOtp())) {
            return ResponseEntity.ok(authenticationService.register(request));
        } else {
            return ResponseEntity.status(401)
                    .body(UnauthorizedAccount.builder().status(401).message("Wrong otp").build());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request) {
        if (request.getAccount() == null) {
            return ResponseEntity.status(400)
                    .body(UnauthorizedAccount.builder().status(400).message("Account is required").build());
        }

        Optional<User> user;
        AuthenticationRequest newRequest;
        if (request.getAccount().contains("@")) {
            user = userService.findByEmail(request.getAccount());
        } else {
            user = userService.findByAccount(request.getAccount());
        }

        if (!user.isPresent()) {
            return ResponseEntity.status(401)
                    .body(UnauthorizedAccount.builder().status(401).message("Account not found").build());
        }

        if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            return ResponseEntity.status(403)
                    .body(UnauthorizedAccount.builder().status(403).message("Wrong password").build());
        }

        newRequest = AuthenticationRequest.builder()
                .account(user.get().getAccount())
                .password(request.getPassword())
                .build();

        return ResponseEntity.ok(authenticationService.authenticate(newRequest));
    }

    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(
            @RequestBody OnlyAccountRequest request) {
        if (request.getAccount() == null || request.getEmail() == null) {
            return ResponseEntity.status(400)
                    .body(UnauthorizedAccount.builder().status(400).message("Account and email are required").build());
        }

        Optional<User> user = userService.findByAccount(request.getAccount());
        if (!user.isPresent()) {
            return ResponseEntity.status(401)
                    .body(UnauthorizedAccount.builder().status(401).message("Account not found").build());
        }

        if (!user.get().getEmail().equals(request.getEmail())) {
            return ResponseEntity.status(401)
                    .body(UnauthorizedAccount.builder().status(401).message("Wrong email").build());
        }

        try {
            otpService.generateOtp(user.get().getEmail(), "Mã xác thực quên mật khẩu.");
            return ResponseEntity.ok(UnauthorizedAccount.builder()
                    .status(200)
                    .message("Request Otp successful")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(UnauthorizedAccount.builder()
                            .status(500)
                            .message("Error generating OTP: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/forget-password-otp")
    public ResponseEntity<?> forgetPasswordOtp(
            @RequestBody ForgetPasswordOtpRequest request) {
        Optional<User> user = userService.findByAccount(request.getAccount());

        if (user.isPresent()) {
            if (otpService.validOtp(user.get().getEmail(), request.getOtp())) {
                String newEncodedPassword = passwordEncoder.encode(request.getNewPassword());
                userService.changePassword(request.getAccount(), newEncodedPassword);
                return ResponseEntity
                        .ok(ChangePasswordSuccessfull.builder().message("Change password Successful")
                                .newPassword(request.getNewPassword()).build());
            } else {
                return ResponseEntity.status(401)
                        .body(UnauthorizedAccount.builder().status(401).message("Wrong otp").build());
            }
        } else {
            return ResponseEntity.status(401)
                    .body(UnauthorizedAccount.builder().status(401).message("Account not found").build());
        }
    }

    @PostMapping("/buff-money")
    public ResponseEntity<?> buffMoney(
            @RequestBody BuffMoneyRequest request) {
        Optional<User> user = userService.findByAccount(request.getAccount());
        if (user.isPresent()) {
            userService.bankingToAccount(request.getAccount(), request.getFund());
            return ResponseEntity
                    .ok(UnauthorizedAccount.builder().status(200).message("Buff money successful").build());
        } else {
            return ResponseEntity.status(401)
                    .body(UnauthorizedAccount.builder().status(401).message("Account not found").build());
        }
    }

    @PostMapping("/create-fake-transaction")
    public ResponseEntity<?> createFakeTransaction(@RequestBody FakeBillRequest request) {
        try {
            // Save the fake transaction directly  
            Optional<User> fromAccount = userService.findByAccount(request.getFromAccount());
            Optional<User> toAccount = userService.findByAccount(request.getToAccount());
            if (fromAccount.isPresent() && toAccount.isPresent()) {
                final TransitionHistory transitionHistory = TransitionHistory.builder()
                                                                .fromAccount(request.getFromAccount())
                                                                .toAccount(request.getToAccount())
                                                                .dateTime(request.getDateTime())
                                                                .balance(request.getBalance())
                                                                .fromUserName(fromAccount.get().getFirstName() + " " + fromAccount.get().getLastName())
                                                                .toUserName(toAccount.get().getFirstName() + " " + toAccount.get().getLastName())
                                                                .message(request.getMessage())
                                                                .build();
                transitionHistoryService.saveTransitionHistory(transitionHistory);
                return ResponseEntity.ok(
                        UnauthorizedAccount.builder().status(200).message("Fake transaction created successfully").build());
            } else {
                return ResponseEntity.status(401)
                        .body(UnauthorizedAccount.builder().status(401).message("Account not found").build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(UnauthorizedAccount.builder().status(500)
                    .message("Error creating fake transaction: " + e.getMessage()).build());
        }
    }

    @PostMapping("/notification")
    public ResponseEntity<?> notification(@RequestBody NotificationTableOverview request) {
        notificationTableOverviewService.saveNotification(request);
        return ResponseEntity.ok(UnauthorizedAccount.builder().status(200).message("Notification saved successfully").build());
    }

    @GetMapping("/get-notification")
    public ResponseEntity<?> getNotification() {
        return ResponseEntity.ok(notificationTableOverviewService.findLatest10Notifications());
    }
}
