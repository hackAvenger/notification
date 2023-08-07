package com.credable.notification.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.credable.notification.components.ApplicationProperties;

public class S3Utility {
  
  private S3Utility() {}

  public static S3Object getS3Object(AmazonS3 s3Client, String bucketName, String s3Url) {
    GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, s3Url);
    return s3Client.getObject(getObjectRequest);
  }

  public static void uploadFileToS3(AmazonS3 s3Client, MultipartFile file, String filePath, String bucket) throws IOException {
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());
    PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, filePath, file.getInputStream(), metadata);
    s3Client.putObject(putObjectRequest);
  }

  public static void uploadFileToS3(AmazonS3 s3Client, byte[] content, String contentType, long size, String filePath, String bucket) throws IOException {
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(size);
    metadata.setContentType(contentType);
    PutObjectRequest putObjectRequest =
        new PutObjectRequest(bucket, filePath, new ByteArrayInputStream(content), metadata);
    s3Client.putObject(putObjectRequest);
  }
}
