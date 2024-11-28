package com.security.controller;

import com.security.service.UserService;
import com.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class DemoController {
    private final UserRepository userRepository;
    private final UserService userService;

//    @GetMapping("/api/v1/demo-controller")
//    public ResponseEntity<String> hello() {
//        return ResponseEntity.ok("Hello World!");
//    }

//    @PostMapping("api/v1/changePassword")
//    public Optional<User> changePassword (
//            @RequestBody User user
//    ) {
//        user.setPassword();
//    }
//    @GetMapping("/dkm/ducngu")
//    public Optional<User> checkProfile(
//            @RequestParam String email
//    ) {
//        return userRepository.findByEmail(email);
//    }
//    @PostMapping("/api/v1/change-password")
//    public ResponseEntity<AuthenticationResponse> changePass(
//            @RequestBody AuthenticationRequest request
//    ) {
//        return ResponseEntity.ok(authenticationService.authenticate(request));
//    }
}
