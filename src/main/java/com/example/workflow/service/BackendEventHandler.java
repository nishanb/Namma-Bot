package com.example.workflow.service;

import com.example.workflow.dto.BackendEventRequestDto;

public interface BackendEventHandler {
    public boolean handelEvent(BackendEventRequestDto event);
}
