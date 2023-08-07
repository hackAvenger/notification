package com.credable.notification.service;

import com.credable.notification.exception.CredableException;
import com.credable.notification.request.TriggerNotificationRequest;

public interface ValidationService {
  void validateRequest(TriggerNotificationRequest notificationRequest) throws CredableException;
}
