package com.credable.notification.dao;

import com.credable.notification.dto.NotificationDataDTO;
import com.credable.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationDAO extends JpaRepository<Notification, Long> {

    @Query("SELECT new com.credable.notification.dto.NotificationDataDTO(DATE(na.created), COUNT(case when na.type = 'SMS' then 1 else NULL END), COUNT(case when na.type = 'EMAIL' then 1 else NULL END), COUNT(case when na.type = 'WHATSAPP' then 1 else NULL END)) FROM Notification na where na.created BETWEEN :pastDate AND :currentDate GROUP BY DATE(na.created)")
    List<NotificationDataDTO> fetchNotificationData(@Param("currentDate") LocalDateTime currentDate, @Param("pastDate") LocalDateTime pastDate);


}
