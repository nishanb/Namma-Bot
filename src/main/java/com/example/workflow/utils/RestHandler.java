package com.example.workflow.utils;

import camundajar.impl.com.google.gson.Gson;
import camundajar.impl.com.google.gson.JsonArray;
import camundajar.impl.com.google.gson.JsonElement;
import camundajar.impl.com.google.gson.JsonObject;
import com.example.workflow.serviceImpl.activityHandlers.StarredPlaceManageActivityHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RestHandler {
    private static OkHttpClient client;
    private static Gson gson;

    static {
        client = new OkHttpClient().newBuilder().build();
        gson = new Gson();
    }

    private RestHandler() {
    }

    public static OkHttpClient getClient() {
        return client;
    }

    private static final Logger logger = LoggerFactory.getLogger(RestHandler.class);

    public static JsonElement execute(Request request) throws IOException {
        logger.info(request.method().toUpperCase() + " Request to " + request.url() + " started");

        Response response = client.newCall(request).execute();
        JsonElement jsonElement = gson.fromJson(response.body().charStream(), JsonElement.class);

        /**
         * Disabled dumping response to system.out will result in higher latency
         if (jsonElement.isJsonArray()) {
         JsonArray jsonArray = jsonElement.getAsJsonArray();
         for (JsonElement element : jsonArray) {
         JsonObject jsonObject = element.getAsJsonObject();
         logger.info(jsonObject.toString());
         }
         } else if (jsonElement.isJsonObject()) {
         JsonObject jsonObject = jsonElement.getAsJsonObject();
         logger.info("Response " + jsonObject.toString());
         }
         **/

        logger.info(request.method().toUpperCase() + " Request to " + request.url() + " is complete wit status code " + response.code());
        return jsonElement;
    }
}

