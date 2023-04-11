package com.example.workflow.utils;

import camundajar.impl.com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class BackendEventSimulatorHelper {

    @Value("${callback-host}")
    private String eventCallBackHost;

    @Value("${event-simulator-host}")
    private String eventSimulatorHost;

    private static Logger logger = LoggerFactory.getLogger(BackendEventSimulatorHelper.class);

    private final Gson gson;

    public BackendEventSimulatorHelper() {
        this.gson = new Gson();
    }

    public void simulateRideEvents(String riderPhoneNumber, Integer rideArrivedDelay, Integer rideStartedDelay, Integer rideEndedDelay) throws IOException {
        try {

            logger.info("Initiated Ride simulated event for  " + riderPhoneNumber);

            Map<String, String> requestBodyMap = new HashMap<>();
            requestBodyMap.put("riderPhoneNumber", riderPhoneNumber);
            requestBodyMap.put("callBackUrl", eventCallBackHost);
            requestBodyMap.put("rideStartedDelay", String.valueOf(rideStartedDelay));
            requestBodyMap.put("rideArrivedDelay", String.valueOf(rideArrivedDelay));
            requestBodyMap.put("rideEndedDelay", String.valueOf(rideEndedDelay));

            String json = this.gson.toJson(requestBodyMap);
            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(eventSimulatorHost + "/api/v1/simulate-ride-events")
                    .post(body)
                    .build();

            RestHandler.execute(request);
            logger.info("<< Ride simulation events triggered for  >>" + riderPhoneNumber);

        } catch (Exception e) {
            logger.info("Exception in simulate event " + e.getMessage());
        }
    }
}