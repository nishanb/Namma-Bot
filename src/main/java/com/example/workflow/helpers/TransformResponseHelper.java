package com.example.workflow.helpers;

import com.example.workflow.dto.MessageServiceResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransformResponseHelper {
    static ObjectMapper objectMapper = new ObjectMapper();

    public static MessageServiceResponseDto transformMessageServiceResponse(String response) throws JsonProcessingException {
        return objectMapper.readValue(response, MessageServiceResponseDto.class);
    }
}
