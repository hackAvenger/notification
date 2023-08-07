package com.credable.notification.dto;

import com.credable.notification.constants.NotificationStatus;

import lombok.Data;

@Data
public class NotificationResponseDTO {
  private Boolean result;
  private String request;
  private String response;
  private String referenceId;
  private NotificationStatus status;
}
