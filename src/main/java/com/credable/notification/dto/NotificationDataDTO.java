package com.credable.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDataDTO {
    private LocalDate date;
    private Long smsCount;
    private Long emailCount;
    private Long whatsappCount;

    public NotificationDataDTO(Date date, Long smsCount, Long emailCount, Long whatsappCount) {
        this.date = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        this.smsCount = smsCount;
        this.emailCount = emailCount;
        this.whatsappCount = whatsappCount;
    }
}
