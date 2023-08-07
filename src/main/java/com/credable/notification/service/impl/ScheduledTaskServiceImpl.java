package com.credable.notification.service.impl;

import java.time.LocalDateTime;

import com.credable.notification.components.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ScheduledTaskServiceImpl{

    @Autowired
    private MailScheduler schedulerService;
    
    @Value("${schedule.email.cron}")
    private String scheduleEmailCron;
    
    @Value("${notification.data.cron}")
    private String notificationDataCron;

    @Scheduled(cron = "${schedule.email.cron}")
    public void scheduleMail() throws Exception {
        log.info("++++++++++++++++++++++++ Cron for scheduled mail task started ++++++++++++++++++++++++++++++ : " + LocalDateTime.now());
        schedulerService.scheduleMail();
        log.info("++++++++++++++++++++++++++++++ Cron for scheduled mail task End  ++++++++++++++++++++++++++++++ " + LocalDateTime.now());
    }
    
    @Scheduled(cron = "${notification.data.cron}")
    public void notificationData() throws Exception{
        log.info("++++++++++++ Cron to fetch notifications data ************************* ");
        schedulerService.scheduleNotificationData();
    }
}
