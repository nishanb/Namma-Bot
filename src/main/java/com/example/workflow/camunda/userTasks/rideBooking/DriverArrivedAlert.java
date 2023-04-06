package com.example.workflow.camunda.userTasks.rideBooking;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.camunda.userTasks.BackendEventTask;
import com.example.workflow.camunda.userTasks.UserTask;
import com.example.workflow.dto.BackendEventRequestDto;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.WebhookMessagePayload;
import com.example.workflow.utils.Constants;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class DriverArrivedAlert implements BackendEventTask {

    @Autowired
    CamundaCoreService camundaCoreService;

    @Override
    public void complete(Task task, User user, BackendEventRequestDto backendEventRequestDto) throws Exception {
        System.out.println("Rider arrived");
    }
}
