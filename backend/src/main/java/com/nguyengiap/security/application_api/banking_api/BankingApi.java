package com.nguyengiap.security.application_api.banking_api;

import com.nguyengiap.security.config.jwt_config.JwtService;
import com.nguyengiap.security.database_model.history_transistion.TransitionHistory;
import com.nguyengiap.security.database_model.history_transistion.TransitionSumary;
import com.nguyengiap.security.service.email_service.EmailService;
import com.nguyengiap.security.service.notification_service.NotificationService;
import com.nguyengiap.security.service.otp_service.OtpService;
import com.nguyengiap.security.service.transition_history_service.TransitionHistoryService;
import com.nguyengiap.security.service.transition_history_service.TransitionSummaryService;
import com.nguyengiap.security.service.user_service.UserService;
import com.nguyengiap.security.database_model.user.User;
import com.nguyengiap.security.model.request_model.BankingRequest;
import com.nguyengiap.security.model.request_model.BankingRequestOTP;
import com.nguyengiap.security.model.response_model.BalanceWithAccount;
import com.nguyengiap.security.model.response_model.UnauthorizedAccount;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bank-api")
public class BankingApi {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TransitionHistoryService transitionHistoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TransitionSummaryService transitionSummaryService;

    @Transactional
    @PostMapping("/banking")
    public ResponseEntity<?> banking(@RequestBody BankingRequest request,
            @RequestHeader("Authorization") String token) {

        final String account = jwtService.extractUserName(token.substring(7));

        // Kiểm tra xem tài khoản token và request có khớp không
        if (!account.equals(request.getFromAccount())) {
            return ResponseEntity.status(403)
                    .body(UnauthorizedAccount.builder().status(403).message("Unauthorized access").build());
        }

        // Kiểm tra xem tài khoản có tồn tại không
        Optional<User> checkFromAccount = userService.findByAccount(account);
        Optional<User> checkToAccount = userService.findByAccount(request.getToAccount());

        if (!checkFromAccount.isPresent() || !checkToAccount.isPresent()) {
            return ResponseEntity.status(404)
                    .body(UnauthorizedAccount.builder().status(404).message("Account not found").build());
        } else {
            if (checkFromAccount.get().getFund() < request.getFund()) {
                return ResponseEntity.status(401)
                        .body(UnauthorizedAccount.builder().status(401).message("Not enough money").build());
            } else {
                otpService.generateOtp(checkFromAccount.get().getEmail(), "Mã xác thực chuyển khoản.");
                return ResponseEntity.status(200)
                        .body(UnauthorizedAccount.builder().status(200).message("Success sent otp").build());
            }
        }
    }

    @PostMapping("/banking-otp")
    public ResponseEntity<?> bankingOTP(@RequestBody BankingRequestOTP request,
            @RequestHeader("Authorization") String token) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = now.format(formatter);

        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedHour = now.format(formatterTime);

        final String account = jwtService.extractUserName(token.substring(7));

        // Kiểm tra xem tài khoản token và request có khớp không
        if (!account.equals(request.getFromAccount())) {
            return ResponseEntity.status(403)
                    .body(UnauthorizedAccount.builder().status(403).message("Unauthorized access").build());
        }

        // Kiểm tra xem tài khoản có tồn tại không
        Optional<User> checkFromAccount = userService.findByAccount(account);
        Optional<User> checkToAccount = userService.findByAccount(request.getToAccount());

        // Nếu không tồn tại
        if (checkFromAccount.isEmpty() || checkToAccount.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(UnauthorizedAccount.builder().status(404).message("Account not found").build());
        } else { // Nếu tồn tại
            if (otpService.validOtp(checkFromAccount.get().getEmail(), request.getOtp())) {
                // Tra cứu số dư tài khoản gửi
                Optional<BalanceWithAccount> checkFromAccountBalance = userService
                        .findBalanceByAccount(account);
                if (checkFromAccountBalance.isPresent()) {
                    // Nếu số tiền còn lại >= số tiền cần gửi
                    if (checkFromAccountBalance.get().getFund() >= request.getFund()) {
                        // Tạo truy vấn chuyển tiền(cộng tiền)
                        userService.bankingToAccount(request.getToAccount(), request.getFund());
                        // Trừ tiền
                        userService.bankingToAccount2(account, request.getFund());

                        // Tên người chuyển
                        String fromUserName = checkFromAccount.get().getFirstName() + " "
                                + checkFromAccount.get().getLastName();
                        String toUserName = checkToAccount.get().getFirstName() + " "
                                + checkToAccount.get().getLastName();

                        Optional<BalanceWithAccount> checkFromAccountRemainBalance = userService
                                .findBalanceByAccount(account);
                        Optional<BalanceWithAccount> checkToAccountRemainBalance = userService
                                .findBalanceByAccount(request.getToAccount());
                        // Check số tiền còn lại của người chuyển
                        String fromUserNotification = notificationFormart(request.getFund(), account,
                                request.getMessage(), checkFromAccountRemainBalance.get().getFund(), formattedDate,
                                formattedHour, true);
                        String toUserNotification = notificationFormart(request.getFund(), request.getToAccount(),
                                request.getMessage(), checkToAccountRemainBalance.get().getFund(), formattedDate,
                                formattedHour, false);

                        // Thông báo biến động số dư qua WebSocket
                        notificationService.sendNotificationToUser(request.getToAccount(), "Thông báo biến động số dư",
                                toUserNotification);
                        notificationService.sendNotificationToUser(account,
                                "Thông báo biến động số dư", fromUserNotification);

                        // Gửi email thông báo biến động số dư
                        emailService.sendEmail(checkFromAccount.get().getEmail(), "Thông báo biến động số dư",
                                fromUserNotification);
                        emailService.sendEmail(checkToAccount.get().getEmail(), "Thông báo biến động số dư",
                                toUserNotification);

                        var transitionHistory = TransitionHistory.builder()
                                .fromAccount(account)
                                .toAccount(request.getToAccount())
                                .message(request.getMessage())
                                .balance(request.getFund())
                                .dateTime(formattedDate)
                                .fromUserName(fromUserName)
                                .toUserName(toUserName)
                                .time(formattedHour)
                                .build();
                        // Lưu giao dịch
                        transitionHistoryService.saveTransitionHistory(transitionHistory.getFromAccount(),
                                transitionHistory.getToAccount(), transitionHistory.getFromUserName(),
                                transitionHistory.getToUserName(), transitionHistory.getBalance(),
                                transitionHistory.getMessage());
                        return ResponseEntity
                                .ok(UnauthorizedAccount.builder().status(200).message("Banking successful").build());
                    } else {
                        return ResponseEntity.status(401)
                                .body(UnauthorizedAccount.builder().status(401).message("Not enough money").build());
                    }
                } else {
                    return ResponseEntity.status(401)
                            .body(UnauthorizedAccount.builder().status(401).message("Something error").build());
                }
            } else {
                return ResponseEntity.status(401)
                        .body(UnauthorizedAccount.builder().status(403).message("Wrong OTP").build());
            }
        }
    }

    @GetMapping("/check-banking-transition")
    public ResponseEntity<?> getBankingTransition(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = true) String account,
            @RequestParam(required = false) String dateTime,
            @RequestParam(required = false) String message) {
        final String accountToken = jwtService.extractUserName(token.substring(7));

        if (!accountToken.equals(account)) {
            return ResponseEntity.status(403)
                    .body(UnauthorizedAccount.builder().status(403).message("You can only check your own transactions")
                            .build());
        }

        Optional<User> checkAccount = userService.findByAccount(account);
        if (!checkAccount.isPresent()) {
            return ResponseEntity.status(404)
                    .body(UnauthorizedAccount.builder().status(404).message("Account not found").build());
        } else {
            if (dateTime != null && !dateTime.isEmpty()) {
                if (message != null && !message.isEmpty()) {
                    // Gọi phương thức tìm theo account và dateTime và message
                    List<TransitionHistory> transitionHistories = transitionHistoryService
                            .findTransitionByAccountAndDateTimeAndMessage(account, dateTime, message);
                    return ResponseEntity.ok(transitionHistories);
                } else {
                    // Gọi phương thức tìm theo account và dateTime
                    List<TransitionHistory> transitionHistories = transitionHistoryService
                            .findTransitionByAccountAndDateTime(account, dateTime);
                    return ResponseEntity.ok(transitionHistories);
                }
            } else {
                // Gọi phương thức tìm chỉ theo account
                List<TransitionHistory> transitionHistories;
                if (message != null && !message.isEmpty()) {
                    transitionHistories = transitionHistoryService.findTransitionByAccountAndMessage(account, message);
                } else {
                    transitionHistories = transitionHistoryService.findTransitionByAccount(account);
                }
                return ResponseEntity.ok(transitionHistories);
            }
        }
    }

    @GetMapping("/check-banking-transition-date-range")
    public ResponseEntity<?> getBankingTransitionDateRange(
            @RequestHeader("Authorization") String token,
            @RequestParam String account,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        final String accountToken = jwtService.extractUserName(token.substring(7));
        final String role = jwtService.extractRole(token.substring(7));

        if (!accountToken.equals(account) && role.equals("USER")) {
            return ResponseEntity.status(403)
                    .body(UnauthorizedAccount.builder().status(403).message("You can only check your own transactions")
                            .build());
        }

        Optional<User> checkAccount = userService.findByAccount(account);
        if (checkAccount.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(UnauthorizedAccount.builder().status(404).message("Account not found").build());
        } else {
            List<TransitionHistory> transitionHistories = transitionHistoryService
                    .findTransitionByAccountAndDateRange(account, startDate, endDate);
            return ResponseEntity.ok(transitionHistories);
        }
    }

    @GetMapping("/monthly-transition-summary")
    public ResponseEntity<?> getMonthlyTransitionSummary(
            @RequestHeader("Authorization") String token,
            @RequestParam String account,
            @RequestParam int year) {
        final String accountToken = jwtService.extractUserName(token.substring(7));

        if (!accountToken.equals(account)) {
            return ResponseEntity.status(403)
                    .body(UnauthorizedAccount.builder().status(403).message("You can only check your own transactions")
                            .build());
        }
        Optional<User> checkAccount = userService.findByAccount(account);
        if (checkAccount.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(UnauthorizedAccount.builder().status(404).message("Account not found").build());
        } else {
            List<TransitionSumary> transitionSumaries = transitionSummaryService
                    .findMonthlyTransitionByAccount(account, year);
            return ResponseEntity.ok(transitionSumaries);
        }
    }

    String notificationFormart(long fund, String account, String message, long remainFund, String day, String hour,
            boolean isBanking) {
        return "TK " + account + " | " + (isBanking ? "GD: -" : "GD: +") + fund + "VND " + day + hour + " | SD:"
                + remainFund + "VND | ND: " + message;
    }
}
