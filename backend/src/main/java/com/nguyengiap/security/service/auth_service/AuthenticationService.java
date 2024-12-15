package com.nguyengiap.security.service.auth_service;

import com.nguyengiap.security.config.jwt_config.JwtService;
import com.nguyengiap.security.database_model.user.Role;
import com.nguyengiap.security.database_model.user.User;
import com.nguyengiap.security.database_model.user.UserRepository;
import com.nguyengiap.security.model.request_model.AuthenticationRequest;
import com.nguyengiap.security.model.request_model.RegisterRequestOtp;
import com.nguyengiap.security.model.response_model.AuthenticationResponse;

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

        public AuthenticationResponse register(RegisterRequestOtp request) {
                var user = User.builder()
                                .account(request.getAccount())
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .email(request.getEmail())
                                .account(request.getAccount())
                                .address(request.getAddress())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .phoneNumber(request.getPhoneNumber())
                                .createDate(request.getCreateDate())
                                .role(Role.USER)
                                .fund(0)
                                .build();
                userRepository.save(user);
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .account(request.getAccount())
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getAccount(),
                                                request.getPassword()));
                var user = userRepository.findByAccount(request.getAccount())
                                .orElseThrow();
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .account(user.getAccount())
                                .role(user.getRole().name())
                                .build();
        }
}
