package com.example.workflow.config;

import java.util.HashMap;
import java.util.Map;

public enum WorkFlowType {

    // TODO: update process definition key
    RIDE_BOOKING("Boooking_Flow"),
    UPDATE_LANGUAGE("language_change_flow");

    private final String processDefinitionName;

    WorkFlowType(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    // Default methods for enum reverse lookup

    private static final Map<String, WorkFlowType> intToEnumMap = new HashMap<>();

    static {
        for (WorkFlowType enumVal : WorkFlowType.values()) {
            intToEnumMap.put(enumVal.getProcessDefinitionName(), enumVal);
        }
    }

    public static WorkFlowType fromProcessDefinitionName(String processDefinitionKey) {
        return intToEnumMap.get(processDefinitionKey);
    }
}
