package com.example.workflow.camunda.userTasks.rideBooking;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.camunda.userTasks.BackendEventTask;
import com.example.workflow.config.BackendEvent;
import com.example.workflow.dto.BackendEventRequestDto;
import com.example.workflow.models.User;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class RideEndedAlert implements BackendEventTask {
    @Autowired
    CamundaCoreService camundaCoreService;

    private final Logger log = Logger.getLogger(RideEndedAlert.class.getName());

    @Override
    public void complete(Task task, User user, BackendEventRequestDto backendEventRequestDto) throws Exception {
        log.info("--> Executing Ride Ended backend event user task <--");
        if (backendEventRequestDto.getEvent().equals(BackendEvent.RIDE_ENDED.getEventType())) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("namma_yatri_rider_ended_notify", true);
            variables.put("namma_yatri_rider_ended_timestamp", LocalTime.now().toString());
            camundaCoreService.completeUserTaskByTaskId(task, variables);
        }
    }
}
