package com.nguyengiap.security.service.transition_history_service;

import com.nguyengiap.security.database_model.history_transistion.TransitionHistory;
import com.nguyengiap.security.database_model.history_transistion.TransitionHistoryRepository;
import com.nguyengiap.security.database_model.history_transistion.TransitionSumary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransitionSummaryService {

    @Autowired
    final TransitionHistoryRepository transitionHistoryRepository;

    public List<TransitionSumary> findMonthlyTransitionByAccount(String account, int year) {
        List<Object[]> results = transitionHistoryRepository.findMonthlyTransitionByAccount(account, year);
        List<TransitionSumary> summaries = results.stream()
                .map(row -> new TransitionSumary(
                        (String) row[0],                  // account
                        ((Number) row[1]).intValue(),     // month
                        ((Number) row[2]).doubleValue(),  // expense
                        ((Number) row[3]).doubleValue()   // income
                ))
                .collect(Collectors.toList());
        return summaries;
    }
}
