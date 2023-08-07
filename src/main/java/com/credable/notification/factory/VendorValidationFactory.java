package com.credable.notification.factory;

import java.util.Map;

import com.credable.notification.constants.NotificationType;
import com.credable.notification.constants.VendorName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.credable.notification.service.ValidationService;

@Component
public class VendorValidationFactory {

  @Autowired
  private Map<String, ValidationService> validationServiceMap;

  public ValidationService getValidationService(NotificationType notificationType, VendorName vendorName) {
    return validationServiceMap.get(notificationType + "_" + vendorName + "_VALIDATION" );
  }

}
