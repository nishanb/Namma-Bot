package com.example.workflow.services;

import com.example.workflow.dto.BackendEventRequestDto;

public interface BackendEventHandlerService {
    public boolean handelEvent(BackendEventRequestDto event);
}
