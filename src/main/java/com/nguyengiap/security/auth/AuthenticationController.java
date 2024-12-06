package com.nguyengiap.security.auth;

import com.nguyengiap.security.auth.model.request_model.AuthenticationRequest;
import com.nguyengiap.security.auth.model.request_model.ForgetPasswordOtpRequest;
import com.nguyengiap.security.auth.model.request_model.OnlyAccountRequest;
import com.nguyengiap.security.auth.model.request_model.RegisterRequest;
import com.nguyengiap.security.auth.model.request_model.RegisterRequestOtp;
import com.nguyengiap.security.auth.model.request_model.BuffMoneyRequest;
import com.nguyengiap.security.database_model.history_transistion.TransitionHistory;
import com.nguyengiap.security.database_model.user.User;
import com.nguyengiap.security.service.OtpService;
import com.nguyengiap.security.service.UserService;
import com.nguyengiap.security.service.TransitionHistoryService;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        Optional<User> user = userService.findByAccount(request.getAccount());

        if(user.isPresent()) {
            return ResponseEntity.status(403).body("Account is exist");
        }
        else {
            otpService.generateOtp(request.getEmail());
            return ResponseEntity.ok("Success to send otp");
        }
    }

    @PostMapping("/register-otp")
    public ResponseEntity<?> registerOtp(
            @RequestBody RegisterRequestOtp request
    ) {
        if(otpService.validOtp(request.getEmail(), request.getOtp())) {
            return ResponseEntity.ok(authenticationService.register(request));
        } else {
            return ResponseEntity.status(403).body("Wrong otp");
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword (
        @RequestBody OnlyAccountRequest request
    ) {
        Optional<User> user = userService.findByAccount(request.getAccount());
        if(user.isPresent()) {
            otpService.generateOtp(user.get().getEmail());
            return ResponseEntity.ok("Request Otp");
        } else {
            return ResponseEntity.status(403).body("Account not found");
        }
    }

    @PostMapping("/forget-password-otp")
    public ResponseEntity<?> forgetPasswordOtp(
            @RequestBody ForgetPasswordOtpRequest request
    ) {
        Optional<User> user = userService.findByAccount(request.getAccount());

        if(user.isPresent()) {
            if(otpService.validOtp(user.get().getEmail(), request.getOtp())) {
                String newEncodedPassword = passwordEncoder.encode(request.getNewPassword());
                userService.changePassword(request.getAccount(), newEncodedPassword);
                return ResponseEntity.ok("Change password Successful");
            } else {
                return ResponseEntity.status(403).body("Wrong otp");
            }
        } else {
            return ResponseEntity.status(403).body("Account not found");
        }
    }

    @PostMapping("/buff-money")
    public ResponseEntity<?> buffMoney(
            @RequestBody BuffMoneyRequest request
    ) {
        Optional<User> user = userService.findByAccount(request.getAccount());
        if(user.isPresent()) {
            userService.bankingToAccount(request.getAccount(), request.getFund());
            return ResponseEntity.ok("Buff money successful");
        } else {
            return ResponseEntity.status(403).body("Account not found");
        }
    }

        @PostMapping("/create-fake-transaction")
    public ResponseEntity<?> createFakeTransaction(@RequestBody TransitionHistory request) {
        try {
            // Save the fake transaction directly
            transitionHistoryService.saveTransitionHistory(request);
            return ResponseEntity.ok("Fake transaction created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating fake transaction: " + e.getMessage());
        }
    }
}
