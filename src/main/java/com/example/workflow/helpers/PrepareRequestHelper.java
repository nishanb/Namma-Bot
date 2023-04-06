package com.example.workflow.helpers;

import camundajar.impl.com.google.gson.Gson;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.RequestBody;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import static com.example.workflow.utils.Constants.*;

@Service
public class PrepareRequestHelper {
    public RequestBody prepareRequestBody(JSONObject jsonObject) {
        return RequestBody.create(jsonObject.toString(), JSON);
    }

    public RequestBody prepareRequestBodyForXUrlEncodedType(String requestBody) {
        return RequestBody.create(X_WWW, requestBody);
    }

    public static String stringifyJson(Object object) throws JsonProcessingException {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static String stringifyRequestBody(String channel, String sourceContactNo, String receiverContactNumber, String messagePayload, String vendorAppName) {
        return String.format("channel=%s&source=%s&destination=%s&message=%s&src.name=%s", channel, sourceContactNo, receiverContactNumber, messagePayload, vendorAppName);
    }
}