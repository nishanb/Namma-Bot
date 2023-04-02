package com.example.workflow.utils;

import camundajar.impl.com.google.gson.Gson;
import camundajar.impl.com.google.gson.JsonArray;
import camundajar.impl.com.google.gson.JsonElement;
import camundajar.impl.com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    public static JsonElement execute(Request request) throws IOException {
        System.out.println(request.method().toString().toUpperCase() + " Request to " + request.url().toString() + " started");

        Response response = client.newCall(request).execute();
        JsonElement jsonElement = gson.fromJson(response.body().charStream(), JsonElement.class);


        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                System.out.println(jsonObject.toString());
            }
        } else if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            System.out.println(jsonObject.toString());
        } else {
            System.out.println("Failed to parse response");
        }

        System.out.println(request.method().toString().toUpperCase() + " Request to " + request.url().toString() + " is complete");

        return jsonElement;
    }
}
