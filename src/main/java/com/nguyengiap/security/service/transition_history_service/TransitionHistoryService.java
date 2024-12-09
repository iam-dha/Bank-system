package com.nguyengiap.security.service.transition_history_service;

import com.nguyengiap.security.database_model.history_transistion.TransitionHistory;
import com.nguyengiap.security.database_model.history_transistion.TransitionHistoryRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransitionHistoryService {
    @Autowired
    final TransitionHistoryRepository transitionHistoryRepository;

    public void saveTransitionHistory(TransitionHistory request) {
        transitionHistoryRepository.save(request);
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
}
