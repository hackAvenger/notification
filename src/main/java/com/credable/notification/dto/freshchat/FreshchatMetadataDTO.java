package com.credable.notification.dto.freshchat;

import com.credable.notification.dto.freshchat.request.Param;

import lombok.Data;

@Data
public class FreshchatMetadataDTO {
  private String[] templateName;
  private String[] templateNamespace;
  private Param[] params;
}
