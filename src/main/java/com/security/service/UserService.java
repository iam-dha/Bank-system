package com.security.service;

import com.security.user.User;
import com.security.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    public User saveOrUpdateUser(User user) {
        return userRepository.save(user);
    }
//    public Boolean ChangePassword(ChangePasswordRequest changePasswordRequest) {
//        User user =
//    }

}
