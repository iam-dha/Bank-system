package com.nguyengiap.security.application_api;

import com.nguyengiap.security.auth.model.request_model.BankingRequest;
import com.nguyengiap.security.auth.model.request_model.BankingRequestOTP;
import com.nguyengiap.security.auth.model.response_model.BalanceWithAccount;
import com.nguyengiap.security.database_model.history_transistion.TransitionHistory;
import com.nguyengiap.security.service.OtpService;
import com.nguyengiap.security.service.TransitionHistoryService;
import com.nguyengiap.security.service.UserService;
import com.nguyengiap.security.service.notification_service.NotificationService;
import com.nguyengiap.security.database_model.user.User;
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
    private TransitionHistoryService transitionHistoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    @PostMapping("/banking")
    public ResponseEntity<?> banking(@RequestBody BankingRequest request) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = now.format(formatter);

        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedHour = now.format(formatterTime);

        // Kiểm tra xem tài khoản c tồn tại không
        Optional<User> checkFromAccount = userService.findByAccount(request.getFromAccount());

        checkFromAccount.ifPresent(user -> otpService.generateOtp(user.getEmail()));

        return ResponseEntity.ok("Success sent otp");
    }

    @PostMapping("/banking-otp")
    public ResponseEntity<?> bankingOTP(@RequestBody BankingRequestOTP request) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = now.format(formatter);

        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedHour = now.format(formatterTime);

        // Kiểm tra xem tài khoản c tồn tại không
        Optional<User> checkFromAccount = userService.findByAccount(request.getFromAccount());

        Optional<User> checkToAccount = userService.findByAccount(request.getToAccount());

        // Nếu không tồn tại
        if (checkFromAccount.isEmpty() || checkToAccount.isEmpty()) {
            return ResponseEntity.status(404).body("Account not found");
        } else { // Nếu tồn tại
            if (otpService.validOtp(checkFromAccount.get().getEmail(), request.getOtp())) {
                // Tra cứu số dư tài khoản gửi
                Optional<BalanceWithAccount> checkFromAccountBalance = userService
                        .findBalanceByAccount(request.getFromAccount());
                if (checkFromAccountBalance.isPresent()) {
                    // Nếu số tiền còn lại >= số tiền cần gửi
                    if (checkFromAccountBalance.get().getFund() >= request.getFund()) {
                        // Tạo truy vấn chuyển tiền(cộng tiền)
                        userService.bankingToAccount(request.getToAccount(), request.getFund());
                        // Trừ tiền
                        userService.bankingToAccount2(request.getFromAccount(), request.getFund());

                        // Tên người chuyển
                        String fromUserName = checkFromAccount.get().getFirstName() + " "
                                + checkFromAccount.get().getLastName();
                        String toUserName = checkToAccount.get().getFirstName() + " "
                                + checkToAccount.get().getLastName();

                        Optional<BalanceWithAccount> checkFromAccountRemainBalance = userService
                                .findBalanceByAccount(request.getFromAccount());
                        Optional<BalanceWithAccount> checkToAccountRemainBalance = userService
                                .findBalanceByAccount(request.getToAccount());

                        String fromUserNotification = notificationFormart(request.getFund(), request.getFromAccount(),
                                request.getMessage(), checkFromAccountRemainBalance.get().getFund(), formattedDate,
                                formattedHour, true);
                        String toUserNotification = notificationFormart(request.getFund(), request.getToAccount(),
                                request.getMessage(), checkToAccountRemainBalance.get().getFund(), formattedDate,
                                formattedHour, false);

                        notificationService.sendNotificationToUser(request.getToAccount(), "Thông báo biến động số dư",
                                toUserNotification);
                        notificationService.sendNotificationToUser(request.getFromAccount(),
                                "Thông báo biến động số dư", fromUserNotification);

                        var transitionHistory = TransitionHistory.builder()
                                .fromAccount(request.getFromAccount())
                                .toAccount(request.getToAccount())
                                .message(request.getMessage())
                                .balance(request.getFund())
                                .dateTime(formattedDate)
                                .fromUserName(fromUserName)
                                .toUserName(toUserName)
                                .time(formattedHour)
                                .build();
                        // Lưu giao dịch
                        transitionHistoryService.saveTransitionHistory(transitionHistory);
                        return ResponseEntity.ok("Banking successful");
                    } else {
                        return ResponseEntity.status(401).body("Not enough money");
                    }
                } else {
                    return ResponseEntity.status(403).body("Wrong OTP");
                }
            } else {
                return ResponseEntity.status(401).body("Something error");
            }
        }
    }

    @GetMapping("/check-banking-transition")
    public ResponseEntity<?> getBankingTransition(
            @RequestParam String account,
            @RequestParam(required = false) String dateTime,
            @RequestParam(required = false) String message) {
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

    String notificationFormart(int fund, String account, String message, int remainFund, String day, String hour,
            boolean isBanking) {
        return "TK " + account + "|" + (isBanking ? "GD: -" : "GD: +") + fund + "VND " + day + hour + " |SD:"
                + remainFund + "VND|ND: " + message;
    }
}
