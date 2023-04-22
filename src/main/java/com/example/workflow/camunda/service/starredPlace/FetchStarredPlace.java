package com.example.workflow.camunda.service.starredPlace;

import camundajar.impl.com.google.gson.JsonArray;
import camundajar.impl.com.google.gson.JsonElement;
import camundajar.impl.com.google.gson.JsonObject;
import com.example.workflow.services.NammaYathriService;
import com.google.gson.Gson;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.logging.Logger;

@Service
public class FetchStarredPlace implements JavaDelegate {

    private final Logger log = Logger.getLogger(FetchStarredPlace.class.getName());

    @Autowired
    NammaYathriService nammaYathriService;

    @Resource
    private Gson gson;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Executing Service Task " + this.getClass().getName() + " For Business Key: " + execution.getBusinessKey());
        try {
            JsonElement starredPlaces = nammaYathriService.getStarredPlaces(execution.getBusinessKey());

            // Filter users starred places
            JsonArray usersStarredPlaces = new JsonArray();
            for (JsonElement place : starredPlaces.getAsJsonArray()) {
                JsonObject object = place.getAsJsonObject();
                if (object.get("phone").getAsString().equals(execution.getBusinessKey())) {
                    usersStarredPlaces.add(object);
                }
            }

            // store response to cpo
            execution.setVariable("places_stored", usersStarredPlaces.getAsJsonArray().size());
            execution.setVariable("places", usersStarredPlaces.getAsJsonArray().toString());
        } catch (Exception e) {
            log.warning("Exception occurred in Service Activity : " + this.getClass().getName() + " " + e.getMessage());
            throw new BpmnError("starred_place_flow_error", "Error sending message.....");
        }
    }
}
