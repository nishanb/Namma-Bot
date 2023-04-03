package com.example.workflow.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WorkflowServiceResponseDto {
    //This method needs to take response message, response status and response body (if any) and return
    //a standard ResponseEntity object
    public static ResponseEntity<Object> transformResponse(String responseMessage, HttpStatus responseStatus, Object responseBody, String dbErrorMsg) {
        Map<String, Object> mapOfResponse = new HashMap<>();
        put(mapOfResponse, "message", responseMessage);
        put(mapOfResponse, "status", responseStatus.value());
        put(mapOfResponse, "data", responseBody);
        put(mapOfResponse, "db-error", dbErrorMsg);
        put(mapOfResponse, "timestamp", new Date());
        return new ResponseEntity<Object>(mapOfResponse, responseStatus);
    }

    private static void put(Map<String, Object> Object, String key, Object value) {
        if (value != null) {
            Object.put(key, value);
        }
    }
}
