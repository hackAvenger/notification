package com.credable.notification.exception;

import lombok.Getter;

@Getter
public class UnAuthorizedSessionException extends RuntimeException {

  private static final long serialVersionUID = -8459516723131176747L;

  private final String messageCode;
  private final String messageDesc;

  public UnAuthorizedSessionException(String messageCode, String messageDesc) {
      super(messageDesc);
      this.messageCode = messageCode;
      this.messageDesc = messageDesc;
  }
}
