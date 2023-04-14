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

    public static final HashMap<String, String> GLOBAL_CANCELLATION_MESSAGE_EVENT_NAME = new HashMap<>();

    static {
        GLOBAL_CANCELLATION_MESSAGE_EVENT_NAME.put("Boooking_Flow", "Global_Booking_Cancellation");
        GLOBAL_CANCELLATION_MESSAGE_EVENT_NAME.put("language_change_flow", "Global_Language_Change_Cancellation");
        GLOBAL_CANCELLATION_MESSAGE_EVENT_NAME.put("Starred_Places_Flow", "Global_Starred_Place_Cancellation");
        GLOBAL_CANCELLATION_MESSAGE_EVENT_NAME.put("Ride_Update_Flow", "Global_Ride_Update_Cancellation");
    }


    // TODO : enable these when switched to gupshup paid version
    //public static final String RATE_CARD_ENGLISH = "https://i.postimg.cc/QN0X5nGS/rate-card-english.jpg";
    //public static final String RATE_CARD_KANNADA = "https://i.postimg.cc/1XH2xTPt/rate-card-kannada.jpg";
    //public static final String RATE_CARD_HINDI = "https://i.postimg.cc/MTXY5pKd/rate-card-hindi.jpg";
    public static final String RATE_CARD_ENGLISH = "https://www.buildquickbots.com/whatsapp/media/sample/jpg/sample01.jpg";
    public static final String RATE_CARD_KANNADA = "https://www.buildquickbots.com/whatsapp/media/sample/jpg/sample01.jpg";
    public static final String RATE_CARD_HINDI = "https://www.buildquickbots.com/whatsapp/media/sample/jpg/sample01.jpg";
}
