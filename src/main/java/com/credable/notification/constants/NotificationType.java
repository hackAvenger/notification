package com.credable.notification.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationType {

    SMS("SMS"),
    EMAIL("EMAIL"),
    WHATSAPP("WHATSAPP"),
    PUSH("PUSH");

    private String type;

    public static NotificationType getNotificationType(String type){

        if(type.equalsIgnoreCase("SMS"))
            return NotificationType.SMS;
        else if(type.equalsIgnoreCase("EMAIL"))
            return NotificationType.EMAIL;
        else if(type.equalsIgnoreCase("WHATSAPP"))
          return NotificationType.WHATSAPP;
        else if(type.equalsIgnoreCase("PUSH"))
          return NotificationType.PUSH;
        return null;
    }
}
