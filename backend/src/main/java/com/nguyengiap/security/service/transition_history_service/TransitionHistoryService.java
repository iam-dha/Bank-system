package com.nguyengiap.security.service.transition_history_service;

import com.nguyengiap.security.database_model.history_transistion.TransitionHistory;
import com.nguyengiap.security.database_model.history_transistion.TransitionHistoryRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransitionHistoryService {
    @Autowired
    final TransitionHistoryRepository transitionHistoryRepository;

    public void saveTransitionHistory(String fromAccount, String toAccount, String fromUserName, String toUserName, long fund, String message) {
        transitionHistoryRepository.save(TransitionHistory.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .balance(fund)
                .dateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))
                .fromUserName(fromUserName)
                .toUserName(toUserName)
                .message(message)
                .build());
    }

    public List<TransitionHistory> findTransitionByAccount(String account) {
        return transitionHistoryRepository.findTransitionByAccount(account);
    }

    public List<TransitionHistory> findTransitionByAccountAndDateTime(String account, String dateTime) {
        return transitionHistoryRepository.findTransitionByAccountAndDateTime(account, dateTime);
    }

    public List<TransitionHistory> findTransitionByAccountAndMessage(String account, String message) {
        return transitionHistoryRepository.findTransitionByAccountAndMessage(account, message);
    }

    public List<TransitionHistory> findTransitionByAccountAndDateTimeAndMessage(String account, String dateTime, String message) {
        return transitionHistoryRepository.findTransitionByAccountAndDateTimeAndMessage(account, dateTime, message);
    }

    public List<TransitionHistory> findTransitionByAccountAndDateRange(String account, String startDate, String endDate) {
        return transitionHistoryRepository.findTransitionByAccountAndDateRange(account, startDate, endDate);
    }
}
