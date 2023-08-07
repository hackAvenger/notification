package com.credable.notification.constants;

public enum NotificationCategory {
  OTP, PROMOTION, NOT_APPLICABLE;
  
  public static NotificationCategory getEnumByString(String val) {
    for(NotificationCategory cat: NotificationCategory.values()) {
      if(cat.name().equalsIgnoreCase(val)) {
        return cat;
      }
    }
    return NOT_APPLICABLE;
  }
}
