package com.nguyengiap.security;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.nguyengiap.security.database_model.user.Role;
import com.nguyengiap.security.database_model.user.User;
import com.nguyengiap.security.database_model.user.UserRepository;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class SecurityApplication {
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@PostConstruct
	public void init() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode("admin123");

		User admin = User.builder()
			.account("admin")
			.firstName("Admin")
			.lastName("User") 
			.email("admin@admin.com")
			.password(encodedPassword)
			.address("Admin Address")
			.createDate(LocalDateTime.now().toString())
			.phoneNumber("0123456789")
			.role(Role.ADMIN)
			.fund(1000000)
			.build();

		try {
			userRepository.save(admin);
			System.out.println("Admin account created successfully");
		} catch (Exception e) {
			System.out.println("Admin account already exists");
		}
	}
}
