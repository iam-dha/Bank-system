package com.nguyengiap.security.service.auth_service;

import com.nguyengiap.security.config.jwt_config.JwtService;
import com.nguyengiap.security.database_model.user.Role;
import com.nguyengiap.security.database_model.user.User;
import com.nguyengiap.security.database_model.user.UserRepository;
import com.nguyengiap.security.model.request_model.AuthenticationRequest;
import com.nguyengiap.security.model.request_model.RegisterRequestOtp;
import com.nguyengiap.security.model.response_model.AuthenticationResponse;
import com.nguyengiap.security.service.user_session_service.UserSessionService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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

        private final UserSessionService userSessionService;

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
                var jwtToken = jwtService.generateTokenWithUserDetails(user);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .account(request.getAccount())
                                .role(Role.USER.name())
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getAccount(),
                                                request.getPassword()));
                var user = userRepository.findByAccount(request.getAccount())
                                .orElseThrow();
                var jwtToken = jwtService.generateTokenWithUserDetails(user);

                final String role = jwtService.extractRole(jwtToken);

                final Date expirationDate = jwtService.extractClaim(jwtToken, Claims::getExpiration);
                LocalDateTime endTime = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                if(role.equals("USER")) {
                        userSessionService.saveUserSession(user.getAccount(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getFund(), endTime);
                }

                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .account(user.getAccount())
                                .role(user.getRole().name())
                                .build();
        }
}
