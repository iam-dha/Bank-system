package com.security.service;

import com.security.repository.UserRepository;
import com.security.request.ChangePasswordRequest;
import com.security.user.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ChangePasswordService {
    private final UserRepository userRepository;

    public String ChangePassword(ChangePasswordRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if(!user.isPresent()) return "Email address invalid";
        User updatedUser = user.get();
        updatedUser.setPassword(request.getNewPassword());
        userRepository.save(updatedUser);
        return "Password changed successfully";
    }

}
