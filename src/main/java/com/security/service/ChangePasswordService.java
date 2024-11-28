package com.security.service;

import com.security.repository.UserRepository;
import com.security.request.ChangePasswordRequest;
import com.security.user.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ChangePasswordService {
    private final UserRepository userRepository;

    public Boolean ChangePassword(ChangePasswordRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        User updatedUser = user.get();
        updatedUser.setPassword(request.getNewPassword());
        userRepository.save(updatedUser);
        return true;
    }

}
