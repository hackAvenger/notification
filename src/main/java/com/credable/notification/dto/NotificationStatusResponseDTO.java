package com.credable.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class NotificationStatusResponseDTO {
  private String status;
  private Long requestId;
}
