package com.nguyengiap.security.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @GetMapping("/api/v1/demo-controller")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello World!");
    }
}
