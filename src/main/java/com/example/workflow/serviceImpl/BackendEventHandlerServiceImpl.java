package com.example.workflow.serviceImpl;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.camunda.userTasks.rideUpdate.DriverArrivedAlert;
import com.example.workflow.camunda.userTasks.rideUpdate.RideEndedAlert;
import com.example.workflow.camunda.userTasks.rideUpdate.RideStartedAlert;
import com.example.workflow.config.BackendEvent;
import com.example.workflow.config.BpmnUserTask;
import com.example.workflow.dto.BackendEventRequestDto;
import com.example.workflow.models.User;
import com.example.workflow.services.BackendEventHandlerService;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BackendEventHandlerServiceImpl implements BackendEventHandlerService {

    @Autowired
    UserService userService;

    @Autowired
    CamundaCoreService camundaCoreService;

    @Autowired
    DriverArrivedAlert driverArrivedAlert;

    @Autowired
    RideEndedAlert rideEndedAlert;

    @Autowired
    RideStartedAlert rideStartedAlert;

    @Override
    public boolean handelEvent(BackendEventRequestDto event) throws Exception {
        if (event.getRiderPhoneNumber() == null) return false;

        User user = userService.findUserByPhoneNumber(event.getRiderPhoneNumber()).orElse(null);
        if (user == null || user.getProcessInstanceId() == null) return false;

        Task currentTask = camundaCoreService.getTaskByProcessDefinitionAndBusinessKey(user.getSubProcessInstanceId(), user.getPhoneNumber());
        switch (BackendEvent.fromEventType(event.getEvent())) {
            case DRIVER_ARRIVED:
                // Before completing backend task Check event received is matching with the backend event
                if (BpmnUserTask.fromTaskDefinitionKey(currentTask.getTaskDefinitionKey()) == BpmnUserTask.RIDE_DRIVER_ARRIVED) {
                    driverArrivedAlert.complete(currentTask, user, event);
                }
                break;
            case RIDE_STARTED:
                if (BpmnUserTask.fromTaskDefinitionKey(currentTask.getTaskDefinitionKey()) == BpmnUserTask.RIDE_STARTED) {
                    rideStartedAlert.complete(currentTask, user, event);
                }
                break;
            case RIDE_ENDED:
                if (BpmnUserTask.fromTaskDefinitionKey(currentTask.getTaskDefinitionKey()) == BpmnUserTask.RIDE_ENDED) {
                    rideEndedAlert.complete(currentTask, user, event);
                }
                break;
            // TODO : handel this dude
            case RIDE_CANCELED_BY_DRIVER:
                System.out.println("Ride canceled by rider " + event.getMessage());
                break;
            case default:
        }
        return false;
    }
}