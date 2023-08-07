package com.credable.notification.dto.freshchat.request;

import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
public class FreshChatOutBoundRequest {
    private Contact from;
    private Contact[] to;
    private Data data;
}