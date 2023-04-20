package com.example.workflow.services;

import com.example.workflow.dto.BackendEventRequestDto;

public interface BackendEventHandlerService {
    boolean handelEvent(BackendEventRequestDto event) throws Exception;
}
