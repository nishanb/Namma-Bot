package com.example.workflow.serviceImpl;

import com.example.workflow.repository.TemplateRepo;
import com.example.workflow.service.MessageData;
import com.example.workflow.model.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Optional;

public class MessageDataImpl implements MessageData {


    @Autowired
    private TemplateRepo templateRepo;


    @Override
    public String formatMessage(String templateId, String language, String[] variables) throws Exception {
        Optional<MessageTemplate> messageData;
        messageData = templateRepo.findById(templateId);
        String message = messageData.get().getMessage(templateId);

        try{
            String[] variable = messageData.get().getVariable();
            if(!messageData.isPresent()) return null;
            MessageTemplate updatedMessage = new MessageTemplate();
            if(variable.length > 1){
                for(int i =0; i< variable.length; i++){
                    message.replace("${variable[i]}", variables[i]);
                }

            } else{
                message.replace("${variable}",variable[0] );
            }

        }catch(Exception e){
            throw new Exception("Message couldn't be found on the database");

        }
        return message;
    }
}
