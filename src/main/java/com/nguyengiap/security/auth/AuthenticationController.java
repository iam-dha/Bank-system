package com.nguyengiap.security.auth;

import com.nguyengiap.security.auth.model.request_model.AuthenticationRequest;
import com.nguyengiap.security.auth.model.request_model.ForgetPasswordOtpRequest;
import com.nguyengiap.security.auth.model.request_model.OnlyAccountRequest;
import com.nguyengiap.security.auth.model.request_model.RegisterRequest;
import com.nguyengiap.security.database_model.user.User;
import com.nguyengiap.security.service.EmailService;
import com.nguyengiap.security.service.OtpService;
import com.nguyengiap.security.service.UserService;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        Optional<User> user = userService.findByAccount(request.getAccount());

        if(user.isPresent()) {
            return ResponseEntity.status(403).body("Account is exist");
        }
        return ResponseEntity.ok(authenticationService.register(request));
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
}
