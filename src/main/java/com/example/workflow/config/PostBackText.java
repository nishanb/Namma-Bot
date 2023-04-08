package com.example.workflow.config;

import java.util.HashMap;
import java.util.Map;

public enum PostBackText {

    AUTO_ASSIGN("AUTO_ASSIGN"),
    CHOOSE_MANUAL("CHOOSE_MANUAL"),
    CHANGE_LOCATION("CHANGE_LOCATION"),
    LANG_PREF_KANNADA("KANNADA"),
    LANG_PREF_ENGLISH("ENGLISH"),
    LANG_PREF_HINDI("HINDI"),
    LANG_PREF_CANCEL("cancel"),
    LANG_PREF_RE_UPDATE("changeSelection"),
    LANG_PREF_APPROVE("confirm");
    private final String postBackText;

    PostBackText(String eventType) {
        this.postBackText = eventType;
    }

    public String getPostBackText() {
        return postBackText;
    }

    // Default methods for enum reverse lookup

    private static final Map<String, PostBackText> intToEnumMap = new HashMap<>();

    static {
        for (PostBackText enumVal : PostBackText.values()) {
            intToEnumMap.put(enumVal.getPostBackText(), enumVal);
        }
    }

    public static PostBackText fromPostBackText(String eventType) {
        return intToEnumMap.get(eventType);
    }
}
