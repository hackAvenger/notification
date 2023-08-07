package com.credable.notification.dto.freshchat.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Body {
    private List<Param> params;
}



