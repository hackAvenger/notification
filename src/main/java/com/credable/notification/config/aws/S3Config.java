package com.credable.notification.config.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.credable.notification.components.ApplicationProperties;

import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
public class S3Config {
  @Autowired
  private ApplicationProperties applicationProperties;
  
  @Bean
  public AmazonS3 createS3Client() {
    String awsId = applicationProperties.getAwsAccessKey();
    String awsKey = applicationProperties.getAwsSecretKey();
    String region = applicationProperties.getRegion();

    log.info("################ creating S3 Application Bucket configuration ###################");
    BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
    AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region)).withCredentials(new AWSStaticCredentialsProvider(awsCreds))
            .withClientConfiguration(new ClientConfiguration().withGzip(Boolean.TRUE).withMaxConnections(100).withConnectionTimeout(60 * 1000).withMaxErrorRetry(15)).build();
    log.info("################ S3 configuration created ###################");
    return s3Client;
  }
}
