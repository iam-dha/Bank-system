package com.nguyengiap.security.service;

import com.nguyengiap.security.auth.model.response_model.BalanceWithAccount;
import com.nguyengiap.security.database_model.user.User;
import com.nguyengiap.security.database_model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByAccount(String account) {
        return userRepository.findByAccount(account);
    }

    public Optional<BalanceWithAccount> findBalanceByAccount(String account) {
        return userRepository.findBalanceByAccount(account);
    }

    public void bankingToAccount(String account, Integer fund) {
        userRepository.bankingToAccount(account, fund);
    }

    public void bankingToAccount2(String account, Integer fund) {
        userRepository.bankingToAccount2(account, fund);
    }

    public void changePassword(String account, String password) {
        userRepository.changePassword(account, password);
    }
}
