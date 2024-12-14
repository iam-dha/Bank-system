package com.nguyengiap.security.service.transition_history_service;

import com.nguyengiap.security.database_model.history_transistion.TransitionHistory;
import com.nguyengiap.security.database_model.history_transistion.TransitionHistoryRepository;
import com.nguyengiap.security.database_model.history_transistion.TransitionSumary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransitionSummaryService {

    @Autowired
    final TransitionHistoryRepository transitionHistoryRepository;

    public List<TransitionSumary> findMonthlyTransitionByAccount(String account, String year) {
        return transitionHistoryRepository.findMonthlyTransitionByAccount(account, year);
    }
}
