package com.nguyengiap.security.service;

import com.nguyengiap.security.database_model.history_transistion.TransitionHistory;
import com.nguyengiap.security.database_model.history_transistion.TransitionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransitionHistoryService {
    final TransitionHistoryRepository transitionHistoryRepository;

    public void saveTransitionHistory(TransitionHistory request) {
        transitionHistoryRepository.save(request);
    }
}
