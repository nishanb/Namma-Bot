package com.example.workflow.serviceImpl;

import camundajar.impl.com.google.gson.JsonElement;
import com.example.workflow.exception.HttpRequestException;
import com.example.workflow.service.MessageServiceApiHelper;
import com.example.workflow.utils.BuildHeaders;
import com.example.workflow.utils.MessageServiceRestHandler;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class WhatsappMsgServiceApiHelper implements MessageServiceApiHelper {
    @Override
    public JsonElement get(String url, Map<String, String> headers) throws IOException, HttpRequestException {
        Request request = new Request.Builder()
                .url(url)
                .headers(BuildHeaders.buildHeaders(headers))
                .method("GET", null)
                .build();

        return MessageServiceRestHandler.execute(request);
    }

    @Override
    public JsonElement post(String url, RequestBody body, Map<String, String> headers) throws IOException, HttpRequestException {
        Request request = new Request.Builder()
                .url(url)
                .headers(BuildHeaders.buildHeaders(headers))
                .method("POST", body)
                .build();

        return MessageServiceRestHandler.execute(request);
    }

    @Override
    public JsonElement put(String url, RequestBody body, Map<String, String> headers) throws IOException, HttpRequestException {
        Request request = new Request.Builder()
                .url(url)
                .headers(BuildHeaders.buildHeaders(headers))
                .method("POST", body)
                .build();

        return MessageServiceRestHandler.execute(request);
    }

    @Override
    public JsonElement delete(String url, RequestBody body, Map<String, String> headers) throws IOException, HttpRequestException {
        Request request = new Request.Builder()
                .url(url)
                .headers(BuildHeaders.buildHeaders(headers))
                .method("DELETE", body)
                .build();

        return MessageServiceRestHandler.execute(request);
    }
}
