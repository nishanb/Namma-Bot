package com.example.workflow.utils;

import okhttp3.MediaType;

import java.util.Map;

public class Constants {
    public static final String INBOUND_WEBHOOK_EVENTS = "message";

    public static final String MESSAGE_TYPE_LIST = "list";

    public static final String MESSAGE_TYPE_TEXT = "text";

    public static final String MESSAGE_TYPE_QUICK_REPLY = "quick_reply";

    public static final String MESSAGE_TYPE_BUTTON_REPLY = "button_reply";
    public static final String MESSAGE_TYPE_LOCATION_REPLY = "location";

    public static final String MESSAGE_TYPE_LIST_REPLY = "list_reply";

    public static final String ATTACHMENT_TYPES_IMAGE = "image";

    public static final String ATTACHMENT_TYPES_FILE = "file";

    public static final String WHATSAPP_CHANNEL = "whatsapp";

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final MediaType X_WWW = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    public static final String SUBMITTED = "submitted";

}
