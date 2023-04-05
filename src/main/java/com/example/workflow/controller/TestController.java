package com.example.workflow.controller;

import com.example.workflow.service.MessageService;
import com.example.workflow.service.MessageServiceWebhookHandler;
import com.example.workflow.service.NammaYathriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import camundajar.impl.com.google.gson.JsonElement;
import camundajar.impl.com.google.gson.JsonObject;
import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.service.NammaYathriService;
import com.example.workflow.service.UserService;
//import model.User;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    @Autowired
    CamundaCoreService camundaCoreService;

    @Value("${camunda.process-definition-id.booking-flow}")
    String processId;

    @GetMapping
    public void test() throws IOException {

        /* Test Backend
        nammaYathriService.getStarredPlaces();
        nammaYathriService.createStarredPlace("1.1", "1.2", "Mansion");
        nammaYathriService.deleteStarredPlace("ba5a3d0e-2250-418a-8981-063fa092a92d");
        nammaYathriService.deleteStarredPlace("ba5a3d0e-2250-418a-8981-063fa092a92d");
        nammaYathriService.generateEstimate("48.8960", "78.8960", "-78.8960", "-74.8960");
        nammaYathriService.findNearByRide("48.8960", "78.8960", "-78.8960", "-74.8960", "auto");
        nammaYathriService.findNearByRide("48.8960", "78.8960", "-78.8960", "-74.8960", "manual");
        nammaYathriService.bookRide("34a55cd6-2d85-42bc-9c7b-67db3cf08461", "3acd51b6-4ff7-40e9-8b0f-4d3f296b51db");
        nammaYathriService.cancelBooking("ccbef961-d1a9-4424-a412-06ff7f7104f2", "Sorry, i have a bf");
        nammaYathriService.rateDriver("70d8244d-7ef1-434c-96c9-ba1820fc199", "3");

        JsonElement response = nammaYathriService.getStarredPlaces("7892693018");

        for (JsonElement element : response.getAsJsonArray()) {
            JsonObject jsonObject = element.getAsJsonObject();
            System.out.println(jsonObject.get("name"));
       }
       */

        // TEST user collection
//        User user = userService.createUser(new User("jhdsajh1u9u4201", "7892693018"));
//        System.out.println(user.toString());

//        Optional<User> user = null;
//        user = userService.findUserByPhoneNumber("7892693018");
//        System.out.println(user.get().getId());

//        user = userService.findUserByProcessInstanceId("dsjhdsajh1u9u4201");
//        System.out.println(user.get().toString());

//        userService.updateProcessInstanceIdByPhoneNumber("7892693018", "nishan");
//        userService.updateProcessInstanceIdByUserId("64296a988c39bf662dacc064", "dora");

//            userService.updateUserLanguageByPhoneNumber("7892693018", "kannada");

        Task task = camundaCoreService.getTasksByBusinessKey("testUserComplete1",processId);
        Map<String, Object> variables = new HashMap<>();
        variables.put("booking_type",2);
        camundaCoreService.completeUserTaskByTaskId(task,variables);

    }
//
//    @PostMapping("/message/send")
//    public ResponseEntity<Object> sendMessage(@RequestBody SendMessageRequestDto sendMessageRequestDto) throws Exception {
//        try {
//            Boolean isMessageSent = messageService.sendTextMessage(sendMessageRequestDto);
//            return WorkflowServiceResponseDto.transformResponse("Random Message posted successfully", HttpStatus.OK, isMessageSent, null);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return WorkflowServiceResponseDto.transformResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null);
//        }
//
//    }
//
//    @PostMapping("/message/list/send")
//    public ResponseEntity<Object> sendListMessage(@RequestBody SendListMessageRequestDto sendListMessageRequestDto) throws Exception {
//        try {
//            Boolean isMessageSent = messageService.sendListMessage(sendListMessageRequestDto);
//            return WorkflowServiceResponseDto.transformResponse("Random Message posted successfully", HttpStatus.OK, isMessageSent, null);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return WorkflowServiceResponseDto.transformResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null);
//        }
//
//    }
//
//    @PostMapping("/message/attachment/send")
//    public ResponseEntity<Object> sendMessageWithAttachment(@RequestBody SendAttachmentMessageDto sendAttachmentMessageDto) {
//        try {
//            Boolean isMessageSent = messageService.sendAttachment(sendAttachmentMessageDto);
//            return WorkflowServiceResponseDto.transformResponse("Message with attachment posted successfully", HttpStatus.OK, isMessageSent, null);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return WorkflowServiceResponseDto.transformResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null);
//        }
//
//    }
//
//    @PostMapping("/message/list/generate")
//    public ResponseEntity<Object> generateListMessage(@RequestBody GenerateListMessageDto generateListMessageDto) {
//        try {
//            ListMessage listMessage = messageService.generateListMessage(generateListMessageDto.getListMessageData(), generateListMessageDto.getListData(), generateListMessageDto.getMessageId());
//            return WorkflowServiceResponseDto.transformResponse("Message with attachment posted successfully", HttpStatus.OK, listMessage, null);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return WorkflowServiceResponseDto.transformResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null);
//        }
//    }
//
//    @PostMapping("/message/quick-reply/send")
//    public ResponseEntity<Object> sendQuickReplyMessage(@RequestBody SendQuickReplyMessageDto sendQuickReplyMessageDto) {
//        try {
//            Boolean isMessageSent = messageService.sendQuickReplyMessage(sendQuickReplyMessageDto);
//            return WorkflowServiceResponseDto.transformResponse("Quick reply message posted successfully", HttpStatus.OK, isMessageSent, null);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return WorkflowServiceResponseDto.transformResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null);
//        }
//
//    }
}
