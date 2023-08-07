package com.credable.notification.controller;


import javax.servlet.http.HttpServletRequest;

import com.credable.notification.constants.ApiMessage;
import com.credable.notification.constants.ApiUriConstant;
import com.credable.notification.dto.NotificationStatusResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.credable.notification.dto.TriggerNotificationResponseDTO;
import com.credable.notification.model.Product;
import com.credable.notification.request.TriggerNotificationRequest;
import com.credable.notification.response.ResponseWO;
import com.credable.notification.service.NotificationService;

@CrossOrigin
@RestController
public class NotificationController{


  @Autowired
  private NotificationService notificationService;

  @Autowired
  private Environment env;

    @PostMapping(value = ApiUriConstant.TRIGGER_NOTIFICATION)
    public ResponseEntity<ResponseWO<TriggerNotificationResponseDTO>> triggerNotification(@RequestBody TriggerNotificationRequest notificationRequest, HttpServletRequest httpServletRequest) throws Exception{
        Product product = (Product) httpServletRequest.getAttribute("product");
        Long id = notificationService.createNotification(notificationRequest,product);
        
        return ResponseEntity.ok(ResponseWO.okBuilder(new TriggerNotificationResponseDTO(id)).setMessageCode(ApiMessage.TRIGGER_NOTIF).setDescription(env.getProperty(ApiMessage.TRIGGER_NOTIF)).build());
    }

    @GetMapping(value = ApiUriConstant.GET_NOTIFICATION_STATUS)
    public ResponseEntity<ResponseWO<NotificationStatusResponseDTO>> getStatus(@RequestParam("notifId") Long notifId, HttpServletRequest httpServletRequest) throws Exception {
      Product product = (Product) httpServletRequest.getAttribute("product");
      NotificationStatusResponseDTO notificationStatusResponseDTO = notificationService.fetchNotificationStatus(notifId, product);

      return ResponseEntity
          .ok(ResponseWO.okBuilder(notificationStatusResponseDTO).setMessageCode(ApiMessage.NOTIF_STATUS).setDescription(env.getProperty(ApiMessage.NOTIF_STATUS)).build());
    }
}
