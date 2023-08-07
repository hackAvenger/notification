package com.credable.notification.components;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ApplicationProperties {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.credentials.access-key}")
    private String awsAccessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String awsSecretKey;

    @Value("${cloud.aws.sqsurl}")
    private String sqsURL;
    
    @Value("${cloud.aws.s3.bucket.private}")
    private String s3bucketPrivate;
    
    @Value("${cloud.aws.s3.bucket.shared}")
    private String s3bucketShared;
    
    @Value("${max.retry.count}")
    private int maxRetryCount;
    
    @Value("${is.otp.hardcoded}")
    private Boolean isOtpHardcoded;
    
    @Value("${hardcoded.otp.value}")
    private Integer hardcodedOtp;
    
    @Value("${email.receivers}")
    private String emailReceivers;
    
    @Value("${product.key.internal}")
    private String productKeyInternal;
    

    
    
}
