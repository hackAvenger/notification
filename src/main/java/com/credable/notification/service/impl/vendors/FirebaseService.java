package com.credable.notification.service.impl.vendors;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import com.credable.notification.constants.NotificationStatus;
import com.credable.notification.dto.NotificationDTO;
import com.credable.notification.dto.NotificationResponseDTO;
import com.credable.notification.dto.NotificationStatusResponseDTO;
import com.credable.notification.dto.VendorDTO;
import com.credable.notification.exception.CredableException;
import com.credable.notification.service.NotificationVendorService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Service("PUSH_FIREBASE")
@Log4j2
public class FirebaseService implements NotificationVendorService {

  private FirebaseApp createFirebaseAppInstance(String accessKey,Long vendorId) throws IOException {
    try {
      return FirebaseApp.getInstance(String.valueOf(vendorId));
    }catch (IllegalStateException ex){
      log.info("Firebase App instance not exists for vendor id {}, creating new instance",vendorId);
      FirebaseOptions firebaseOptions = FirebaseOptions.builder()
              .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(accessKey.getBytes())))
              .build();
      return FirebaseApp.initializeApp(firebaseOptions, String.valueOf(vendorId));
    }
  }
  @Override
  public NotificationResponseDTO sendNotification( NotificationDTO notificationDTO, VendorDTO vendorDTO) throws Exception {

      FirebaseApp firebaseApp = createFirebaseAppInstance(vendorDTO.getAccessKey(), vendorDTO.getVendorId());
      FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance(firebaseApp);
      Notification notification = Notification
              .builder()
              .setTitle(notificationDTO.getSubject())
              .setBody(notificationDTO.getBody())
              .build();

      AndroidNotification androidNotification = AndroidNotification.builder()
              .setBody(notificationDTO.getBody())
              .setTitle(notificationDTO.getSubject())
              .build();

      Aps aps = Aps.builder()

              .build();

      ApnsConfig apnsConfig = ApnsConfig.builder()
              .setAps(aps)

              .build();

      AndroidConfig androidConfig = AndroidConfig.builder()
              .setNotification(androidNotification)
              .build();

      Message message = Message
              .builder()
              .setToken(notificationDTO.getRecipient())
              .setNotification(notification)
              .setAndroidConfig(androidConfig)
              .setApnsConfig(apnsConfig)
              .build();

      String response = firebaseMessaging.send(message);

      NotificationResponseDTO responseDTO = new NotificationResponseDTO();
      responseDTO.setResponse(response);
      responseDTO.setResult(true);
      responseDTO.setStatus(NotificationStatus.SENT);
      return responseDTO;

  }

  @Override
  public NotificationStatusResponseDTO fetchNotificationStatus(com.credable.notification.model.Notification notification, VendorDTO vendorDTO) throws CredableException {
    // TODO Auto-generated method stub
    return null;
  }
  
}
