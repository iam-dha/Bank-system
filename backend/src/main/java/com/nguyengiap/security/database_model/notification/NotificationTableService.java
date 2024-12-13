package com.nguyengiap.security.database_model.notification;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationTableService {
    final NotificationTableRepository notificationTableRepository;

    public List<NotiticationTable> getMessageWithAccount(String account) {
        return notificationTableRepository.getMessageWithAccount(account);
    }

    public void deleteMessageWithAccount(String account) {
        notificationTableRepository.deleteMessageWithAccount(account);
    }

    public void saveMessage(NotiticationTable notiticationTable) {
        notificationTableRepository.save(notiticationTable);
    }
}