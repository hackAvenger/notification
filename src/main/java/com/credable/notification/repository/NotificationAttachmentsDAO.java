package com.credable.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.credable.notification.model.NotificationAttachment;

@Repository
public interface NotificationAttachmentsDAO extends JpaRepository<NotificationAttachment, Integer>{

}
