package com.example.workflow.service;

import camundajar.impl.com.google.gson.JsonElement;
import com.example.workflow.exception.HttpRequestException;
import okhttp3.Headers;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.Map;

public interface MessageServiceApiHelper {
    public JsonElement get(String url, Map<String, String> headers) throws IOException, HttpRequestException;

    public JsonElement post(String url, RequestBody body, Map<String, String> headers) throws IOException, HttpRequestException;

    public JsonElement put(String url, RequestBody requestBody, Map<String, String> headers) throws IOException, HttpRequestException;

    public JsonElement delete(String url, RequestBody body, Map<String, String> headers) throws IOException, HttpRequestException;
}
