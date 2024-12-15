package com.nguyengiap.security.service.transition_history_service;

import com.nguyengiap.security.database_model.history_transistion.TransitionHistory;
import com.nguyengiap.security.database_model.history_transistion.TransitionHistoryRepository;
import com.nguyengiap.security.database_model.history_transistion.TransitionSumary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransitionSummaryService {

    @Autowired
    final TransitionHistoryRepository transitionHistoryRepository;

    public List<TransitionSumary> findMonthlyTransitionByAccount(String account, int year) {
        List<Object[]> results = transitionHistoryRepository.findMonthlyTransitionByAccount(account, year);
        List<TransitionSumary> rawSummaries = results.stream()
                .map(row -> new TransitionSumary(
                        ((Number) row[0]).intValue(),     // month
                        ((Number) row[1]).longValue(),  // expense
                        ((Number) row[2]).longValue()   // income
                ))
                .collect(Collectors.toList());
        List<TransitionSumary> summaries = new ArrayList<>();
        for(int i = 1; i <= 12; i++) {
            TransitionSumary tempRecord = new TransitionSumary(0, 0, 0);
            for(TransitionSumary record : rawSummaries) {
                if(record.getMonth() == i){
                    tempRecord = new TransitionSumary(record.getMonth(), record.getExpense(), record.getIncome());
                }
            }
            if(tempRecord.getMonth() != i) tempRecord = new TransitionSumary(i, 0, 0);
            summaries.add(tempRecord);
        }
        return summaries;
    }
}
