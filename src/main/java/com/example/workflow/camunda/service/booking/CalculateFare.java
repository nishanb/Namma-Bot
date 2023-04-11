package com.example.workflow.camunda.service.booking;

import camundajar.impl.com.google.gson.JsonElement;
import camundajar.impl.com.google.gson.JsonObject;
import com.example.workflow.config.MessageTemplate;
import com.example.workflow.dto.SendAttachmentMessageDto;
import com.example.workflow.dto.SendMessageRequestDto;
import com.example.workflow.models.User;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.NammaYathriService;
import com.example.workflow.services.TemplateService;
import com.example.workflow.services.UserService;
import com.example.workflow.utils.Constants;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static com.example.workflow.utils.Constants.ATTACHMENT_TYPES_IMAGE;

@Service
public class CalculateFare implements JavaDelegate {
    private final Logger log = Logger.getLogger(CalculateFare.class.getName());

    @Autowired
    NammaYathriService nammaYathriService;

    @Autowired
    TemplateService templateService;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            // Get estimates from namma yatri side & notify user
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElseGet(null);

            SendAttachmentMessageDto attachmentMessage = new SendAttachmentMessageDto();
            attachmentMessage.setReceiverContactNumber(user.getPhoneNumber());
            attachmentMessage.setAttachmentType(ATTACHMENT_TYPES_IMAGE);
            attachmentMessage.setCaption(templateService.format(MessageTemplate.RIDE_CALCULATE_FARE, user.getPreferredLanguage()));

            // set images based on user language config
            String rateCardImageUrl = switch (user.getPreferredLanguage()) {
                case "kannada" -> Constants.RATE_CARD_KANNADA;
                case "hindi" -> Constants.RATE_CARD_HINDI;
                default -> Constants.RATE_CARD_ENGLISH;
            };

            attachmentMessage.setOriginalUrl(rateCardImageUrl);
            attachmentMessage.setPreviewUrl(rateCardImageUrl);

            // share rate card with calculating estimation message
            messageService.sendAttachment(attachmentMessage);

            // collect pickup & drop lats for backend price estimation api calls
            String destinationLatitude = (String) execution.getVariable("destination_latitude");
            String destinationLongitude = (String) execution.getVariable("destination_longitude");
            String sourceLatitude = (String) execution.getVariable("source_latitude");
            String sourceLongitude = (String) execution.getVariable("source_longitude");

            JsonElement response = nammaYathriService.generateEstimate(sourceLatitude, sourceLongitude, destinationLatitude, destinationLongitude);
            JsonObject estimationData = response.getAsJsonObject().getAsJsonObject("data");

            execution.setVariable("distance_estimation", estimationData.get("distance").getAsString());
            execution.setVariable("time_estimation", estimationData.get("time").getAsString());
            execution.setVariable("price_est_low", estimationData.get("price_est_low").getAsString());
            execution.setVariable("price_est_high", estimationData.get("price_est_high").getAsString());

            log.info("CalculateFare: execute method is called......");
        } catch (Exception e) {
            log.warning("CalculateFare: Exception occurred......" + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
