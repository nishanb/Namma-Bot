package com.example.workflow.utils;

import camundajar.impl.com.google.gson.JsonElement;
import com.example.workflow.exceptions.HttpRequestException;
import okhttp3.RequestBody;

import java.io.IOException;

public interface MessageServiceApiHelper {
    JsonElement get(String url) throws IOException, HttpRequestException;

    JsonElement post(String url, RequestBody body) throws IOException, HttpRequestException;

    JsonElement put(String url, RequestBody requestBody) throws IOException, HttpRequestException;

    JsonElement delete(String url, RequestBody body) throws IOException, HttpRequestException;
}
