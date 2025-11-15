/*
*  @(#)TaskUseCase.java
*
*  Copyright (c) J-Tech Solucoes em Informatica.
*  All Rights Reserved.
*
*  This software is the confidential and proprietary information of J-Tech.
*  ("Confidential Information"). You shall not disclose such Confidential
*  Information and shall use it only in accordance with the terms of the
*  license agreement you entered into with J-Tech.
*
*/
package br.com.jtech.tasklist.application.core.usecases;

import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.input.TaskInputGateway;
import br.com.jtech.tasklist.application.ports.output.TaskOutputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

/**
* class TaskUseCase  
* 
* @author jtech
*/
public class TaskUseCase implements TaskInputGateway {

    private final TaskOutputGateway taskOutputGateway;

    public TaskUseCase(TaskOutputGateway taskOutputGateway) {
        this.taskOutputGateway = taskOutputGateway;
    }

    @Override
    public Task create(Task task, String userId) {
        task.setUserId(userId);
        task.setCompleted(task.getCompleted() != null ? task.getCompleted() : false);
        return taskOutputGateway.save(task);
    }

    @Override
    public List<Task> findAllByUserId(String userId) {
        return taskOutputGateway.findByUserId(userId);
    }

    @Override
    public Optional<Task> findByIdAndUserId(String id, String userId) {
        return taskOutputGateway.findByIdAndUserId(id, userId);
    }

    @Override
    public Task update(Task task, String userId) {
        Task existingTask = taskOutputGateway.findByIdAndUserId(task.getId(), userId)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        if (task.getCompleted() != null) {
            existingTask.setCompleted(task.getCompleted());
        }
        
        return taskOutputGateway.save(existingTask);
    }

    @Override
    public void delete(String id, String userId) {
        if (!taskOutputGateway.existsByIdAndUserId(id, userId)) {
            throw new ResourceNotFoundException("Task not found");
        }
        
        taskOutputGateway.delete(id);
    }
}

