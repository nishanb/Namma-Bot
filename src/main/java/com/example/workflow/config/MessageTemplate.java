package com.example.workflow.config;

import java.util.HashMap;
import java.util.Map;

public enum MessageTemplate {
    GREET_MAIN_HEADER("Greet_Header_Main"),
    GREET_MAIN_CONTENT("Greet_Content_Main"),
    GREET_MAIN_OPTION_BOOK_RIDE("Greet_Content_Options_Main_Book_Ride"),
    GREET_MAIN_OPTION_VIEW_PAST_RIDE("Greet_Content_Options_Main_ViewPastRide"),
    GREET_MAIN_OPTION_MORE_OPTION("Greet_Content_Options_Main_More"),
    GREET_OTHER_HEADER("Greet_Header_Other"),
    GREET_OTHER_BODY("Greet_Content_Other"),
    OPTION_BUTTON_CHOOSE_FROM_HERE("Option_button_choose_from_here"),
    GREET_OTHER_SUB_HEADER("Greet_Other_sub_header"),
    GREET_OTHER_OPTION_MANAGE_FAV_TITLE("Greet_Content_Options_Other_fav_place_header"),
    GREET_OTHER_OPTION_MANAGE_FAV_DESC("Greet_Content_Options_Other_fav_place_description"),
    GREET_OTHER_OPTION_LANGUAGE_UPDATE_TITLE("Greet_Content_Options_Other_change_lang_header"),
    GREET_OTHER_OPTION_LANGUAGE_UPDATE_DESC("Greet_Content_Options_Other_change_lang_desc"),
    GREET_OTHER_OPTION_SUPPORT_TITLE("Greet_Content_Options_Other_support_header"),
    GREET_OTHER_OPTION_SUPPORT_DESC("Greet_Content_Options_Other_support_desc"),
    GREET_OTHER_OPTION_FEEDBACK_TITLE("Greet_Content_Options_Other_feedback_header"),
    GREET_OTHER_OPTION_FEEDBACK_DESC("Greet_Content_Options_Other_feedback_desc"),
    GREET_OTHER_OPTION_KNOW_MORE_TITLE("Greet_Content_Options_Other_know_more_header"),
    GREET_OTHER_OPTION_KNOW_MORE_DESC("Greet_Content_Options_Other_know_more_desc"),
    GREET_OTHER_OPTION_OPEN_DATA_TITLE("Greet_Content_Options_Other_live_board_title"),
    GREET_OTHER_OPTION_OPEN_DATA_DESC("Greet_Content_Options_Other_live_board_desc"),
    RIDE_REQUEST_DESTINATION_LOCATION("Request_Destination_Location"),
    RIDE_REQUEST_PICKUP_LOCATION("Request_Pickup_Location"),
    RIDE_INVALID_MESSAGE("Not_able_to_process_message"),
    RIDE_CALCULATE_FARE("Calculate_Fare_Details"),
    RIDE_CLOSING_CONVERSATION_FOR_NO_RESPONSE("Closing_Conversation_For_No_Response"),
    RIDE_CLOSING_CONVERSATION_FOR_NO_RESPONSE_HEADER("Closing_Conversation_For_No_Response_Header"),
    RIDE_BOOKING_TYPE_HEADER("Request_Booking_Type_Header"),
    RIDE_BOOKING_TYPE_BODY("Request_Booking_Type_Body"),
    RIDE_BOOKING_TYPE_OPTION_AUTO("Request_Booking_Type_Options_Auto"),
    RIDE_BOOKING_TYPE_OPTION_MANUAL("Request_Booking_Type_Options_Manual_Ride"),
    RIDE_BOOKING_TYPE_OPTION_UPDATE_LOCATION("Ride_update_location"),

    RIDE_BOOKING_TYPE_CLOSE_RIDE("Request_Booking_Type_Options_Cancel_Booking"),
    RIDE_FETCH_NEARBY_DRIVERS("Fetch_Nearby_Rides"),
    RIDE_RETRY_SELECTION_HEADER("Send_Retry_Selection_Header"),
    RIDE_RETRY_SELECTION_BODY("Send_Retry_Selection_Body"),
    RIDE_RETRY_SELECTION_OPTIONS_RETRY("Send_Retry_Selection_Options_Retry"),
    RIDE_RETRY_SELECTION_CANCEL_BOOKING("Send_Retry_Selection_Options_Cancel"),
    RIDE_MANUAL_CHOOSE_RIDER_HEADER("Request_Choose_Ride_Header"),
    RIDE_MANUAL_CHOOSE_RIDER_BODY("Request_Choose_Ride_Body"),
    RIDE_MANUAL_CHOOSE_RIDER_LIST_HEADER("Request_Choose_Ride_List_Header"),
    RIDE_MANUAL_CHOOSE_RIDER_LIST_DESC("Request_Choose_Ride_List_Description"),
    RIDE_ASSIGNED_INFO("Assign_The_Ride"),
    RIDE_DRIVER_ARRIVED_EVENT("Driver_Arrived_Book"),
    RIDE_STARTED_EVENT("Ride_Started_Update"),
    RIDE_ENDED_EVENT("Ride_Ended_Update"),
    RIDE_SHARE_RIDE_RATING_TITLE("Share_Driver_Rating_Header"),
    RIDE_SHARE_RIDE_RATING_DESC("Share_Driver_Rating_Body"),
    RIDE_SHARE_RIDE_RATING_OP_1_START_TITLE("Request_Choose_Ride_List_Header_1_star"),
    RIDE_SHARE_RIDE_RATING_OP_1_START_DESC("Request_Choose_Ride_List_Description_1_star"),
    RIDE_SHARE_RIDE_RATING_OP_2_START_TITLE("Request_Choose_Ride_List_Header_2_star"),
    RIDE_SHARE_RIDE_RATING_OP_2_START_DESC("Request_Choose_Ride_List_Description_2_star"),
    RIDE_SHARE_RIDE_RATING_OP_3_START_TITLE("Request_Choose_Ride_List_Header_3_star"),
    RIDE_SHARE_RIDE_RATING_OP_3_START_DESC("Request_Choose_Ride_List_Description_3_star"),
    RIDE_SHARE_RIDE_RATING_OP_4_START_TITLE("Request_Choose_Ride_List_Header_4_star"),
    RIDE_SHARE_RIDE_RATING_OP_4_START_DESC("Request_Choose_Ride_List_Description_4_star"),
    RIDE_SHARE_RIDE_RATING_OP_5_START_TITLE("Request_Choose_Ride_List_Header_5_star"),
    RIDE_SHARE_RIDE_RATING_OP_5_START_DESC("Request_Choose_Ride_List_Description_5_star"),
    RIDE_CANCELLED_BY_DRIVER("Ride_Request_Cancelled"),
    LANGUAGE_UPDATE_PREFERENCE_HEADER("Language_update_pref_header"),
    LANGUAGE_UPDATE_PREFERENCE_BODY("Language_update_pref_body"),

    LANGUAGE_UPDATE_CONFIRMATION_HEADER("Language_update_confirmation_header"),
    LANGUAGE_UPDATE_CONFIRMATION_BODY("Language_update_confirmation_body"),
    LANGUAGE_UPDATE_CONFIRMATION_OPTION_YES("Language_update_confirmation_option_yes"),
    LANGUAGE_UPDATE_CONFIRMATION_OPTION_NO("Language_update_confirmation_option_no"),
    LANGUAGE_UPDATE_CONFIRMATION_OPTION_CANCEL("Language_update_confirmation_option_cancel"),
    LANGUAGE_UPDATE_DONE("Language_update_Done"),
    GREET_MAIN_MENU("Main_menu_button"),
    LAST_OPERATION_CANCELED("last_operation_cancelled"),
    BOT_ERROR("Error");

    private final String templateId;

    MessageTemplate(String eventType) {
        this.templateId = eventType;
    }

    public String getTemplateId() {
        return templateId;
    }

    // Default methods for enum reverse lookup

    private static final Map<String, MessageTemplate> intToEnumMap = new HashMap<>();

    static {
        for (MessageTemplate enumVal : MessageTemplate.values()) {
            intToEnumMap.put(enumVal.getTemplateId(), enumVal);
        }
    }

    public static MessageTemplate fromTemplateId(String eventType) {
        return intToEnumMap.get(eventType);
    }
}
