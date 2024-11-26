package com.nguyengiap.security.demo;

import com.nguyengiap.security.user.User;
import com.nguyengiap.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class DemoController {
    private final UserRepository userRepository;

    @GetMapping("/api/v1/demo-controller")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello World!");
    }

    @GetMapping("/dkm/ducngu")
    public Optional<User> checkProfile(
            @RequestParam String email
    ) {
        return userRepository.findByEmail(email);
    }
}
