package com.example.workflow.camunda.service.booking;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.models.User;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import static com.example.workflow.utils.Constants.GLOBAL_CANCELLATION_MESSAGE_EVENT_NAME;

@Service
public class CancelBooking implements JavaDelegate {

    private final Logger log = Logger.getLogger(CancelBooking.class.getName());
    @Autowired
    UserService userService;

    @Autowired
    CamundaCoreService camundaCoreService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Executing Service Task " + this.getClass().getName() + " For Business Key: " + execution.getBusinessKey());

        try {
            Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
            User user = userSaved.get();

            //Updating user process instance by empty string
            userService.updateProcessInstanceIdByUserId(user.getId(), null);
            if(!(user.getSubProcessInstanceId() == null)){
                Map<String, Object> variables = new HashMap<>();
                variables.put("cancel_ride_request", true);
                //TODO: Get the Process definition name from config.
                String cancelMessageEventName = GLOBAL_CANCELLATION_MESSAGE_EVENT_NAME.get("Ride_Update_Flow");
                camundaCoreService.createMessageCorrelation(user.getPhoneNumber(), cancelMessageEventName, variables);
            }

            execution.setVariable("CancelBooking", true);
        } catch (Exception e) {
            log.warning("Exception occurred in Service Task : " + this.getClass().getName() + " " + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
