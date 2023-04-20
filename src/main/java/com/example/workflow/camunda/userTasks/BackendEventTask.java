package com.example.workflow.camunda.userTasks;

import com.example.workflow.dto.BackendEventRequestDto;
import com.example.workflow.models.User;
import org.camunda.bpm.engine.task.Task;

public interface BackendEventTask {
    void complete(Task task, User user, BackendEventRequestDto backendEventRequestDto) throws Exception;
}
