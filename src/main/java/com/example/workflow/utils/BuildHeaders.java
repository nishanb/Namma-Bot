package com.example.workflow.utils;

import okhttp3.Headers;

import java.util.Map;

public class BuildHeaders {
    public static Headers buildHeaders(Map<String, String> headersMap) {
        Headers.Builder builder = new Headers.Builder();
        for (
                Map.Entry<String, String> entry : headersMap.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }
}
