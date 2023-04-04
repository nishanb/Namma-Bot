package com.example.workflow.utils;

import camundajar.impl.com.google.gson.JsonElement;
import com.example.workflow.exceptions.HttpRequestException;
import okhttp3.RequestBody;

import java.io.IOException;

public interface MessageServiceApiHelper {
    public JsonElement get(String url) throws IOException, HttpRequestException;

    public JsonElement post(String url, RequestBody body) throws IOException, HttpRequestException;

    public JsonElement put(String url, RequestBody requestBody) throws IOException, HttpRequestException;

    public JsonElement delete(String url, RequestBody body) throws IOException, HttpRequestException;
}
