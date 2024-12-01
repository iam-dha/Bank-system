package com.nguyengiap.security.auth;

import com.nguyengiap.security.auth.model.request_model.AuthenticationRequest;
import com.nguyengiap.security.auth.model.request_model.RegisterRequest;
import com.nguyengiap.security.config.JwtService;
import com.nguyengiap.security.database_model.history_transistion.TransitionHistory;
import com.nguyengiap.security.database_model.history_transistion.TransitionHistoryRepository;
import com.nguyengiap.security.database_model.user.Role;
import com.nguyengiap.security.database_model.user.User;
import com.nguyengiap.security.database_model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .account(request.getAccount())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .account(request.getAccount())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .createDate(request.getCreateDate())
                .role(Role.USER)
                .fund(request.getFund())
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getAccount(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByAccount(request.getAccount())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
