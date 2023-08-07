package com.credable.notification.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.credable.notification.model.Notification;
import com.credable.notification.model.NotificationAttachment;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    @Query("select n from Notification n left join fetch n.notificationAttachments join fetch n.vendor join fetch n.product where n.status IN ('PENDING', 'FAILED') and n.retryCount < :maxRetry and n.scheduledTime <= :now")
    List<Notification> findAllPendingNotifications(@Param("maxRetry") int maxRetry, @Param("now") LocalDateTime now);
}
