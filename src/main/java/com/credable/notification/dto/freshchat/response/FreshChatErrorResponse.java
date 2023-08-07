package com.credable.notification.dto.freshchat.response;

import lombok.Data;

@Data
public class FreshChatErrorResponse implements FreshChatResponse {
    boolean success;
    int error_code;
    String error_message;
}
