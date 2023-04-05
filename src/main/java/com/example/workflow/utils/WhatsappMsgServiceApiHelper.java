package com.example.workflow.utils;

import camundajar.impl.com.google.gson.JsonElement;
import com.example.workflow.exceptions.HttpRequestException;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsappMsgServiceApiHelper implements MessageServiceApiHelper {

    @Value("${gupshup-api-key}")
    private String vendorApiKey;

    @Override
    public JsonElement get(String url) throws IOException, HttpRequestException {

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        requestHeaders.put("apikey", vendorApiKey);

        Request request = new Request.Builder()
                .url(url)
                .headers(BuildHeaders.buildHeaders(requestHeaders))
                .method("GET", null)
                .build();

        return MessageServiceRestHandler.execute(request);
    }

    @Override
    public JsonElement post(String url, RequestBody body) throws IOException, HttpRequestException {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        requestHeaders.put("apikey", vendorApiKey);

        Request request = new Request.Builder()
                .url(url)
                .headers(BuildHeaders.buildHeaders(requestHeaders))
                .method("POST", body)
                .build();

        return MessageServiceRestHandler.execute(request);
    }

    @Override
    public JsonElement put(String url, RequestBody body) throws IOException, HttpRequestException {

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        requestHeaders.put("apikey", vendorApiKey);


        Request request = new Request.Builder()
                .url(url)
                .headers(BuildHeaders.buildHeaders(requestHeaders))
                .method("POST", body)
                .build();

        return MessageServiceRestHandler.execute(request);
    }

    @Override
    public JsonElement delete(String url, RequestBody body) throws IOException, HttpRequestException {

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        requestHeaders.put("apikey", vendorApiKey);

        Request request = new Request.Builder()
                .url(url)
                .headers(BuildHeaders.buildHeaders(requestHeaders))
                .method("DELETE", body)
                .build();

        return MessageServiceRestHandler.execute(request);
    }
}
