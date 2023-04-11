package com.example.workflow.utils;

import okhttp3.MediaType;

import java.util.HashMap;
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

    public static final HashMap<String, String> GLOBAL_CANCELLATION_MESSAGE_EVENT_NAME= new HashMap<>();

    static {
        GLOBAL_CANCELLATION_MESSAGE_EVENT_NAME.put("Boooking_Flow","Global_Booking_Cancellation");
        GLOBAL_CANCELLATION_MESSAGE_EVENT_NAME.put("language_change_flow","Global_Language_Change_Cancellation");
    };
}
