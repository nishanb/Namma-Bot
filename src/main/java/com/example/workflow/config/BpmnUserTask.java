package com.example.workflow.config;

import java.util.HashMap;
import java.util.Map;

public enum BpmnUserTask {

    RIDE_RECEIVE_DESTINATION_LOCATION("Receive_Destination_Location"),
    RIDE_RECEIVE_SOURCE_LOCATION("Receive_Pickup_Location"),
    RIDE_RECEIVE_BOOKING_TYPE("Receive_Booking_Type"),
    RIDE_CONFIRMATION("Ride_Confirmation"),
    RIDE_RETRY_SELECTION("Receive_Retry_Selection"),
    RIDE_DRIVER_ARRIVED("Driver_Arrived"),
    RIDE_STARTED("Ride_Started"),
    RIDE_CAPTURE_RIDE_RATING("Capture_Driver_Rating"),
    RIDE_ENDED("Ride_Ended"),
    LANGUAGE_UPDATE_PREFERENCE_SELECTION("Language_Selection"),
    LANGUAGE_UPDATE_PREFERENCE_CONFIRMATION("Language_Change_Confirmation"),
    STARRED_PLACE_DESIRED_ACTION("Receive_Desired_Action"),
    STARRED_PLACE_RECEIVE_LOCATION_TO_ADD("Receive_Location_To_Add"),
    STARRED_PLACE_RECEIVE_LOCATION_TAG_TO_ADD("Receive_Location_Tag"),
    STARRED_PLACE_RECEIVE_LOCATION_TO_DELETE("Receive_Location_To_Delete"),
    CANCEL_RIDE_REQUEST_BY_CX("Cancel_Ride_CX"),
    Need_Help_Request("Need_Help_Request");
    private static final Map<String, BpmnUserTask> intToEnumMap = new HashMap<>();

    static {
        for (BpmnUserTask enumVal : BpmnUserTask.values()) {
            intToEnumMap.put(enumVal.getTaskDefinitionKey(), enumVal);
        }
    }

    private final String taskDefinitionKey;

    // Default methods for enum reverse lookup

    BpmnUserTask(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public static BpmnUserTask fromTaskDefinitionKey(String taskDefinitionKey) {
        return intToEnumMap.get(taskDefinitionKey);
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }
}
