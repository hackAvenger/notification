package com.credable.notification.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.credable.notification.constants.NotificationType;
import com.credable.notification.dto.FileDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TriggerNotificationRequest {

    @NotNull(message = "Notification Type Cannot be Empty")
    private NotificationType notificationType;

    @NotBlank(message = "Recipient Cannot be Empty")
    private String recipient;

    private String emailCC;

    private String emailBCC;

    private String subject;

    private String body;

    private LocalDateTime scheduledTime;

    private List<FileDTO> fileList;
    
    private String category;
    
    private Map<String,String> metadata;

}
