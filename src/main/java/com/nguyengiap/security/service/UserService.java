package com.nguyengiap.security.service;

import com.nguyengiap.security.user.User;
import com.nguyengiap.security.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAccountByEmail(String email) {
        return userRepository.findAccountByEmail(email);
    }
}
