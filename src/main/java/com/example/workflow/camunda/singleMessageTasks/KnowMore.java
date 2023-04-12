package com.example.workflow.camunda.singleMessageTasks;

import com.example.workflow.models.User;
import org.springframework.stereotype.Service;

@Service
public class KnowMoreSingleMessageTask implements SingleMessageTask {
    @Override
    public void sendMessage(User user) {

    }
}
