package com.credable.notification.dto.freshchat.response;

import java.util.List;
import java.util.Optional;

import org.springframework.util.CollectionUtils;

import lombok.Data;

@Data
public class FreshChatStatusResponse {
    List<Response> outbound_messages;

    public Optional<String> getStatus() {
        String status = null;
        if (!CollectionUtils.isEmpty(this.outbound_messages)) {
            status = this.outbound_messages.get(0).getStatus();
        }
        return Optional.ofNullable(status);
    }
}

@Data
class Response {
    String request_id;
    String status;
    String created_on;
}
