package com.credable.notification.dto;

import com.credable.notification.constants.NotificationCategory;
import com.credable.notification.constants.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class NotificationDTO {

    private NotificationType notificationType;

    private NotificationCategory category;
    
    private String recipient;
    
    private String sender;
    
    private String senderFromName;

    private String emailCC;

    private String emailBCC;

    private String subject;

    private String body;

    private LocalDateTime scheduledTime;

    private List<FileDTO> fileList;
    
    private Map<String,String> metadata;
    
}
