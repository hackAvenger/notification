package com.credable.notification.service.impl.vendors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.credable.notification.dto.NotificationStatusResponseDTO;
import com.credable.notification.dto.freshchat.response.FreshChatStatusResponse;
import com.credable.notification.model.Notification;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.credable.notification.Utils.ConverterUtils;
import com.credable.notification.constants.ExceptionCode;
import com.credable.notification.constants.NotificationConstant;
import com.credable.notification.constants.NotificationStatus;
import com.credable.notification.dto.NotificationDTO;
import com.credable.notification.dto.NotificationResponseDTO;
import com.credable.notification.dto.VendorDTO;
import com.credable.notification.dto.freshchat.request.Body;
import com.credable.notification.dto.freshchat.request.Contact;
import com.credable.notification.dto.freshchat.request.Data;
import com.credable.notification.dto.freshchat.request.FreshChatOutBoundRequest;
import com.credable.notification.dto.freshchat.request.Language;
import com.credable.notification.dto.freshchat.request.MessageTemplate;
import com.credable.notification.dto.freshchat.request.Param;
import com.credable.notification.dto.freshchat.request.RichTemplateData;
import com.credable.notification.dto.freshchat.response.FreshChatErrorResponse;
import com.credable.notification.dto.freshchat.response.FreshChatResponse;
import com.credable.notification.dto.freshchat.response.FreshChatSuccessResponse;
import com.credable.notification.exception.CredableException;
import com.credable.notification.service.NotificationVendorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Service("WHATSAPP_FRESHCHAT")
@Log4j2
public class FreshChatService implements NotificationVendorService {

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public NotificationResponseDTO sendNotification(NotificationDTO notificationDTO, VendorDTO vendorDTO) throws Exception {
    NotificationResponseDTO responseDTO = new NotificationResponseDTO();
    log.info("Sending whatsapp msg: [{}]", notificationDTO);
    FreshChatOutBoundRequest outBoundMsgRequest;
    try {
      outBoundMsgRequest = getOutBoundMsgRequest(notificationDTO);
    } catch (CredableException e1) {
      responseDTO.setResponse(ExceptionUtils.getStackTrace(e1));
      responseDTO.setResult(Boolean.FALSE);
      responseDTO.setStatus(NotificationStatus.FAILED);
      return responseDTO;
    }
    String requestStr = null;
    try {
      requestStr = mapper.writeValueAsString(outBoundMsgRequest);
      responseDTO.setRequest(requestStr);
    } catch (JsonProcessingException e) {
      log.error(e);
      responseDTO.setResponse(ExceptionUtils.getStackTrace(e));
      responseDTO.setResult(Boolean.FALSE);
      responseDTO.setStatus(NotificationStatus.FAILED);
      return responseDTO;
    }
    
    try {
      Optional<FreshChatResponse> freshChatResponse = callSendOutBoundMsgAPI(outBoundMsgRequest, vendorDTO);
      if (freshChatResponse.isPresent()) {
        String responseStr = mapper.writeValueAsString(freshChatResponse.get());
        responseDTO.setResponse(responseStr);
        if (freshChatResponse.get() instanceof FreshChatSuccessResponse) {
          FreshChatSuccessResponse successResponse = (FreshChatSuccessResponse) freshChatResponse.get();
          responseDTO.setReferenceId(successResponse.getRequest_id());
          responseDTO.setStatus(NotificationStatus.SENT);
        } else {
          responseDTO.setStatus(NotificationStatus.FAILED);
        }
      } else {
        responseDTO.setStatus(NotificationStatus.FAILED);
      }
    } catch (Exception ex) {
      log.error("Error while sending whatsapp msg {}", ex);
      responseDTO.setResponse(ExceptionUtils.getStackTrace(ex));
      responseDTO.setResult(Boolean.FALSE);
      responseDTO.setStatus(NotificationStatus.FAILED);
      return responseDTO;
    }
    if (responseDTO.getStatus() == NotificationStatus.SENT) {
      responseDTO.setResult(Boolean.TRUE);
      responseDTO.setStatus(NotificationStatus.SENT);
    }
    else {
      responseDTO.setResult(Boolean.FALSE);
      responseDTO.setStatus(NotificationStatus.FAILED);
    }
    return responseDTO;
  }

  private Optional<FreshChatResponse> callSendOutBoundMsgAPI(FreshChatOutBoundRequest outBoundMsgRequest, VendorDTO vendorDTO) throws JsonProcessingException {
    String freshChatUrl = vendorDTO.getBaseUrl();
    String freshChatUri = vendorDTO.getUris().get(NotificationConstant.FRESHCHAT_OUTBOUND_URI);
    HttpHeaders header = createHeader(vendorDTO.getAccessKey());
    HttpEntity<Object> requestEntity = new HttpEntity<>(outBoundMsgRequest, header);

    ResponseEntity<String> response = callApi(freshChatUrl + freshChatUri, HttpMethod.POST, requestEntity);

    FreshChatResponse responseObj;
    if (HttpStatus.OK == response.getStatusCode() || HttpStatus.ACCEPTED == response.getStatusCode()) {
      responseObj = mapper.readValue(response.getBody(), FreshChatSuccessResponse.class);
    } else if (HttpStatus.BAD_REQUEST == response.getStatusCode()) {
      responseObj = mapper.readValue(response.getBody(), FreshChatErrorResponse.class);
    } else {
      responseObj = null;
    }
    return Optional.ofNullable(responseObj);
  }

  private ResponseEntity<String> callApi(String url, HttpMethod method, HttpEntity<Object> requestEntity) {
    log.info("Request sending to URL: [{}], requestEntity: [{}]", url, requestEntity);
    ResponseEntity<String> response = restTemplate.exchange(url, method, requestEntity, String.class);
    log.info("Response received: [{}]", response);
    return response;
  }

  private FreshChatOutBoundRequest getOutBoundMsgRequest(NotificationDTO notificationDTO) throws CredableException {
    String templateName = notificationDTO.getMetadata().get("templateName");
    String templateNamespace = notificationDTO.getMetadata().get("templateNamespace");
    List<Param> param = new ArrayList<>();
    try {
      param = ConverterUtils.convertFromStringToList(notificationDTO.getMetadata().get("params"))
          .stream().map(Param::new).collect(Collectors.toList());
    } catch (Exception e) {
      log.error(e);
    }
    
    validateMetadata(templateName, templateNamespace);
    
    RichTemplateData richTemplateData = new RichTemplateData(new Body(param));
    MessageTemplate messageTemplate = MessageTemplate.builder().language(new Language()).template_name(templateName)
        .namespace(templateNamespace).rich_template_data(richTemplateData).build();
    FreshChatOutBoundRequest request = new FreshChatOutBoundRequest();
    request.setFrom(new Contact("+91"+notificationDTO.getSender()));
    Contact[] to = {new Contact("+91"+notificationDTO.getRecipient())};
    request.setTo(to);
    request.setData(new Data(messageTemplate));
    log.info("Fresh chat payload: [{}]", request);
    return request;
  }

  private void validateMetadata(String templateName, String templateNamespace) throws CredableException {
    if(!StringUtils.hasText(templateName) || !StringUtils.hasText(templateNamespace))
      throw new CredableException(ExceptionCode.FRESHCHAT_INVALID_TEMPLATE);
  }

  private HttpHeaders createHeader(String freshChatToken) {
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(new MediaType(NotificationConstant.APPLICATION, NotificationConstant.JSON));
    requestHeaders.set(NotificationConstant.AUTHORIZATION_HEADER_KEY, NotificationConstant.BEARER_TOKEN_START + freshChatToken);
    return requestHeaders;
  }

  @Override
  public NotificationStatusResponseDTO fetchNotificationStatus(Notification notification, VendorDTO vendorDTO) throws CredableException {
    String freshChatUrl = vendorDTO.getBaseUrl();
    String freshChatUri = vendorDTO.getUris().get(NotificationConstant.FRESHCHAT_STATUS_URI);
    HttpHeaders header = createHeader(vendorDTO.getAccessKey());
    HttpEntity<Object> requestEntity = new HttpEntity<>(header);

    ResponseEntity<String> response = callApi(freshChatUrl + freshChatUri + notification.getReferenceId(), HttpMethod.GET, requestEntity);

    FreshChatStatusResponse responseObj = null;
    try {
      if (HttpStatus.OK == response.getStatusCode()) {
        responseObj = mapper.readValue(response.getBody(), FreshChatStatusResponse.class);
      }
    } catch (JsonProcessingException e) {
     log.error("Error while fetching status for notification: [{}]", notification.getId());
    }

    if(responseObj == null || !responseObj.getStatus().isPresent()) {
      throw new CredableException(ExceptionCode.FAILED_TO_FETCH_STATUS);
    }

    return NotificationStatusResponseDTO.builder()
        .status(responseObj.getStatus().get())
        .requestId(notification.getId())
        .build();
  }
}
