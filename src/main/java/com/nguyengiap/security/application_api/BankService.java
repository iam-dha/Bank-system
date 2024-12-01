package com.nguyengiap.security.application_api;

import com.nguyengiap.security.auth.model.request_model.BankingRequest;
import com.nguyengiap.security.auth.model.request_model.response_model.BalanceWithAccount;
import com.nguyengiap.security.service.UserService;
import com.nguyengiap.security.database_model.user.User;
import com.nguyengiap.security.database_model.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BankService {
    final UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/api/v1/check-profile-balance")
    public ResponseEntity<?> checkProfileBalance(@RequestParam String account) {
        Optional<BalanceWithAccount> balance = userRepository.findBalanceByAccount(account);
        return balance.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/api/v1/list_account_by_email")
    public ResponseEntity<?> getListAccountWithEmail(
            @RequestParam String email
    ) {
        List<User> users = userService.findAccountByEmail(email);
        if (users.isEmpty()) {
            return ResponseEntity.status(404).body("No account found");
        }
        return ResponseEntity.ok(users);
    }

    @Transactional
    @PostMapping("/api/v1/banking")
    public ResponseEntity<?> banking(@RequestBody BankingRequest request) {
        Optional<User> checkFromAccount = userRepository.findByAccount(request.getFromAccount());
        Optional<User> checkToAccount = userRepository.findByAccount(request.getToAccount());
        if (checkFromAccount.isEmpty() || checkToAccount.isEmpty()) {
            return ResponseEntity.status(404).body("Account not found");
        } else {
            Optional<BalanceWithAccount> checkFromAccountBalance = userRepository.findBalanceByAccount(request.getFromAccount());
            if(checkFromAccountBalance.isPresent()) {
                if (checkFromAccountBalance.get().getFund() >= request.getFund()) {
                    userRepository.bankingToAccount(request.getToAccount(), request.getFund());
                    return ResponseEntity.ok("Banking successful");
                } else {
                    return ResponseEntity.status(401).body("Not enough money");
                }
            } else {
                return ResponseEntity.status(401).body("Something error");
            }
        }
    }

}

