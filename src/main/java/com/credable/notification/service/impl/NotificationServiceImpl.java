package com.credable.notification.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.credable.notification.dto.NotificationStatusResponseDTO;
import com.credable.notification.factory.VendorValidationFactory;
import com.credable.notification.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.credable.notification.constants.ExceptionCode;
import com.credable.notification.constants.NotificationCategory;
import com.credable.notification.constants.NotificationStatus;
import com.credable.notification.dto.NotificationDTO;
import com.credable.notification.dto.NotificationResponseDTO;
import com.credable.notification.dto.VendorDTO;
import com.credable.notification.exception.CredableException;
import com.credable.notification.factory.NotificationVendorFactory;
import com.credable.notification.model.Notification;
import com.credable.notification.model.Product;
import com.credable.notification.model.Vendor;
import com.credable.notification.repository.NotificationRepository;
import com.credable.notification.repository.VendorRepository;
import com.credable.notification.request.TriggerNotificationRequest;
import com.credable.notification.service.NotificationHelperService;
import com.credable.notification.service.NotificationService;
import com.credable.notification.service.NotificationVendorService;

import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private VendorRepository vendorRepository;
    
    @Autowired
    private NotificationVendorFactory vendorFactory;
    
    @Autowired
    private NotificationHelperService notificationHelperService;

    @Autowired
    private VendorValidationFactory vendorValidationFactory;
    
    @Override
    public NotificationResponseDTO triggerNotification(Notification notification) throws Exception {
        NotificationDTO notificationDTO = notificationHelperService.convertToNotificationDTO(notification);
        VendorDTO vendorDTO = notificationHelperService.convertToVendorDTO(notification.getVendor());
        NotificationVendorService vendorService = vendorFactory.getNotificationType(vendorDTO);
        if(vendorService == null) {
          throw new CredableException(ExceptionCode.VENDOR_NOT_FOUND, ExceptionCode.VENDOR_NOT_FOUND);
        }
        log.info("Attempting to send notification scheduled at {} with vendor {}", notification.getScheduledTime(), vendorDTO.getVendorName());
        
        return vendorService.sendNotification(notificationDTO, vendorDTO);
    }


    @Override
    public Long createNotification(TriggerNotificationRequest notificationRequest,Product product) throws Exception {
        log.info("Trigger Notification Request Received for Product {} request {}",product.getProductName(),notificationRequest);
        Product newProduct = new Product();
        newProduct.setId(product.getId());
        List<Vendor> vendors = vendorRepository.findActiveVendorByProduct(notificationRequest.getNotificationType(),product.getId());
        if(CollectionUtils.isEmpty(vendors)){
          throw new CredableException(ExceptionCode.VENDOR_NOT_FOUND, ExceptionCode.VENDOR_NOT_FOUND);
        }
        if(vendors.size() > 1){
          throw new CredableException(ExceptionCode.MORE_THAN_ONE_ACTIVE_VENDOR_FOUND, ExceptionCode.MORE_THAN_ONE_ACTIVE_VENDOR_FOUND);
        }
        log.info("Vendor found for notification:  {}", vendors.get(0).getVendorName());
        ValidationService validationService = vendorValidationFactory.getValidationService(notificationRequest.getNotificationType(), vendors.get(0).getVendorName());
        if (validationService != null) {
          log.info("Validating notification request with {}", validationService.getClass().getSimpleName());
          validationService.validateRequest(notificationRequest);
        }

        Notification notification = new Notification();
        notification.setCreated( LocalDateTime.now());
        notification.setUpdated(LocalDateTime.now());
        notification.setProduct(newProduct);
        notification.setType(notificationRequest.getNotificationType());
        notification.setVendor(vendors.get(0));
        notification.setStatus(NotificationStatus.PENDING);
        notification.setBody(notificationRequest.getBody());
        notification.setEmailBcc(notificationRequest.getEmailBCC());
        notification.setEmailCC(notificationRequest.getEmailCC());
        notification.setRecipient(notificationRequest.getRecipient());
        if(notificationRequest.getScheduledTime() == null){
            notification.setScheduledTime(LocalDateTime.now());
        }else{
            notification.setScheduledTime(notificationRequest.getScheduledTime());
        }
        notification.setSubject(notificationRequest.getSubject());
        notification.setRetryCount(0);
        NotificationCategory category = NotificationCategory.getEnumByString(notificationRequest.getCategory());
        notification.setCategory(category);
        notification.setMetadata(notificationRequest.getMetadata());
        LOGGER.info("Saving notification details in table notification");
        notification = notificationRepository.save(notification);
        if(!CollectionUtils.isEmpty(notificationRequest.getFileList()))
            notificationHelperService.createAttachments(notificationRequest,notification);
        return notification.getId();
    }

    @Override
    public NotificationStatusResponseDTO fetchNotificationStatus(Long notifId, Product product) throws CredableException {
      Notification notification = notificationRepository.findById(notifId).orElseThrow(() -> new CredableException(ExceptionCode.NOTIFICATION_NOT_FOUND));
      VendorDTO vendorDTO = VendorDTO.from(notification);
      NotificationVendorService notificationService = vendorFactory.getNotificationType(vendorDTO);
      NotificationStatusResponseDTO notificationStatusResponseDTO = notificationService.fetchNotificationStatus(notification, vendorDTO);
      log.info("Notification status fetched for notification id {} is {}, Saving vendor status", notifId, notificationStatusResponseDTO);
      String status = notificationStatusResponseDTO.getStatus();
      notification.setVendorStatus(status);
      notification.setUpdated(LocalDateTime.now());
      notificationRepository.save(notification);
      return notificationStatusResponseDTO;
    }



}
