package com.nguyengiap.security.database_model.history_transistion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface TransitionHistoryRepository extends JpaRepository<TransitionHistory, Integer> {
    @Query("SELECT u FROM TransitionHistory u WHERE u.fromAccount = :account OR u.toAccount = :account")
    List<TransitionHistory> findTransitionByAccount(@Param("account") String account);

    @Query("SELECT u FROM TransitionHistory u WHERE u.dateTime = :dateTime AND u.fromAccount = :account")
    List<TransitionHistory> findTransitionByAccountAndDateTime(@Param("account") String account,
            @Param("dateTime") String dateTime);

    @Query("SELECT u FROM TransitionHistory u WHERE u.message LIKE %:message% AND u.fromAccount = :account")
    List<TransitionHistory> findTransitionByAccountAndMessage(@Param("account") String account,
            @Param("message") String message);

    @Query("SELECT u FROM TransitionHistory u WHERE u.message LIKE %:message% AND u.fromAccount = :account AND u.dateTime = :dateTime")
    List<TransitionHistory> findTransitionByAccountAndDateTimeAndMessage(@Param("account") String account,
            @Param("dateTime") String dateTime, @Param("message") String message);

    @Query("SELECT u FROM TransitionHistory u WHERE TO_DATE(u.dateTime, 'DD/MM/YYYY') BETWEEN TO_DATE(:startDate, 'DD/MM/YYYY') AND TO_DATE(:endDate, 'DD/MM/YYYY') AND (u.fromAccount = :account OR u.toAccount = :account)")
    List<TransitionHistory> findTransitionByAccountAndDateRange(
            @Param("account") String account,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    @Query("SELECT expense, income, tab1.month " +
            "FROM " +
            "(SELECT SUM(u.balance) AS expense, EXTRACT(month FROM TO_DATE(u.dateTime, 'DD/MM/YYYY')) AS month " +
            "FROM TransitionHistory u " +
            "WHERE u.fromAccount = :account AND :year = EXTRACT(month FROM TO_DATE(u.dateTime, 'DD/MM/YYYY')) " +
            "GROUP BY EXTRACT(month FROM TO_DATE(u.dateTime, 'DD/MM/YYYY'))) AS tab1 " +
            "FULL OUTER JOIN " +
            "(SELECT SUM(i.balance) AS income, EXTRACT(month FROM TO_DATE(i.dateTime, 'DD/MM/YYYY')) AS month2 " +
            "FROM TransitionHistory i " +
            "WHERE i.toAccount = :account AND :year = EXTRACT(month FROM TO_DATE(i.dateTime, 'DD/MM/YYYY')) " +
            "GROUP BY EXTRACT(month FROM TO_DATE(i.dateTime, 'DD/MM/YYYY'))) AS tab2 " +
            "ON tab1.month = tab2.month2")
    List<TransitionSumary> findMonthlyTransitionByAccount(
            @Param("account") String account,
            @Param("year") String year);
}
