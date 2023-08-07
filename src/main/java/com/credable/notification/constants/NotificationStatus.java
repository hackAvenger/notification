package com.credable.notification.constants;

public enum NotificationStatus {

    DELIVERED("DELIVERED"),
    SENT("SENT"),
    PENDING("PENDING"),
    FAILED("FAILED"),
    INPROGRESS("INPROGRESS");

    String status;

    private NotificationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static NotificationStatus getNotificationStatus(String status){
        if(status.equalsIgnoreCase("DELIVERED"))
            return NotificationStatus.DELIVERED;
        else if(status.equalsIgnoreCase("PENDING"))
            return NotificationStatus.PENDING;
        else if(status.equalsIgnoreCase("FAILED"))
            return NotificationStatus.FAILED;
        else if(status.equalsIgnoreCase("SENT"))
          return NotificationStatus.SENT;
        else if(status.equalsIgnoreCase("INPROGRESS"))
          return NotificationStatus.INPROGRESS;
      
        return null;
    }

}
