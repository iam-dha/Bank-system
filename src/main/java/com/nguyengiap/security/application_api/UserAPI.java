package com.nguyengiap.security.application_api;

import com.nguyengiap.security.auth.model.request_model.ChangePasswordRequest;
import com.nguyengiap.security.auth.model.response_model.BalanceWithAccount;
import com.nguyengiap.security.database_model.user.User;
import com.nguyengiap.security.service.EmailService;
import com.nguyengiap.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserAPI {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/list_account_by_email")
    public ResponseEntity<?> getListAccountWithEmail(
            @RequestParam String email
    ) {
        List<User> users = userService.findAccountByEmail(email);
        if (users.isEmpty()) {
            return ResponseEntity.status(404).body("No account found");
        }
        return ResponseEntity.ok(users);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        Optional<User> user = userService.findByAccount(request.getAccount());
        if (user.isPresent()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            // Lấy mật khẩu đã mã hóa từ cơ sở dữ liệu
            String encodedPassword = user.get().getPassword();

            // So sánh mật khẩu hiện tại
            if (passwordEncoder.matches(request.getCurrentPassword(), encodedPassword)) {
                // Mã hóa mật khẩu mới trước khi lưu vào DB
                String newEncodedPassword = passwordEncoder.encode(request.getNewPassword());
                userService.changePassword(request.getAccount(), newEncodedPassword);

                return ResponseEntity.ok("Change Password Successful");
            } else {
                return ResponseEntity.status(403).body("Password is incorrect");
            }
        } else {
            return ResponseEntity.status(403).body("Account not found");
        }
    }

    @GetMapping("/check-profile-balance")
    public ResponseEntity<?> checkProfileBalance(@RequestParam String account) {
        Optional<BalanceWithAccount> balance = userService.findBalanceByAccount(account);
        return balance.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/send-email")
    public String sendEmail() {
        emailService.sendEmail("giapbacvan@gmail.com", "Subject of the email", "Content of the email");
        return "Email sent successfully!";
    }
}
