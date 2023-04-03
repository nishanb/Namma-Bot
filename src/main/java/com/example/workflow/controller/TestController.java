package com.example.workflow.controller;

import camundajar.impl.com.google.gson.Gson;
import camundajar.impl.com.google.gson.JsonElement;
import camundajar.impl.com.google.gson.JsonObject;
import com.example.workflow.dto.*;
import com.example.workflow.models.ListMessage;
import com.example.workflow.service.MessageService;
import com.example.workflow.service.MessageServiceWebhookHandler;
import com.example.workflow.service.NammaYathriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

// Use this controller to test service / anything :( anything .... :()
//TODO: Remove this before release
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    NammaYathriService nammaYathriService;

    @Autowired
    MessageService messageService;

    @Autowired
    MessageServiceWebhookHandler messageServiceWebhookHandler;

    @GetMapping
    public void test() throws IOException {
//        nammaYathriService.getStarredPlaces();
//        nammaYathriService.createStarredPlace("1.1", "1.2", "Mansion");
//        nammaYathriService.deleteStarredPlace("ba5a3d0e-2250-418a-8981-063fa092a92d");
//        nammaYathriService.deleteStarredPlace("ba5a3d0e-2250-418a-8981-063fa092a92d");
//        nammaYathriService.generateEstimate("48.8960", "78.8960", "-78.8960", "-74.8960");
//        nammaYathriService.findNearByRide("48.8960", "78.8960", "-78.8960", "-74.8960", "auto");
//        nammaYathriService.findNearByRide("48.8960", "78.8960", "-78.8960", "-74.8960", "manual");
//        nammaYathriService.bookRide("34a55cd6-2d85-42bc-9c7b-67db3cf08461", "3acd51b6-4ff7-40e9-8b0f-4d3f296b51db");
//        nammaYathriService.cancelBooking("ccbef961-d1a9-4424-a412-06ff7f7104f2", "Sorry, i have a bf");
//        nammaYathriService.rateDriver("70d8244d-7ef1-434c-96c9-ba1820fc199", "3");

        JsonElement response = nammaYathriService.getStarredPlaces();

        for (JsonElement element : response.getAsJsonArray()) {
            JsonObject jsonObject = element.getAsJsonObject();
            System.out.println(jsonObject.get("name"));
        }
    }

    @PostMapping("/message/send")
    public ResponseEntity<Object> sendMessage(@RequestBody SendMessageRequestDto sendMessageRequestDto) throws Exception {
        try {
            Boolean isMessageSent = messageService.sendTextMessage(sendMessageRequestDto);
            return WorkflowServiceResponseDto.transformResponse("Random Message posted successfully", HttpStatus.OK, isMessageSent, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return WorkflowServiceResponseDto.transformResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null);
        }

    }

    @PostMapping("/message/list/send")
    public ResponseEntity<Object> sendListMessage(@RequestBody SendListMessageRequestDto sendListMessageRequestDto) throws Exception {
        try {
            Boolean isMessageSent = messageService.sendListMessage(sendListMessageRequestDto);
            return WorkflowServiceResponseDto.transformResponse("Random Message posted successfully", HttpStatus.OK, isMessageSent, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return WorkflowServiceResponseDto.transformResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null);
        }

    }

    @PostMapping("/message/attachment/send")
    public ResponseEntity<Object> sendMessageWithAttachment(@RequestBody SendAttachmentMessageDto sendAttachmentMessageDto) {
        try {
            Boolean isMessageSent = messageService.sendAttachment(sendAttachmentMessageDto);
            return WorkflowServiceResponseDto.transformResponse("Message with attachment posted successfully", HttpStatus.OK, isMessageSent, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return WorkflowServiceResponseDto.transformResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null);
        }

    }

    @PostMapping("/message/list/generate")
    public ResponseEntity<Object> generateListMessage(@RequestBody GenerateListMessageDto generateListMessageDto) {
        try {
            ListMessage listMessage = messageService.generateListMessage(generateListMessageDto.getListMessageData(), generateListMessageDto.getListData(), generateListMessageDto.getMessageId());
            return WorkflowServiceResponseDto.transformResponse("Message with attachment posted successfully", HttpStatus.OK, listMessage, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return WorkflowServiceResponseDto.transformResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null);
        }
    }

    @PostMapping("/message/quick-reply/send")
    public ResponseEntity<Object> sendQuickReplyMessage(@RequestBody SendQuickReplyMessageDto sendQuickReplyMessageDto) {
        try {
            Boolean isMessageSent = messageService.sendQuickReplyMessage(sendQuickReplyMessageDto);
            return WorkflowServiceResponseDto.transformResponse("Quick reply message posted successfully", HttpStatus.OK, isMessageSent, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return WorkflowServiceResponseDto.transformResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null);
        }

    }

    @PostMapping("/message/webhook")
    public ResponseEntity<Object> handleWebhookEvent(@RequestBody WebhookEventRequestDto webhookEventRequestDto) throws Exception {
        try {
            Boolean isMessageSent = messageServiceWebhookHandler.handleWebhookEvent(webhookEventRequestDto);

            return WorkflowServiceResponseDto.transformResponse("Webhook event handled successfully", HttpStatus.OK, isMessageSent, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return WorkflowServiceResponseDto.transformResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null);
        }

    }

}
