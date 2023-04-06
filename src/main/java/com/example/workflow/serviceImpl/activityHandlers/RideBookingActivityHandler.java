package com.example.workflow.serviceImpl.activityHandlers;

import com.example.workflow.camunda.userTasks.rideBooking.ReceiveDestinationLocation;
import com.example.workflow.camunda.userTasks.rideBooking.ReceiveSourceLocation;
import com.example.workflow.config.UserTaskName;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.WebhookMessagePayload;
import com.example.workflow.services.ActivityHandlerService;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RideBookingActivityHandler implements ActivityHandlerService {

    @Autowired
    ReceiveDestinationLocation receiveDestinationLocation;

    @Autowired
    ReceiveSourceLocation receiveSourceLocation;

    private static final Logger logger = LoggerFactory.getLogger(RideBookingActivityHandler.class);


    @Override
    public void handle(Task task, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception {

        switch (task.getName()) {
            case UserTaskName.RECEIVE_DESTINATION_LOCATION -> {
                receiveDestinationLocation.complete(task, user, messageType, webhookMessagePayload);
            }
            case UserTaskName.RECEIVE_SOURCE_LOCATION -> {
                receiveSourceLocation.complete(task, user, messageType, webhookMessagePayload);
            }
            case default -> {
                logger.info(String.format(">>>>>>>> No user task class found for %s <<<<<<<<<", task.getName()));
            }
        }
    }
}
