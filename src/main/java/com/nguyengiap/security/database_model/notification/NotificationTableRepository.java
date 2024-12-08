package com.nguyengiap.security.database_model.notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface NotificationTableRepository extends JpaRepository<NotiticationTable, Integer> {
    @Query("SELECT u FROM NotiticationTable u WHERE u.account = :account")
    List<NotiticationTable> getMessageWithAccount(@Param("account") String account);

    @Transactional
    @Query("DELETE FROM NotiticationTable u WHERE u.account = :account")
    void deleteMessageWithAccount(@Param("account") String account);
    
}
