package com.credable.notification.constants;

public interface NotificationConstant {
  
  public static final String EMAIL_CONTENT_TYPE_KEY = "Content-type";
  public static final String EMAIL_CONTENT_TYPE_VALUE = "text/HTML; charset=UTF-8";
  public static final String EMAIL_FORMAT_KEY =  "format";
  public static final String EMAIL_FORMAT_VALUE =  "flowed";
  public static final String EMAIL_CONTENT_TRANSFER_ENCODING_KEY = "Content-Transfer-Encoding";
  public static final String EMAIL_CONTENT_TRANSFER_ENCODING_VALUE =  "8bit";
  public static final String EMAIL_CHARSET = "UTF-8";
  public static final String UTF_8 = "UTF-8";
  
  public static final String MAIL_SMTP_HOST_KEY = "mail.smtp.host";
  public static final String MAIL_SMTP_HOST_VALUE = "smtp.gmail.com";
  public static final String MAIL_SMTP_SOCKET_FACTORY_PORT_KEY = "mail.smtp.socketFactory.port";
  public static final String MAIL_SMTP_SOCKET_FACTORY_PORT_VALUE = "465";
  public static final String MAIL_SMTP_SOCKET_FACTORY_CLASS_KEY = "mail.smtp.socketFactory.class";
  public static final String MAIL_SMTP_SOCKET_FACTORY_CLASS_VALUE = "javax.net.ssl.SSLSocketFactory";
  public static final String MAIL_SMTP_AUTH_KEY = "mail.smtp.auth";
  public static final String MAIL_SMTP_AUTH_VALUE =  "true";
  public static final Object MAIL_SMTP_PORT_KEY = "mail.smtp.port";
  public static final Object MAIL_SMTP_PORT_VALUE = "465";
  public static final Object MAIL_SMTP_START_TTLS_ENABLE_KEY = "mail.smtp.starttls.enable";
  public static final Object MAIL_SMTP_START_TTLS_ENABLE_VALUE = "true";
  public static final String MAIL_SMTP_SSL_PROTOCOLS_KEY = "mail.smtp.ssl.protocols";
  public static final String MAIL_SMTP_SSL_PROTOCOLS_VALUE = "TLSv1.2";
  
  public static final String APPLICATION = "application";
  public static final String JSON = "json";
  public static final String AUTHORIZATION_HEADER_KEY = "Authorization";
  public static final String BEARER_TOKEN_START = "Bearer ";
  
  public static final String MSG91_AUTH_KEY = "authkey";
  
  public static final String FRESHCHAT_OUTBOUND_URI = "freshchatOutboundUri";
  public static final String FRESHCHAT_STATUS_URI = "freshchatStatusUri";
  
  public static final Object MSG91_TRANSACTION_URI = "msg91TransactionUri";
  public static final Object MSG91_OTP_URI = "msg91OtpUri";
  public static final Object MSG91_TEMPLATE_ID_KEY = "flow_id";
  public static final Object FROM_NAME = "fromName";
  public static final Object MSG91_PARAMS_KEY = "params";

  public static final String EMAIL_USERNAME = "${email.username}";
  public static final String EMAIL_PASSWORD = "${email.password}";
  public static final String EMAIL_RECEIVERS = "${email.receivers}";
  
  public static final String NOTIFICATION_DATA_EMAIL_SUBJECT = "Notification Data For %s to %s";

}
