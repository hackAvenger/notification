package com.credable.notification.dao;

import com.credable.notification.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationLogDAO extends JpaRepository<NotificationLog, Long> {
}
