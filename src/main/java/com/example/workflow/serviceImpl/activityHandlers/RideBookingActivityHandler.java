package com.example.workflow.serviceImpl.activityHandlers;

import com.example.workflow.camunda.userTasks.rideBooking.*;
import com.example.workflow.config.BpmnUserTask;
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
    ReceiveDestinationLocation receiveDestinationLocationTask;

    @Autowired
    ReceiveSourceLocation receiveSourceLocationTask;

    @Autowired
    ManualRideConfirmation manualRideConfirmationTask;

    @Autowired
    ReceiveBookingType receiveBookingTypeTask;

    @Autowired
    ReceiveRideRating receiveRideRatingTask;

    @Autowired
    ReceiveRetrySelection receiveRetrySelectionTask;

    private static final Logger logger = LoggerFactory.getLogger(RideBookingActivityHandler.class);

    @Override
    public void handle(Task task, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception {
        switch (BpmnUserTask.fromTaskDefinitionKey(task.getTaskDefinitionKey())) {
            case RIDE_RECEIVE_DESTINATION_LOCATION -> {
                receiveDestinationLocationTask.complete(task, user, messageType, webhookMessagePayload);
            }
            case RIDE_RECEIVE_SOURCE_LOCATION -> {
                receiveSourceLocationTask.complete(task, user, messageType, webhookMessagePayload);
            }
            case RIDE_CONFIRMATION -> {
                manualRideConfirmationTask.complete(task, user, messageType, webhookMessagePayload);
            }
            case RIDE_RECEIVE_BOOKING_TYPE -> {
                receiveBookingTypeTask.complete(task, user, messageType, webhookMessagePayload);
            }
            case RIDE_RETRY_SELECTION -> {
                receiveRetrySelectionTask.complete(task, user, messageType, webhookMessagePayload);
            }
            case RIDE_CAPTURE_RIDE_RATING -> {
                receiveRideRatingTask.complete(task, user, messageType, webhookMessagePayload);
            }
            case default -> {
                logger.info(String.format(">>>>>>>> No user task class found for %s <<<<<<<<<", task.getName()));
            }
        }
    }
}