package com.example.workflow.serviceImpl;

import com.example.workflow.constants.BackendEventType;
import com.example.workflow.dto.BackendEventRequestDto;
import com.example.workflow.service.BackendEventHandler;
import org.springframework.stereotype.Service;

@Service
public class BackendEventHandlerImpl implements BackendEventHandler {

    // TODO :  get user phone and manage bpm logic here
    @Override
    public boolean handelEvent(BackendEventRequestDto event) {
        switch (event.getEvent()) {
            case BackendEventType.DRIVER_ARRIVED:
                System.out.println("Driver arrived " + event.getMessage());
                break;
            case BackendEventType.RIDE_STARTED:
                System.out.println("Ride Started " + event.getMessage());
                break;
            case BackendEventType.RIDE_ENDED:
                System.out.println("Ride ended " + event.getMessage());
                break;
            case BackendEventType.RIDE_CANCELED_BY_DRIVER:
                System.out.println("Ride canceled by rider " + event.getMessage());
                break;
            case default:
                return false;
        }
        System.out.println(event.getRiderPhoneNumber());
        System.out.println(event.getMessage());
        System.out.println(event.getData().toString());
        return true;
    }
}
