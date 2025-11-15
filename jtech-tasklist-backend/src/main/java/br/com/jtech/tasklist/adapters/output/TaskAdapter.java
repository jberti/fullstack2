/*
*  @(#)TaskAdapter.java
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
package br.com.jtech.tasklist.adapters.output;

import br.com.jtech.tasklist.adapters.output.repositories.TaskRepository;
import br.com.jtech.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.adapters.output.repositories.entities.TaskEntity;
import br.com.jtech.tasklist.adapters.output.repositories.entities.UserEntity;
import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.output.TaskOutputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.jtech.tasklist.application.core.domains.Task.of;

/**
* class TaskAdapter 
* 
* @author jtech
*/
@Component
@RequiredArgsConstructor
public class TaskAdapter implements TaskOutputGateway {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public Task save(Task task) {
        TaskEntity entity = task.toEntity();
        
        if (task.getUserId() != null) {
            UserEntity user = userRepository.findById(UUID.fromString(task.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            entity.setUser(user);
        }
        
        TaskEntity savedEntity = taskRepository.save(entity);
        return of(savedEntity);
    }

    @Override
    public List<Task> findByUserId(String userId) {
        List<TaskEntity> entities = taskRepository.findByUserId(UUID.fromString(userId));
        return entities.stream()
            .map(Task::of)
            .toList();
    }

    @Override
    public Optional<Task> findByIdAndUserId(String id, String userId) {
        return taskRepository.findByIdAndUserId(UUID.fromString(id), UUID.fromString(userId))
            .map(Task::of);
    }

    @Override
    public void delete(String id) {
        taskRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public boolean existsByIdAndUserId(String id, String userId) {
        return taskRepository.existsByIdAndUserId(UUID.fromString(id), UUID.fromString(userId));
    }
}


