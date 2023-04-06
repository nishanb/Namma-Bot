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
    RIDE_ENDED("Ride_Ended");

    private final String taskDefinitionKey;

    BpmnUserTask(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    // Default methods for enum reverse lookup

    private static final Map<String, BpmnUserTask> intToEnumMap = new HashMap<>();

    static {
        for (BpmnUserTask enumVal : BpmnUserTask.values()) {
            intToEnumMap.put(enumVal.getTaskDefinitionKey(), enumVal);
        }
    }

    public static BpmnUserTask fromTaskDefinitionKey(String taskDefinitionKey) {
        return intToEnumMap.get(taskDefinitionKey);
    }
}
