package com.credable.notification.config.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.credable.notification.components.ApplicationProperties;

@Configuration
public class SQSConfig {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
        return  new QueueMessagingTemplate(amazonSQSAsync());
    }

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder
                .standard()
                .withRegion(applicationProperties.getRegion())
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(applicationProperties.getAwsAccessKey(), applicationProperties.getAwsSecretKey())))
                .build();
    }


}
