package com.credable.notification.dto.freshchat.request;

import lombok.Data;

@Data
public class Header {
    private String type = "text";
    private Param[] params;
}