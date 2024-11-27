package com.nguyengiap.security.application_api;

import com.nguyengiap.security.auth.model.request_model.response_model.BalanceWithAccount;
import com.nguyengiap.security.service.UserService;
import com.nguyengiap.security.user.User;
import com.nguyengiap.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BankService {
    final UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/api/v1/check-profile-balance")
    public Optional<BalanceWithAccount> checkProfileBalance(
            @RequestParam String account
            ) {
        return userRepository.findBalanceByAccount(account);
    }

    @GetMapping("/api/v1/list_account_by_email")
    public ResponseEntity<?> getListAccountWithEmail(
            @RequestParam String email
    ) {
        List<User> users = userService.findAccountByEmail(email);
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }
}

