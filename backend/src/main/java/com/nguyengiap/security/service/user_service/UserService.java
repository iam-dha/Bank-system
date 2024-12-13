package com.nguyengiap.security.service.user_service;

import com.nguyengiap.security.database_model.user.User;
import com.nguyengiap.security.database_model.user.UserRepository;
import com.nguyengiap.security.model.response_model.BalanceWithAccount;

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

    public void bankingToAccount(String account, double fund) {
        userRepository.bankingToAccount(account, fund);
    }

    public void bankingToAccount2(String account, double fund) {
        userRepository.bankingToAccount2(account, fund);
    }

    public void changePassword(String account, String password) {
        userRepository.changePassword(account, password);
    }

    public void updateAddress(String account, String address) {
        userRepository.updateAddress(account, address);
    }

    public void updateEmail(String account, String email) {
        userRepository.updateEmail(account, email);
    }

    public void updatePhoneNumber(String account, String phoneNumber) {
        userRepository.updatePhoneNumber(account, phoneNumber);
    }
}
