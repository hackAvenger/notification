package com.credable.notification.service;

import com.credable.notification.dto.NotificationDTO;
import com.credable.notification.dto.NotificationResponseDTO;
import com.credable.notification.dto.NotificationStatusResponseDTO;
import com.credable.notification.dto.VendorDTO;
import com.credable.notification.exception.CredableException;
import com.credable.notification.model.Notification;

public interface NotificationVendorService {

   NotificationResponseDTO sendNotification(NotificationDTO notificationRequest, VendorDTO vendorDTO) throws Exception;

   NotificationStatusResponseDTO fetchNotificationStatus(Notification notification, VendorDTO vendorDTO) throws CredableException;
}
