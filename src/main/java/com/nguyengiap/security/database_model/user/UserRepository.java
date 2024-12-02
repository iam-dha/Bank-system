package com.nguyengiap.security.database_model.user;

import com.nguyengiap.security.auth.model.request_model.response_model.BalanceWithAccount;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAccountByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.account = :account")
    Optional<User> findByAccount(@Param("account") String account);

    @Query("SELECT new com.nguyengiap.security.auth.model.request_model.response_model.BalanceWithAccount(u.account, u.fund) " +
            "FROM User u WHERE u.account = :account")
    Optional<BalanceWithAccount> findBalanceByAccount(@Param("account") String account);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.fund = u.fund + :fund WHERE u.account = :account")
    void bankingToAccount(@Param("account") String account, @Param("fund") Integer fund);

    @Modifying
    @Transactional
    @Query("UPDATE User u Set u.fund = u.fund - :fund WHERE u.account = :account")
    void bankingToAccount2(@Param("account") String account, @Param("fund") Integer fund);
}