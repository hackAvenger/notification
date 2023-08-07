package com.credable.notification.dto.freshchat.response;

import lombok.Data;

@Data
public class FreshChatSuccessResponse implements FreshChatResponse {
    String request_id;
    String request_process_time;
    Link link;
}

@Data
class Link {
    String rel;
    String href;
    String type;
}