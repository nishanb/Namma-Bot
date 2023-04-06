package com.example.workflow.config;

import java.util.HashMap;
import java.util.Map;

public enum BpmnWorkFlow {

    RIDE_BOOKING("Boooking_Flow"),
    UPDATE_LANGUAGE("language_change_flow");

    private final String processDefinitionName;

    BpmnWorkFlow(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    // Default methods for enum reverse lookup

    private static final Map<String, BpmnWorkFlow> intToEnumMap = new HashMap<>();

    static {
        for (BpmnWorkFlow enumVal : BpmnWorkFlow.values()) {
            intToEnumMap.put(enumVal.getProcessDefinitionName(), enumVal);
        }
    }

    public static BpmnWorkFlow fromProcessDefinitionName(String processDefinitionKey) {
        return intToEnumMap.get(processDefinitionKey);
    }
}
