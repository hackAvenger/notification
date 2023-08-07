package com.credable.notification.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.credable.notification.dao.NotificationLogDAO;
import com.credable.notification.model.NotificationLog;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.credable.notification.Utils.CryptoUtils;
import com.credable.notification.Utils.RestClient;
import com.credable.notification.Utils.S3Utility;
import com.credable.notification.components.ApplicationProperties;
import com.credable.notification.constants.NotificationStatus;
import com.credable.notification.constants.NotificationType;
import com.credable.notification.constants.UploadType;
import com.credable.notification.dto.FileDTO;
import com.credable.notification.dto.NotificationDTO;
import com.credable.notification.dto.VendorDTO;
import com.credable.notification.model.Notification;
import com.credable.notification.model.NotificationAttachment;
import com.credable.notification.model.Product;
import com.credable.notification.model.ProductVendor;
import com.credable.notification.model.Vendor;
import com.credable.notification.repository.NotificationAttachmentsDAO;
import com.credable.notification.repository.NotificationRepository;
import com.credable.notification.repository.ProductVendorDAO;
import com.credable.notification.request.TriggerNotificationRequest;
import com.credable.notification.service.NotificationHelperService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class NotificationHelperServiceImpl implements NotificationHelperService {

  @Autowired
  private NotificationRepository notificationRepository;

  @Autowired
  private ApplicationProperties applicationProperties;

  @Autowired
  private AmazonS3 amazonS3;

  @Autowired
  private NotificationAttachmentsDAO notificationAttachmentsDAO;
  
  @Autowired
  private ProductVendorDAO productVendorDAO;
  
  @Autowired
  private NotificationLogDAO notificationLogDAO;


  @Override
  public NotificationDTO convertToNotificationDTO(Notification notification) {
    Vendor vendor = notification.getVendor();
    NotificationDTO notificationDTO = new NotificationDTO();
    notificationDTO.setNotificationType(notification.getType());
    notificationDTO.setBody(notification.getBody());
    notificationDTO.setEmailCC(notification.getEmailCC());
    notificationDTO.setEmailBCC(notification.getEmailBcc());
    notificationDTO.setRecipient(notification.getRecipient());
    notificationDTO.setSubject(notification.getSubject());
    notificationDTO.setScheduledTime(notification.getScheduledTime());
    notificationDTO.setRecipient(notification.getRecipient());
    notificationDTO.setMetadata(notification.getMetadata());
    ProductVendor pv = productVendorDAO.findByProductAndVendor(notification.getProduct().getId(), vendor.getId());
    notificationDTO.setSender(pv.getSender());
    notificationDTO.setSenderFromName(pv.getFromName());
    if (!CollectionUtils.isEmpty(notification.getNotificationAttachments())) {
      List<FileDTO> fileDTOList = new ArrayList<>();
      for (NotificationAttachment notificationAttachment : notification.getNotificationAttachments()) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileName(notificationAttachment.getFileName());
        fileDTO.setFileUrl(notificationAttachment.getFileUrl());
        String bucket = null;
        
        if (notificationAttachment.getUploadType() == UploadType.FILE) 
          bucket = applicationProperties.getS3bucketPrivate();
         else 
          bucket = applicationProperties.getS3bucketShared();
        S3Object downloaded = S3Utility.getS3Object(amazonS3, bucket, notificationAttachment.getFileUrl());
        try {
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          StreamUtils.copy(downloaded.getObjectContent(), baos);
          fileDTO.setFileContents(CryptoUtils.encodeToBase64(baos.toByteArray()));
          fileDTOList.add(fileDTO);
        } catch (Exception e) {
          log.error("Error while downloading file {}",notificationAttachment.getFileName(), e);
        }
      }
      notificationDTO.setFileList(fileDTOList);
    }
    return notificationDTO;
  }

  @Override
  public VendorDTO convertToVendorDTO(Vendor vendor) {
    return new VendorDTO(vendor.getVendorType(), vendor.getVendorName()).setAccessKey(vendor.getAccessKey()).setSecretKey(vendor.getSecretKey()).setBaseUrl(vendor.getBaseUrl())
        .setUris(vendor.getUris()).setVendorId(vendor.getId());
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveNotificationLogs(List<NotificationLog> notificationLogList) {
    notificationLogDAO.saveAll(notificationLogList);
    
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void markAsInprogress(List<Notification> pendingNotifications) {
    pendingNotifications.forEach(pn -> pn.setStatus(NotificationStatus.INPROGRESS));
    notificationRepository.saveAll(pendingNotifications);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveInNewTransaction(List<Notification> pendingNotifications) {
    notificationRepository.saveAll(pendingNotifications);
  }

  @Override
  public void createAttachments(TriggerNotificationRequest triggerNotificationRequest, Notification notification)  {
    Product product = notification.getProduct();
    String uploadPath = product.getProductName() + "/" + notification.getId() + "/";

    List<FileDTO> fileDTOS = triggerNotificationRequest.getFileList();
    List<NotificationAttachment> notificationAttachments = new ArrayList<>();
    for (FileDTO fileDTO : fileDTOS) {
      NotificationAttachment notificationAttachment = new NotificationAttachment();
      notificationAttachment.setNotification(notification);
      notificationAttachment.setCreated(LocalDateTime.now());
      notificationAttachment.setUpdated(LocalDateTime.now());
      notificationAttachment.setFileName(fileDTO.getFileName());
      notificationAttachment.setFileUrl(fileDTO.getFileUrl());

      if (!StringUtils.hasText(fileDTO.getFileUrl()) && StringUtils.hasText(fileDTO.getFileContents())) {
        String uploadFileUrl = uploadPath + fileDTO.getFileName();
        try {
          byte[] fileContent = CryptoUtils.decodeFromBase64(fileDTO.getFileContents());
          S3Utility.uploadFileToS3(amazonS3,fileContent , "application/octet-stream", fileContent.length, uploadFileUrl,
              applicationProperties.getS3bucketPrivate());
          notificationAttachment.setFileUrl(uploadFileUrl);
          notificationAttachment.setUploadType(UploadType.FILE);
        } catch (IOException e) {
          log.error("Skipping because Error while uploading file to S3 {} with exception", uploadFileUrl, e);
          break;
        }
      } else if (StringUtils.hasText(fileDTO.getFileUrl())) {
        notificationAttachment.setUploadType(UploadType.S3_LINK);
      } else {
        log.error("Skipping because Neither content nor url present");
        break;
      }

      notificationAttachments.add(notificationAttachment);
    }

    notificationAttachmentsDAO.saveAll(notificationAttachments);
  }

}
