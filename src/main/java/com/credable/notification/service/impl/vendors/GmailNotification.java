package com.credable.notification.service.impl.vendors;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.credable.notification.dto.NotificationStatusResponseDTO;
import com.credable.notification.exception.CredableException;
import com.credable.notification.model.Notification;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.credable.notification.Utils.CryptoUtils;
import com.credable.notification.constants.NotificationConstant;
import com.credable.notification.dto.NotificationDTO;
import com.credable.notification.dto.NotificationResponseDTO;
import com.credable.notification.dto.VendorDTO;
import com.credable.notification.service.NotificationVendorService;

import lombok.extern.log4j.Log4j2;

@Service("EMAIL_GMAIL")
@Log4j2
public class GmailNotification  implements NotificationVendorService {

  @Override
  public NotificationResponseDTO sendNotification(NotificationDTO notificationRequest, VendorDTO vendorDTO) throws Exception {
    NotificationResponseDTO responseDTO = new NotificationResponseDTO();
    Map<String, String> metadata = notificationRequest.getMetadata();
    String image = null;
    if(metadata != null)
      image = metadata.getOrDefault("image", null);

    log.info("Sending Gmail notificaiton... ");
    String username = vendorDTO.getAccessKey();
    String password = vendorDTO.getSecretKey();
    Session session = getSession(username, password);
    if (Objects.isNull(session)) {
      responseDTO.setResult(Boolean.FALSE);
      responseDTO.setResponse("Failed to generate session");
      return responseDTO;
    }
    try {
      MimeMessage msg = new MimeMessage(session);
      msg.addHeader(NotificationConstant.EMAIL_CONTENT_TYPE_KEY, NotificationConstant.EMAIL_CONTENT_TYPE_VALUE);
      msg.addHeader(NotificationConstant.EMAIL_FORMAT_KEY, NotificationConstant.EMAIL_FORMAT_VALUE);
      msg.addHeader(NotificationConstant.EMAIL_CONTENT_TRANSFER_ENCODING_KEY, NotificationConstant.EMAIL_CONTENT_TRANSFER_ENCODING_VALUE);
      msg.setFrom(new InternetAddress(username));
      msg.setReplyTo(InternetAddress.parse(notificationRequest.getRecipient(), false));
      msg.setSubject(notificationRequest.getSubject(), NotificationConstant.EMAIL_CHARSET);
      msg.setSentDate(new Date());
      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(notificationRequest.getRecipient(), false));
      if(StringUtils.hasText(notificationRequest.getEmailCC())) {
        msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(notificationRequest.getEmailCC(), false));
      }
      
      if (CollectionUtils.isEmpty(notificationRequest.getFileList())) {
        msg.setText(notificationRequest.getBody(), NotificationConstant.EMAIL_CHARSET, "html");
      } else {
        Multipart multiPart = new MimeMultipart();
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(notificationRequest.getBody(), StandardCharsets.UTF_8.name(), "html");
        multiPart.addBodyPart(messageBodyPart);
        for (int i = 0; i < notificationRequest.getFileList().size(); i++) {
          byte[] bs = CryptoUtils.decodeFromBase64(notificationRequest.getFileList().get(i).getFileContents());
          if (bs != null && bs.length > 0) {
            DataSource fds = new ByteArrayDataSource(bs, "application/octet-stream");
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setDataHandler(new DataHandler(fds));
            attachment.setDisposition(Part.ATTACHMENT);
            attachment.setFileName(notificationRequest.getFileList().get(i).getFileName());
            multiPart.addBodyPart(attachment);
          }
        }
        msg.setContent(multiPart);
      }
      if(StringUtils.hasText(image)) {
          Multipart multiPart = new MimeMultipart();
          BodyPart bodyPart1 = new MimeBodyPart();
          bodyPart1.setContent(notificationRequest.getBody(), "text/html");
          MimeBodyPart messageBodyPart2 = new MimeBodyPart();
          messageBodyPart2.setFileName("chart_image.png");
          messageBodyPart2.setContentID("<chart_image>");
          messageBodyPart2.setDataHandler(new DataHandler(new ByteArrayDataSource(CryptoUtils.decodeFromBase64(image), "image/png")));
          multiPart.addBodyPart(messageBodyPart2);
          multiPart.addBodyPart(bodyPart1);
          msg.setContent(multiPart);
          log.info("image set");
        }
      Transport.send(msg);
      log.info("Email Sent Successfully");
      responseDTO.setResult(Boolean.TRUE);
      return responseDTO;
    } catch (Exception e) {
      log.error("Exception while Sending Email", e);
      responseDTO.setResult(Boolean.FALSE);
      responseDTO.setResponse(ExceptionUtils.getStackTrace(e));
      return responseDTO;
    }
  }

  @Override
  public NotificationStatusResponseDTO fetchNotificationStatus(Notification notification, VendorDTO vendorDTO) throws CredableException {
    throw new CredableException("Not implemented");
  }



  private Session getSession(String username, String password) {
    if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
      Properties props = new Properties();
      props.put(NotificationConstant.MAIL_SMTP_HOST_KEY, NotificationConstant.MAIL_SMTP_HOST_VALUE);
      props.put(NotificationConstant.MAIL_SMTP_SOCKET_FACTORY_PORT_KEY, NotificationConstant.MAIL_SMTP_SOCKET_FACTORY_PORT_VALUE);
      props.put(NotificationConstant.MAIL_SMTP_SOCKET_FACTORY_CLASS_KEY, NotificationConstant.MAIL_SMTP_SOCKET_FACTORY_CLASS_VALUE);
      props.put(NotificationConstant.MAIL_SMTP_AUTH_KEY, NotificationConstant.MAIL_SMTP_AUTH_VALUE);
      props.put(NotificationConstant.MAIL_SMTP_PORT_KEY, NotificationConstant.MAIL_SMTP_PORT_VALUE);
      props.put(NotificationConstant.MAIL_SMTP_START_TTLS_ENABLE_KEY, NotificationConstant.MAIL_SMTP_START_TTLS_ENABLE_VALUE);
      props.put(NotificationConstant.MAIL_SMTP_SSL_PROTOCOLS_KEY, NotificationConstant.MAIL_SMTP_SSL_PROTOCOLS_VALUE);
      return Session.getDefaultInstance(props, new javax.mail.Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(username, password);
        }
      });
    } else
      return null;
  }
}
