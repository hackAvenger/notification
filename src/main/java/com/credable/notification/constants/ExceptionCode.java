package com.credable.notification.constants;

public class ExceptionCode {

  private ExceptionCode() {}

  // ############ VENDOR EXCEPTIONS #################
  public static final String VENDOR_NOT_FOUND = "VENDOREXP01";
  public static final String MORE_THAN_ONE_ACTIVE_VENDOR_FOUND = "VENDOREXP02";


  // ############ FRESHCHAT EXCEPTIONS #################
  public static final String FRESHCHAT_INVALID_TEMPLATE = "FRSHCHTEXP01";
  public static final String TEMPLATE_NAME_REQUIRED = "TEMPLATENAMEREQUIRED";
  public static final String TEMPLATE_NAMESPACE_REQUIRED = "TEMPLATENAMESPACEREQUIRED";
  public static final String PARAMETERS_REQUIRED = "PARAMETERSREQUIRED";

  // ############ SMS EXCEPTIONS #################
  public static final String MESSAGE_TEMPLATE_ID_REQUIRED = "MSGTEMPLATEIDREQUIRED";
  public static final String MESSAGE_CATEGORY_REQUIRED = "MSGCATEGOYRIDREQUIRED";

  // ############ EMAIL EXCEPTIONS #################
  public static final String EMAIL_SUBJECT_REQUIRED = "EMAILSUBJECTREQUIRED";
  public static final String EMAIL_BODY_REQUIRED = "EMAILBODYREQUIRED";
  public static final String EMAIL_RECIPIENTS_REQUIRED = "EMAILRECIPIENTSREQUIRED";


  // ############ NOTIFICATION EXCEPTIONS #################
  public static final String NOTIFICATION_NOT_FOUND = "NOTIFICATIONNOTFOUND";
  public static final String FAILED_TO_FETCH_STATUS = "FAILEDTOFETCHSTATUS";


}
