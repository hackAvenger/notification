package com.credable.notification.exception;

import com.credable.notification.constants.ExceptionMessageType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CredableException extends Exception {

    private static final long serialVersionUID = 1L;
    
    private final String messageCode;
    private final String messageDesc;
    private final ExceptionMessageType messageProducerType;
    
    public CredableException(String messageCode, String messageDesc) {
        super(messageDesc);
        this.messageCode = messageCode;
        this.messageDesc = messageDesc;
        this.messageProducerType = ExceptionMessageType.MESSAGE_BY_CODE;
    }
    
    public CredableException(String messageCode) {
        this.messageCode = messageCode;
        this.messageDesc = null;
        this.messageProducerType = ExceptionMessageType.MESSAGE_BY_CODE;
    }
      
    public CredableException(String messageCode, ExceptionMessageType type) {
      this.messageCode = messageCode;
      this.messageDesc = messageCode;
      this.messageProducerType = type;
    }
}
