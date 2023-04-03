package com.example.workflow.service;

import camundajar.impl.com.google.gson.JsonElement;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public interface NammaYathriService {

    //TODO: For now will return JsonElement if time permits will make model mapping and create getter and setter methods
    public JsonElement getStarredPlaces() throws IOException;

    public JsonElement createStarredPlace(String latitude, String longitude, String name) throws IOException;

    public JsonElement deleteStarredPlace(String placeId) throws IOException;

    public JsonElement generateEstimate(String sourceLatitude, String sourceLongitude, String destinationLatitude, String destinationLongitude) throws IOException;

    public JsonElement findNearByRide(String sourceLatitude, String sourceLongitude, String destinationLatitude, String destinationLongitude, String rideSelectionMode) throws IOException;

    public JsonElement bookRide(String driverId, String rideId) throws IOException;

    public JsonElement cancelBooking(String rideId, String cancellationReason) throws IOException;

    public JsonElement rateDriver(String rideId, String rating) throws IOException;
}