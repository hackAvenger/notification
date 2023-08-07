package com.credable.notification.service.impl.validation;

import org.springframework.stereotype.Service;

import com.credable.notification.constants.ExceptionCode;
import com.credable.notification.constants.NotificationConstant;
import com.credable.notification.exception.CredableException;
import com.credable.notification.request.TriggerNotificationRequest;
import com.credable.notification.service.ValidationService;

import lombok.extern.log4j.Log4j2;

@Service("SMS_MSG91_VALIDATION")
@Log4j2
public class Msg91ValidationService implements ValidationService {
  @Override
  public void validateRequest(TriggerNotificationRequest notificationRequest) throws CredableException {
    if (!notificationRequest.getMetadata().containsKey(NotificationConstant.MSG91_TEMPLATE_ID_KEY)) {
      throw new CredableException(ExceptionCode.MESSAGE_TEMPLATE_ID_REQUIRED);
    }
    if (notificationRequest.getCategory() == null) {
      throw new CredableException(ExceptionCode.MESSAGE_CATEGORY_REQUIRED);
    }
  }
}
