package com.credable.notification.constants;

public enum ParamError {

    USER_PERSIST_ERROR("USER_PERSIST_ERROR","Unable to save user"),
    DISABLED_USER("DISABLED_USER","Disabled user"),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS","Invalid credentials"),
    JSON_PARSER_ERROR ("JSON_PARSER_ERROR ","Error while converting json object"),
    TOKEN_CREATION_ERROR("TOKEN_CREATION_ERROR",  "Error while creating token"),
    USER_EXISTS_ERROR("USER_EXISTS_ERROR","Duplicate user name"),
    EMAIL_SEND_ERROR( "EMAIL_SEND_ERROR","Error while sending mail"),
    CLIENT_NOT_FOUND( "CLIENT_NOT_FOUND","Please check client name,its not found"),
    NOTIFICATION_LIST_ERROR( "NOTIFICATION_LIST_ERROR","Error while fetching notification list"),
    UN_SUPPORTED_ENCODING_EXCEPTION("UN_SUPPORTED_ENCODING_EXCEPTION","Unsupported encoding exception"),
    NOTIFICATION_TYPE_IS_NULL("Notification type is null","Notification type is null");

    private final String name;
    private final String desc;

    ParamError(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

}
