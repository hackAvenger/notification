package com.credable.notification.service;

public interface SQSService {
     void pushNotificationToSQS(String notificationMsg);
     void pullNotificationFromSQS(String message);
}
