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


    @Query(value = "SELECT tab1.account AS account, " +
            "tab1.month AS month, " +
            "COALESCE(tab1.expense, 0) AS expense, " +
            "COALESCE(tab2.income, 0) AS income " +
            "FROM " +
            "(SELECT u.from_account AS account, " +
            "        SUM(u.balance) AS expense, " +
            "        EXTRACT(MONTH FROM TO_DATE(u.date_time, 'DD/MM/YYYY')) AS month " +
            " FROM _transition_history u " +
            " WHERE u.from_account = :account " +
            "   AND EXTRACT(YEAR FROM TO_DATE(u.date_time, 'DD/MM/YYYY')) = :year " +
            " GROUP BY u.from_account, EXTRACT(MONTH FROM TO_DATE(u.date_time, 'DD/MM/YYYY'))) tab1 " +
            "FULL OUTER JOIN " +
            "(SELECT EXTRACT(MONTH FROM TO_DATE(i.date_time, 'DD/MM/YYYY')) AS month, " +
            "        SUM(i.balance) AS income " +
            " FROM _transition_history i " +
            " WHERE i.to_account = :account " +
            "   AND EXTRACT(YEAR FROM TO_DATE(i.date_time, 'DD/MM/YYYY')) = :year " +
            " GROUP BY EXTRACT(MONTH FROM TO_DATE(i.date_time, 'DD/MM/YYYY'))) tab2 " +
            "ON tab1.month = tab2.month",
            nativeQuery = true)
    List<Object[]> findMonthlyTransitionByAccount(
            @Param("account") String account,
            @Param("year") int year);

}
