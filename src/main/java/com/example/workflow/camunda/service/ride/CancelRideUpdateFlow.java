package com.example.workflow.camunda.service.ride;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.models.User;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CancelRideUpdateFlow implements JavaDelegate {

    private final Logger log = Logger.getLogger(DriverArrivedNotifier.class.getName());
    @Autowired
    CamundaCoreService camundaCoreService;
    @Autowired
    UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            log.info("<-- Ride Arrived Message-Service executed -->");

            Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
            User user = userSaved.get();

            // Remove sub process instance id to DB ( TODO : Use new backend user table & correlate when actual namma yatri api is provided )
            user.setSubProcessInstanceId(null);
            userService.updateUser(execution.getBusinessKey(), user);
        } catch (Exception e) {
            System.out.println("Exception >>>>>>" + e.getMessage());
            log.warning("ride.DriverArrived: Exception occurred......" + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
