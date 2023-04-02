package com.example.workflow.controller;

import camundajar.impl.com.google.gson.JsonElement;
import camundajar.impl.com.google.gson.JsonObject;
import com.example.workflow.service.NammaYathriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

// Use this controller to test service / anything :( anything .... :()
//TODO: Remove this before release
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    NammaYathriService nammaYathriService;

    @GetMapping
    public void test() throws IOException {
//        nammaYathriService.getStarredPlaces();
//        nammaYathriService.createStarredPlace("1.1", "1.2", "Mansion");
//        nammaYathriService.deleteStarredPlace("ba5a3d0e-2250-418a-8981-063fa092a92d");
//        nammaYathriService.deleteStarredPlace("ba5a3d0e-2250-418a-8981-063fa092a92d");
//        nammaYathriService.generateEstimate("48.8960", "78.8960", "-78.8960", "-74.8960");
//        nammaYathriService.findNearByRide("48.8960", "78.8960", "-78.8960", "-74.8960", "auto");
//        nammaYathriService.findNearByRide("48.8960", "78.8960", "-78.8960", "-74.8960", "manual");
//        nammaYathriService.bookRide("34a55cd6-2d85-42bc-9c7b-67db3cf08461", "3acd51b6-4ff7-40e9-8b0f-4d3f296b51db");
//        nammaYathriService.cancelBooking("ccbef961-d1a9-4424-a412-06ff7f7104f2", "Sorry, i have a bf");
//        nammaYathriService.rateDriver("70d8244d-7ef1-434c-96c9-ba1820fc199", "3");

        JsonElement response = nammaYathriService.getStarredPlaces();

        for (JsonElement element : response.getAsJsonArray()) {
            JsonObject jsonObject = element.getAsJsonObject();
            System.out.println(jsonObject.get("name"));
        }
    }
}
