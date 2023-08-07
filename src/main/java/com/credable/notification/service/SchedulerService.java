package com.credable.notification.service;

import com.credable.notification.dto.NotificationDataDTO;

import java.io.IOException;
import java.util.List;

public interface SchedulerService {

     void scheduleMail() throws Exception;
     
     void scheduleNotificationData() throws Exception;
} 
