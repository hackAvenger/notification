package com.credable.notification.service.impl.vendors;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

import com.credable.notification.dto.NotificationStatusResponseDTO;
import com.credable.notification.exception.CredableException;
import com.credable.notification.model.Notification;
import com.credable.notification.constants.ApiMessage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.credable.notification.constants.NotificationConstant;
import com.credable.notification.dto.NotificationDTO;
import com.credable.notification.dto.NotificationResponseDTO;
import com.credable.notification.dto.VendorDTO;
import com.credable.notification.service.NotificationVendorService;
import com.sendgrid.Attachments;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import lombok.extern.log4j.Log4j2;

@Service("EMAIL_SENDGRID")
@Log4j2
public class TwilioNotification implements NotificationVendorService {

  @Override
  public NotificationResponseDTO sendNotification(NotificationDTO notificationRequest, VendorDTO vendorDTO) throws Exception {
    NotificationResponseDTO responseDTO = new NotificationResponseDTO();
    log.info("Sending sendgrid notification...");
    SendGrid sendGrid = new SendGrid(vendorDTO.getAccessKey());
    if (!validateData(notificationRequest)) {
      log.error("Invalid subject or body or recipients");
      responseDTO.setResponse("Invalid subject or body or recipients");
      responseDTO.setResult(Boolean.FALSE);
      return responseDTO;
    }
    try {
      String fromEmail = notificationRequest.getSender();
      String fromName = notificationRequest.getSenderFromName();
      if (!StringUtils.hasText(fromEmail)) {
        log.error("Invalid From Email");
        responseDTO.setResponse("Invalid From Email");
        responseDTO.setResult(Boolean.FALSE);
        return responseDTO;
      }
      log.info("From email {}", fromEmail);
      Email from = new Email(fromEmail);
      from.setName(fromName.isEmpty() ? "UpScale" : fromName);
      Content content = new Content("text/html", notificationRequest.getBody());
      Personalization personalization = getPersonalization(notificationRequest);
      Mail mail = getMail(notificationRequest.getSubject(), from, content);
      mail.addPersonalization(personalization);
      if (!CollectionUtils.isEmpty(notificationRequest.getFileList()))
        addAttachmentsToMail(notificationRequest, mail);
      Response response = callSendGridApi(sendGrid, mail);
      log.info("Response from send grid status {}  body {} and header {} of mail UNIQUE ID - {}", response.getStatusCode(), response.getBody(), response.getHeaders(),
          mail.getCustomArgs());
      responseDTO.setResponse("status=" + response.getStatusCode() + ",Body=" + response.getBody() + ",Headers=" + response.getHeaders() + ",CustomArgs=" + mail.getCustomArgs());
      String reference = mail.getCustomArgs().get(ApiMessage.CREADBLE_MESSAGE_ID);
      responseDTO.setReferenceId(reference);
      responseDTO.setRequest(notificationRequest.toString());
      if (response.getStatusCode() == 202) {
        responseDTO.setResult(Boolean.TRUE);
        return responseDTO;
      } else {
        responseDTO.setResult(Boolean.FALSE);
        return responseDTO;
      }
    } catch (Exception ex) {
      log.error("Exception while sending email via sendgrid", ex);
      responseDTO.setResponse(ExceptionUtils.getStackTrace(ex));
      responseDTO.setResult(Boolean.FALSE);
      return responseDTO;
    }
  }

  @Override
  public NotificationStatusResponseDTO fetchNotificationStatus(Notification notification, VendorDTO vendorDTO) throws CredableException {
    throw new CredableException("Not implemented");
  }


  private Response callSendGridApi(SendGrid sendGrid, Mail mail) throws IOException {
    Request request = new Request();
    request.setMethod(Method.POST);
    request.setEndpoint("mail/send");
    request.setBody(mail.build());
    return sendGrid.api(request);
  }

  private void addAttachmentsToMail(NotificationDTO notificationDTO, Mail mail) {
    for (int i = 0; i < notificationDTO.getFileList().size(); i++) {
      String fileName = notificationDTO.getFileList().get(i).getFileName();
      byte[] fileContent = notificationDTO.getFileList().get(i).getFileContents().getBytes();
      if (fileContent != null && fileContent.length > 0) {
        String encodedfile = new String(fileContent, StandardCharsets.UTF_8);
        Attachments attachments = new Attachments();
        attachments.setContent(encodedfile);
        attachments.setType("application/octet-stream");
        attachments.setFilename(fileName);
        attachments.setDisposition("inline");
        attachments.setContentId(fileName);
        mail.addAttachments(attachments);
      }
    }
  }

  private Mail getMail(String subject, Email from, Content content) {
    Mail mail = new Mail();
    mail.setSubject(subject);
    mail.setFrom(from);
    mail.addContent(content);
    String uniqueID = UUID.randomUUID().toString();
    mail.addCustomArg(ApiMessage.CREADBLE_MESSAGE_ID, uniqueID);
    return mail;
  }

  private Personalization getPersonalization(NotificationDTO notificationDTO) {
    List<String> receivers = Arrays.asList(notificationDTO.getRecipient().split(","));
    Personalization personalization = new Personalization();
    for (String str : receivers) {
      personalization.addTo(new Email(str));
    }
    if (StringUtils.hasText(notificationDTO.getEmailCC())) {
      List<String> ccReceivers = Arrays.asList(notificationDTO.getEmailCC().split(","));
      for (String str : ccReceivers) {
        personalization.addCc(new Email(str));
      }
    }
    return personalization;
  }


  private boolean validateData(NotificationDTO notificationDTO) {
    if (!StringUtils.hasText(notificationDTO.getSubject())) {
      return false;
    }
    if (!StringUtils.hasText(notificationDTO.getBody())) {
      return false;
    }
    return StringUtils.hasText(notificationDTO.getRecipient());
  }
}
