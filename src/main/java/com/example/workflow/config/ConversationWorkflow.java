package com.example.workflow.config;

import java.util.HashMap;
import java.util.Map;

public enum ConversationWorkflow {

    RIDE_BOOKING("Boooking_Flow", "BOOK_RIDE"),
    UPDATE_LANGUAGE("language_change_flow", "UPDATE_LANGUAGE"),
    PREVIOUS_RIDE("", "PREVIOUS_RIDE"),
    SUPPORT("", "SUPPORT"),
    MANAGE_PLACES("", "MANAGE_PLACES"),
    OTHER("", "OTHER"),
    KNOW_MORE("", "KNOW_MORE"),
    OPEN_DATA("", "OPEN_DATA"),
    FEEDBACK("", "FEEDBACK");

    private final String processDefinitionName;

    private final String postbackText;

    ConversationWorkflow(String processDefinitionName, String postbackText) {
        this.processDefinitionName = processDefinitionName;
        this.postbackText = postbackText;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public String getPostbackText() {
        return postbackText;
    }

    private static final Map<String, ConversationWorkflow> intToEnumMapProcessDefinition = new HashMap<>();
    private static final Map<String, ConversationWorkflow> intToEnumMapPostBack = new HashMap<>();

    static {
        // Default methods for enum reverse lookup fby definition id

        for (ConversationWorkflow enumVal : ConversationWorkflow.values()) {
            intToEnumMapProcessDefinition.put(enumVal.getProcessDefinitionName(), enumVal);
        }

        // Default methods for enum reverse lookup fby postback text

        for (ConversationWorkflow enumVal : ConversationWorkflow.values()) {
            intToEnumMapPostBack.put(enumVal.getPostbackText(), enumVal);
        }
    }

    public static ConversationWorkflow fromProcessDefinitionName(String processDefinitionKey) {
        return intToEnumMapProcessDefinition.get(processDefinitionKey);
    }

    public static ConversationWorkflow fromPostBackText(String postbackText) {
        return intToEnumMapPostBack.get(postbackText);
    }
}
