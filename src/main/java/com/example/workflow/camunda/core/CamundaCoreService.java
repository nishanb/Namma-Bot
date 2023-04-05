package com.example.workflow.camunda.core;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CamundaCoreService {

    @Value("${camunda.bpm.admin-user.id}")
    private String camundaUserName;

    @Autowired
    public RuntimeService runtimeService;

    @Autowired
    public TaskService taskService;

    public ProcessInstance startProcessInstance(String processDefinitionId, String businessKey){
        return runtimeService.startProcessInstanceById(processDefinitionId, businessKey);
    }

    public Task getTasksByBusinessKey(String businessKey, String processDefinitionId){
        Task task = taskService.createTaskQuery()
                .processDefinitionId(processDefinitionId)
                .taskAssignee(camundaUserName)
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        return task;
    }

    public void completeUserTaskByTaskId(Task task, Map<String, Object> variables){
        taskService.complete(task.getId(), variables);
    }

    public void createMessageCorrelation(String businessKey, String messageName, Map<String, Object> variables){
        runtimeService.correlateMessage(messageName, businessKey, variables);
    }

}
