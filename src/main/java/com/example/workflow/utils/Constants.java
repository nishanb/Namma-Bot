package com.example.workflow.utils;

import okhttp3.MediaType;

import java.util.Map;

public class Constants {
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public enum AllowedHttpVerbs {GET, POST, PUT, DELETE}

    public static final String MESSAGE = "message";

    public static final String INBOUND_WEBHOOK_EVENTS = "message";

    public static final String HTTP_REQUEST_TYPE = "REQUESTED";
    public static final String HTTP_RESPONSE_TYPE = "RESPONDED";

    public static final String BEARER_STRING = "Bearer ";

    public static final String MESSAGE_TYPE_LIST = "list";

    public static final String MESSAGE_TYPE_TEXT = "text";

    public static final String MESSAGE_TYPE_QUICK_REPLY = "quick_reply";

    public static final String MESSAGE_TYPE_BUTTON_REPLY = "button_reply";

    public static final String MESSAGE_TYPE_LIST_REPLY = "list_reply";

    public static final String ATTACHMENT_TYPES_IMAGE = "image";

    public static final String ATTACHMENT_TYPES_FILE = "file";

    public static final String WHATSAPP_CHANNEL = "whatsapp";

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final MediaType X_WWW = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    public static final String SUBMITTED = "submitted";

    public static final String STATUS = "status";

    public static final Map<String, String> CAMUNDA_WORKFLOW_PROCESS_NAME_MAP = Map.of(
            "BOOK_RIDE", "Process_1bgrwav:6:454ab604-d21a-11ed-8444-6efb47c1fefa",
            "PREVIOUS_RIDE", "camunda_id_goes_here",
            "ADD_PLACE", "camunda_id_goes_here",
            "DELETE_PLACE", "camunda_id_goes_here",
            "SUPPORT", "camunda_id_goes_here",
            "OTHER", "camunda_id_goes_here"
    );

}
