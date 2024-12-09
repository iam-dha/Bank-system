package com.nguyengiap.security.database_model.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nguyengiap.security.model.response_model.BalanceWithAccount;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.account = :account")
    Optional<User> findByAccount(String account);

    @Query("SELECT new com.nguyengiap.security.auth.model.response_model.BalanceWithAccount(u.account, u.fund) " +
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

    @Modifying
    @Transactional
    @Query("UPDATE User u Set u.password = :password WHERE u.account = :account")
    void changePassword(@Param("account") String account, @Param("password") String password);
}
