package com.credable.notification.service.impl.vendors;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.credable.notification.dto.NotificationStatusResponseDTO;
import com.credable.notification.exception.CredableException;
import com.credable.notification.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.credable.notification.Utils.GenericUtil;
import com.credable.notification.Utils.RestClient;
import com.credable.notification.components.ApplicationProperties;
import com.credable.notification.constants.NotificationCategory;
import com.credable.notification.constants.NotificationConstant;
import com.credable.notification.dto.NotificationDTO;
import com.credable.notification.dto.NotificationResponseDTO;
import com.credable.notification.dto.VendorDTO;
import com.credable.notification.dto.Msg91.Msg91ResponseDTO;
import com.credable.notification.service.NotificationVendorService;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.extern.log4j.Log4j2;

@Service("SMS_MSG91")
@Log4j2
public class Message91Notification implements NotificationVendorService {

  @Autowired
  private ApplicationProperties applicationProperties;

  @Autowired
  private RestClient restClient;

  @Override
  public NotificationResponseDTO sendNotification(NotificationDTO notificationDTO, VendorDTO vendorDTO) throws Exception {
    String templateId = notificationDTO.getMetadata().get(NotificationConstant.MSG91_TEMPLATE_ID_KEY);

    if (!validateSmsData(notificationDTO, templateId)) {
      log.error("Sms DTO validation failed");
      return null;
    }
    String msg91AuthKey = vendorDTO.getAccessKey();
    String msg91SmsUrl = vendorDTO.getBaseUrl();

    if (!StringUtils.hasText(msg91AuthKey) || !StringUtils.hasText(msg91SmsUrl)) {
      log.error("Sms Service url & credential not found");
      return null;
    }
    NotificationResponseDTO responseDTO = null;
    if (notificationDTO.getCategory() == NotificationCategory.OTP)
      responseDTO = sendOtp(notificationDTO, msg91SmsUrl + vendorDTO.getUris().get(NotificationConstant.MSG91_OTP_URI), msg91AuthKey, templateId);
    else
      responseDTO = sendTransactionalSms(notificationDTO, msg91SmsUrl + vendorDTO.getUris().get(NotificationConstant.MSG91_TRANSACTION_URI), msg91AuthKey);

    return responseDTO;
  }

  @Override
  public NotificationStatusResponseDTO fetchNotificationStatus(Notification notification, VendorDTO vendorDTO) throws CredableException {
    throw new CredableException("Not implemented");
  }

  private NotificationResponseDTO sendOtp(NotificationDTO notificationDTO, String url, String authKey, String templateId) throws Exception {
    String requestUrl = url + "?template_id=" + templateId + "&mobile=91" + notificationDTO.getRecipient() + "&authkey=" + authKey + "&otp=" + getOtp();
    Optional<Msg91ResponseDTO> response = restClient.get(requestUrl, Msg91ResponseDTO.class);
    log.info("Sms Response {}", response);

    return handleMsg91Response(requestUrl, new HashMap<>(), response);
  }

  private NotificationResponseDTO sendTransactionalSms(NotificationDTO notificationDTO, String url, String authKey) {
    Map<String, String> metadata = notificationDTO.getMetadata();
    String msg91SenderId = notificationDTO.getSender();
    HttpHeaders requestHeaders = setMsg91RequestHeader(authKey);
    Map<String, String> request = new LinkedHashMap<>();
    request.put("mobiles", "91" + notificationDTO.getRecipient());
    request.put("sender", msg91SenderId);
    request.putAll(metadata);
    HttpEntity<Object> requestEntity = new HttpEntity<>(request, requestHeaders);
    TypeReference<Msg91ResponseDTO> typeReference = new TypeReference<Msg91ResponseDTO>() {};
    Optional<Msg91ResponseDTO> response = restClient.post(url, requestEntity, typeReference);
    log.info("Sms Response {}", response);

    return handleMsg91Response(url, request, response);
  }

  private NotificationResponseDTO handleMsg91Response(String url, Map<String, String> request, Optional<Msg91ResponseDTO> response) {
    NotificationResponseDTO responseDTO = new NotificationResponseDTO();
    responseDTO.setRequest("url="+url+",body="+request);
    if (response.isPresent()) {
      
      responseDTO.setReferenceId(getReferenceIdFromMsg91Response(response.get()));
      responseDTO.setResponse(response.get().toString());
      if("error".equalsIgnoreCase(response.get().getType()))
          responseDTO.setResult(Boolean.FALSE);
      else
        responseDTO.setResult(Boolean.TRUE);
    } else {
      responseDTO.setResult(Boolean.FALSE);
    }
    return responseDTO;
  }

  private String getReferenceIdFromMsg91Response(Msg91ResponseDTO res) {
    return StringUtils.hasText(res.getMessage()) ? res.getMessage() : res.getRequest_id();
  }

  private boolean validateSmsData(NotificationDTO smsDTO, String templateId) {
    if (StringUtils.hasText(smsDTO.getRecipient()) && smsDTO.getRecipient().length() != 10)
      return false;
    return StringUtils.hasText(smsDTO.getRecipient()) && StringUtils.hasText(templateId);
  }


  private HttpHeaders setMsg91RequestHeader(String authKey) {
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(new MediaType(NotificationConstant.APPLICATION, NotificationConstant.JSON));
    requestHeaders.set(NotificationConstant.MSG91_AUTH_KEY, authKey);
    return requestHeaders;
  }

  private Integer getOtp() {
    Integer otp = null;
    Boolean isOtpHarcode = applicationProperties.getIsOtpHardcoded();
    if (isOtpHarcode != null && isOtpHarcode.equals(Boolean.TRUE)) {
      otp = applicationProperties.getHardcodedOtp();
    } else {
      otp = GenericUtil.getRandomOTP();
    }
    return otp;
  }

}
