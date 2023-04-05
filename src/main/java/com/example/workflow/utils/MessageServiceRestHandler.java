package com.example.workflow.utils;

import camundajar.impl.com.google.gson.Gson;
import camundajar.impl.com.google.gson.JsonElement;
import com.example.workflow.exceptions.HttpRequestException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

