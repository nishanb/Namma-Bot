package com.example.workflow.services;

import camundajar.impl.com.google.gson.JsonElement;

import java.io.IOException;

public interface NammaYathriService {

    //TODO: For now will return JsonElement if time permits will make model mapping and create getter and setter methods
    JsonElement getStarredPlaces(String userPhone) throws IOException;

    JsonElement createStarredPlace(String latitude, String longitude, String name, String userPhone) throws IOException;

    JsonElement deleteStarredPlace(String placeId) throws IOException;

    JsonElement generateEstimate(String sourceLatitude, String sourceLongitude, String destinationLatitude, String destinationLongitude) throws IOException;

    JsonElement findNearByRide(String sourceLatitude, String sourceLongitude, String destinationLatitude, String destinationLongitude, String rideSelectionMode) throws IOException;

    JsonElement bookRide(String driverId, String rideId) throws IOException;

    JsonElement cancelBooking(String rideId, String cancellationReason) throws IOException;

    JsonElement rateDriver(String rideId, String rating) throws IOException;
}
