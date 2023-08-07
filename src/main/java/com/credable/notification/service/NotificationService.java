package com.credable.notification.service;


import com.credable.notification.dto.NotificationStatusResponseDTO;
import com.credable.notification.exception.CredableException;
import com.credable.notification.model.Product;
import com.credable.notification.dto.NotificationResponseDTO;
import com.credable.notification.model.Notification;
import com.credable.notification.request.TriggerNotificationRequest;

public interface NotificationService {

   NotificationResponseDTO triggerNotification(Notification notification) throws Exception;

   Long createNotification(TriggerNotificationRequest notification,Product product) throws Exception;

   NotificationStatusResponseDTO fetchNotificationStatus(Long notifId, Product product) throws CredableException;
}
