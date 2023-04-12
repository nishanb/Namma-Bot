package com.example.workflow.camunda.singleMessageTasks;

import com.example.workflow.models.User;

public interface SingleMessageTask {
    public void process(User user) throws Exception;
}
