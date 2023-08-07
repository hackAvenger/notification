package com.credable.notification.service;

import java.util.List;

import com.credable.notification.dto.NotificationDTO;
import com.credable.notification.dto.VendorDTO;
import com.credable.notification.model.Notification;
import com.credable.notification.model.NotificationLog;
import com.credable.notification.model.Vendor;
import com.credable.notification.request.TriggerNotificationRequest;

public interface NotificationHelperService {
  public NotificationDTO convertToNotificationDTO(Notification notification) throws Exception;

  public VendorDTO convertToVendorDTO(Vendor vendor);

  public void markAsInprogress(List<Notification> pendingNotifications);

  public void saveInNewTransaction(List<Notification> pendingNotifications);

  public void createAttachments(TriggerNotificationRequest notificationRequest, Notification notification);
  
  public void saveNotificationLogs(List<NotificationLog> notificationLogList);
  
}
