package com.credable.notification.service.impl.validation;

import org.springframework.stereotype.Service;

import com.credable.notification.constants.ExceptionCode;
import com.credable.notification.exception.CredableException;
import com.credable.notification.request.TriggerNotificationRequest;
import com.credable.notification.service.ValidationService;

import lombok.extern.log4j.Log4j2;

@Service("WHATSAPP_FRESHCHAT_VALIDATION")
@Log4j2
public class FreshChatValidationService implements ValidationService {
  @Override
  public void validateRequest(TriggerNotificationRequest notificationRequest) throws CredableException {
    if (!notificationRequest.getMetadata().containsKey("templateName")) {
      throw new CredableException(ExceptionCode.TEMPLATE_NAME_REQUIRED);
    }
    if (!notificationRequest.getMetadata().containsKey("templateNamespace")) {
      throw new CredableException(ExceptionCode.TEMPLATE_NAMESPACE_REQUIRED);
    }
    if (!notificationRequest.getMetadata().containsKey("params")) {
      throw new CredableException(ExceptionCode.PARAMETERS_REQUIRED);
    }
  }
}
