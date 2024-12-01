package com.nguyengiap.security.application_api;

import com.nguyengiap.security.auth.model.request_model.BankingRequest;
import com.nguyengiap.security.auth.model.request_model.response_model.BalanceWithAccount;
import com.nguyengiap.security.database_model.history_transistion.TransitionHistory;
import com.nguyengiap.security.database_model.history_transistion.TransitionHistoryRepository;
import com.nguyengiap.security.service.TransitionHistoryService;
import com.nguyengiap.security.service.UserService;
import com.nguyengiap.security.database_model.user.User;
import com.nguyengiap.security.database_model.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BankService {
    final UserRepository userRepository;

    final TransitionHistoryRepository transitionHistoryRepository;
    final TransitionHistoryService transitionHistoryService;

    @Autowired
    private UserService userService;

    @GetMapping("/api/v1/check-profile-balance")
    public ResponseEntity<?> checkProfileBalance(@RequestParam String account) {
        Optional<BalanceWithAccount> balance = userRepository.findBalanceByAccount(account);
        return balance.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/api/v1/list_account_by_email")
    public ResponseEntity<?> getListAccountWithEmail(
            @RequestParam String email
    ) {
        List<User> users = userService.findAccountByEmail(email);
        if (users.isEmpty()) {
            return ResponseEntity.status(404).body("No account found");
        }
        return ResponseEntity.ok(users);
    }

    @Transactional
    @PostMapping("/api/v1/banking")
    public ResponseEntity<?> banking(@RequestBody BankingRequest request) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = now.format(formatter);

        //Kiểm tra xem tài khoản c tồn tại không
        Optional<User> checkFromAccount = userRepository.findByAccount(request.getFromAccount());
        Optional<User> checkToAccount = userRepository.findByAccount(request.getToAccount());

        //Nếu không tồn tại
        if (checkFromAccount.isEmpty() || checkToAccount.isEmpty()) {
            return ResponseEntity.status(404).body("Account not found");
        } else { //Nếu tồn tại
            //Tra cứu số dư tài khoản gửi
            Optional<BalanceWithAccount> checkFromAccountBalance = userRepository.findBalanceByAccount(request.getFromAccount());
            if(checkFromAccountBalance.isPresent()) {
                //Nếu số tiền còn lại >= số tiền cần gửi
                if (checkFromAccountBalance.get().getFund() >= request.getFund()) {
                    //Tạo truy vấn chuyển tiền(cộng tiền)
                    userRepository.bankingToAccount(request.getToAccount(), request.getFund());
                    //Trừ tiền
                    userRepository.bankingToAccount2(request.getFromAccount(), request.getFund());

                    var transitionHistory = TransitionHistory.builder()
                                    .fromAccount(request.getFromAccount())
                                            .toAccount(request.getToAccount())
                                                    .message(request.getMessage())
                                                            .balance(request.getFund())
                                                                    .dateTime(formattedDate)
                                                                            .build();
                    //Lưu giao dịch
                    transitionHistoryService.saveTransitionHistory(transitionHistory);
                    return ResponseEntity.ok("Banking successful");
                } else {
                    return ResponseEntity.status(401).body("Not enough money");
                }
            } else {
                return ResponseEntity.status(401).body("Something error");
            }
        }
    }

    @GetMapping("/api/v1/check-banking-transition")
    public ResponseEntity<?> getBankingTransistionByAccount(@RequestParam String account) {
        System.out.println("Account parameter: " + account);
        List<TransitionHistory> transitionHistories = transitionHistoryRepository.findTransitionByAccount(account);
        if(transitionHistories.isEmpty()) {
            return ResponseEntity.ok("Transition history is empty");
        } else {
            return ResponseEntity.ok(transitionHistories);
        }
    }


}

