package com.example.workflow.camunda.service.booking;

import com.example.workflow.config.MessageTemplate;
import com.example.workflow.dto.ListMessageDto;
import com.example.workflow.dto.SendListMessageRequestDto;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.GlobalButtons;
import com.example.workflow.models.gupshup.ListMessageItem;
import com.example.workflow.models.gupshup.ListMessageItemOption;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.TemplateService;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.spin.json.SpinJsonNode;
import org.camunda.spin.plugin.variable.value.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ShareRideRating implements JavaDelegate {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    TemplateService templateService;

    private final Logger log = Logger.getLogger(ShareRideRating.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("<<-- Share ride rating is called");
        try {

            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElseGet(null);

            // Find driver name to ask for rating
            String chosenDriverId = (String) execution.getVariable("chosen_driver_id");

            JsonValue persistedRides = execution.getVariableTyped("rides_to_persist");
            SpinJsonNode selectedDriverDetails = persistedRides.getValue().prop(chosenDriverId);
            String driverName = (String) selectedDriverDetails.prop("driver_name").value();

            ListMessageDto listMessageDto = new ListMessageDto();
            listMessageDto.setTitle(templateService.format(MessageTemplate.RIDE_SHARE_RIDE_RATING_TITLE, user.getPreferredLanguage()));
            listMessageDto.setBody(templateService.format(MessageTemplate.RIDE_SHARE_RIDE_RATING_DESC, user.getPreferredLanguage(), new ArrayList<>(Collections.singletonList(driverName))));

            // Set Global button
            List<GlobalButtons> globalButtonsList = new ArrayList<>(List.of(new GlobalButtons("text", templateService.format(MessageTemplate.OPTION_BUTTON_CHOOSE_FROM_HERE, user.getPreferredLanguage()))));
            listMessageDto.setGlobalButtons(globalButtonsList);

            // List Group
            List<ListMessageItem> listMessageGroup = new ArrayList<>();

            //Other section listGREET_OTHER_SUB_HEADER
            ListMessageItem ratingsOption = new ListMessageItem(templateService.format(MessageTemplate.GREET_OTHER_SUB_HEADER, user.getPreferredLanguage()));
            ratingsOption.setOptions(Arrays.asList(
                    new ListMessageItemOption(templateService.format(MessageTemplate.RIDE_SHARE_RIDE_RATING_OP_5_START_TITLE, user.getPreferredLanguage()), templateService.format(MessageTemplate.RIDE_SHARE_RIDE_RATING_OP_5_START_DESC, user.getPreferredLanguage()), "5"),
                    new ListMessageItemOption(templateService.format(MessageTemplate.RIDE_SHARE_RIDE_RATING_OP_4_START_TITLE, user.getPreferredLanguage()), templateService.format(MessageTemplate.RIDE_SHARE_RIDE_RATING_OP_4_START_DESC, user.getPreferredLanguage()), "4"),
                    new ListMessageItemOption(templateService.format(MessageTemplate.RIDE_SHARE_RIDE_RATING_OP_3_START_TITLE, user.getPreferredLanguage()), templateService.format(MessageTemplate.RIDE_SHARE_RIDE_RATING_OP_3_START_DESC, user.getPreferredLanguage()), "3"),
                    new ListMessageItemOption(templateService.format(MessageTemplate.RIDE_SHARE_RIDE_RATING_OP_2_START_TITLE, user.getPreferredLanguage()), templateService.format(MessageTemplate.RIDE_SHARE_RIDE_RATING_OP_2_START_DESC, user.getPreferredLanguage()), "2"),
                    new ListMessageItemOption(templateService.format(MessageTemplate.RIDE_SHARE_RIDE_RATING_OP_1_START_TITLE, user.getPreferredLanguage()), templateService.format(MessageTemplate.RIDE_SHARE_RIDE_RATING_OP_1_START_DESC, user.getPreferredLanguage()), "1")
            ));

            // Add others section to group
            listMessageGroup.add(ratingsOption);

            // set list-message group to main message
            listMessageDto.setItems(listMessageGroup);

            messageService.sendListMessage(new SendListMessageRequestDto(user.getPhoneNumber(), messageService.generateListMessage(listMessageDto)));

        } catch (Exception e) {
            log.warning("ShareRideRating: Exception occured......");
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
