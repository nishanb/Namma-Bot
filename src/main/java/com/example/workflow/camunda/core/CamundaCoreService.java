package com.example.workflow.camunda.core;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CamundaCoreService {

    @Value("${camunda.bpm.admin-user.id}")
    private String camundaUserName;

    @Autowired
    public RuntimeService runtimeService;

    @Autowired
    public TaskService taskService;

    @Autowired
    public RepositoryService repositoryService;

    public ProcessInstance startProcessInstance(String processDefinitionId, String businessKey) {
        return runtimeService.startProcessInstanceById(processDefinitionId, businessKey);
    }

    public ProcessInstance startProcessInstanceByName(String processDefinitionName, String businessKey) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionName(processDefinitionName)
                .latestVersion()
                .singleResult();

        if (processDefinition == null) {
            throw new RuntimeException("Process definition not found for name: " + processDefinitionName);
        }

        return runtimeService.startProcessInstanceById(processDefinition.getId(), businessKey);
    }


    public Task getTasksByBusinessKey(String businessKey, String processDefinitionId) {
        System.out.println("businesskey >> " + businessKey + " processid >> " + processDefinitionId);
        Task task = taskService.createTaskQuery()
                .processDefinitionId(processDefinitionId)
                .taskAssignee(camundaUserName)
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        return task;
    }

    public void completeUserTaskByTaskId(Task task, Map<String, Object> variables) {
        taskService.complete(task.getId(), variables);
    }

    public void createMessageCorrelation(String businessKey, String messageName, Map<String, Object> variables) {
        runtimeService.correlateMessage(messageName, businessKey, variables);
    }

    public String getProcessDefinitionNameByProcessDefinitionId(String processDefinitionId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();

        if (processDefinition == null) {
            throw new RuntimeException("Process definition not found for ID: " + processDefinitionId);
        }
        return processDefinition.getName();
    }
    public Task getTaskByProcessDefinitionAndBusinessKey(String processInstanceId, String businessKey) {
        ActivityInstance activityInstance = runtimeService.getActivityInstance(processInstanceId);
        List<ActivityInstance> activeInstances = List.of(activityInstance.getChildActivityInstances());

        if (activeInstances.size() > 0) {
            ActivityInstance currentActivityInstance = activeInstances.get(0);
            return getTasksByBusinessKey(businessKey, activityInstance.getProcessDefinitionId());
        }
        return null;
    }
    public ActivityInstance getActivityInstance(String processInstanceId){
        return runtimeService.getActivityInstance(processInstanceId);
    }
}