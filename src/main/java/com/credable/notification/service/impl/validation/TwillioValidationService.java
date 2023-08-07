package com.credable.notification.service.impl.validation;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.credable.notification.constants.ExceptionCode;
import com.credable.notification.exception.CredableException;
import com.credable.notification.request.TriggerNotificationRequest;
import com.credable.notification.service.ValidationService;

import lombok.extern.log4j.Log4j2;

@Service("EMAIL_SENDGRID_VALIDATION")
@Log4j2
public class TwillioValidationService implements ValidationService {
  @Override
  public void validateRequest(TriggerNotificationRequest notificationRequest) throws CredableException {
    if (!StringUtils.hasText(notificationRequest.getRecipient())) {
      throw new CredableException(ExceptionCode.EMAIL_RECIPIENTS_REQUIRED);
    }
    if (!StringUtils.hasText(notificationRequest.getBody())) {
      throw new CredableException(ExceptionCode.EMAIL_BODY_REQUIRED);
    }
    if (!StringUtils.hasText(notificationRequest.getSubject())) {
      throw new CredableException(ExceptionCode.EMAIL_SUBJECT_REQUIRED);
    }

  }
}
