package com.credable.notification.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

import com.credable.notification.components.ApplicationProperties;
import com.credable.notification.service.SQSService;

@Service
public class SQSServiceImpl implements SQSService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SQSServiceImpl.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Override
    public void pushNotificationToSQS(String notificationMsg) {
        Map<String, Object> headers = new HashMap<>();
        //headers.put( SqsMessageHeaders.SQS_GROUP_ID_HEADER, "my-application");
        //headers.put(SqsMessageHeaders.SQS_DEDUPLICATION_ID_HEADER, UUID.randomUUID().toString());
        queueMessagingTemplate.convertAndSend(applicationProperties.getSqsURL(),
                notificationMsg,headers);
    }

    @SqsListener("notification")
    @Override
    public void pullNotificationFromSQS(String message){

        LOGGER.info("Message from SQS {}",message);
    }
}
