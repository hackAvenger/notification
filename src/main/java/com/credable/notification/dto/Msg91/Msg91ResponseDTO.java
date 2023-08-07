package com.credable.notification.dto.Msg91;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Msg91ResponseDTO {
  @JsonProperty("request_id")
  private String request_id;
  @JsonProperty("type")
  private String type;
  @JsonProperty("message")
  private String message;
}
