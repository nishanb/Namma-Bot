package com.example.workflow.helpers;

import camundajar.impl.com.google.gson.JsonElement;
import com.example.workflow.utils.RestHandler;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NammaYathriApiHelper {

    @Value("${ny-backend-host}")
    private String NammaYathriApiUrl;

    public JsonElement get(String url) throws IOException {

        Request request = new Request.Builder()
                .url(NammaYathriApiUrl + url)
                .method("GET", null)
                .build();

        return RestHandler.execute(request);
    }

    public JsonElement post(String url, RequestBody body) throws IOException {

        Request request = new Request.Builder()
                .url(NammaYathriApiUrl + url)
                .method("POST", body)
                .build();

        return RestHandler.execute(request);
    }

    public JsonElement delete(String url, RequestBody body) throws IOException {

        Request request = new Request.Builder()
                .url(NammaYathriApiUrl + url)
                .method("DELETE", body)
                .build();

        return RestHandler.execute(request);
    }
}
