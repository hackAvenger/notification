package com.credable.notification.dto.freshchat.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageTemplate {
    private final String storage="none";
    private String template_name;
    private String namespace;
    private Language language;
    private RichTemplateData rich_template_data;
}
