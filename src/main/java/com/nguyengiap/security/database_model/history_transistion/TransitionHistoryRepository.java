package com.nguyengiap.security.database_model.history_transistion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransitionHistoryRepository extends JpaRepository<TransitionHistory, Integer> {
    @Query("SELECT u FROM TransitionHistory u WHERE u.fromAccount = :account OR u.toAccount = :account")
    List<TransitionHistory> findTransitionByAccount(@Param("account") String account);
}
