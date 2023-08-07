package com.credable.notification.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VendorName {
    GMAIL("GMAIL"),
    SENDGRID("SENDGRID"),
    FRESHCHAT("FRESHCHAT"),
    MSG91("MSG91"),
    FIREBASE("FIREBASE");
    private final String name;
}
