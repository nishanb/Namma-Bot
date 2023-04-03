package com.example.workflow.utils;

import camundajar.impl.com.google.gson.Gson;
import camundajar.impl.com.google.gson.JsonArray;
import camundajar.impl.com.google.gson.JsonElement;
import camundajar.impl.com.google.gson.JsonObject;
import com.example.workflow.exception.HttpRequestException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class MessageServiceRestHandler {
    private static OkHttpClient client;
    private static Gson gson;

    private static Logger logger = LoggerFactory.getLogger(MessageServiceRestHandler.class);

    static {
        client = new OkHttpClient().newBuilder().build();
        gson = new Gson();
    }

    private MessageServiceRestHandler() {
    }

    public static OkHttpClient getClient() {
        return client;
    }
    public static JsonElement execute(Request request) throws IOException, HttpRequestException {
        System.out.println(request.method().toUpperCase() + " Request to " + request.url().toString() + " started");
        logger.info(request.method().toUpperCase() + " Request to " + request.url().toString() + " started");

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                logger.info(request.method().toUpperCase() + " Request to " + request.url().toString() + " is successful. Parsing response...");
                JsonElement jsonElement = gson.fromJson(Objects.requireNonNull(response.body()).charStream(), JsonElement.class);

                if (jsonElement.isJsonArray()) {
                    logger.info("Response is an array...parsing...");
//                    JsonArray jsonArray = jsonElement.getAsJsonArray();
//                    for (JsonElement element : jsonArray) {
//                        JsonObject jsonObject = element.getAsJsonObject();
//                        System.out.println(jsonObject.toString());
//                    }
                } else if (jsonElement.isJsonObject()) {
                    logger.info("Response is an object...parsing...");
//                    JsonObject jsonObject = jsonElement.getAsJsonObject();
//                    System.out.println(jsonObject.toString());
                } else {
                    logger.error("Failed to parse response");
                    System.out.println("Failed to parse response");
                }

                System.out.println(request.method().toString().toUpperCase() + " Request to " + request.url().toString() + " is complete");
                logger.info(request.method().toString().toUpperCase() + " Request to " + request.url().toString() + " is complete");

                return jsonElement;
            } else {
                logger.error("Response failed with status code " + response.code() + " and error message: " + response.message());
                System.out.println("<<<<<< error >>>>>" + response.toString());
                return null;
            }

        } catch (Exception e) {
            throw new HttpRequestException(e.getMessage());
        }
    }
}

