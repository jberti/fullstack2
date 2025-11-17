package br.com.jtech.tasklist.service;

import br.com.jtech.tasklist.dto.TasklistRequest;
import br.com.jtech.tasklist.dto.TasklistResponse;
import br.com.jtech.tasklist.entity.Tasklist;
import br.com.jtech.tasklist.entity.User;
import br.com.jtech.tasklist.exception.ResourceNotFoundException;
import br.com.jtech.tasklist.exception.DuplicateResourceException;
import br.com.jtech.tasklist.repository.TasklistRepository;
import br.com.jtech.tasklist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TasklistService {

    private final TasklistRepository tasklistRepository;
    private final UserRepository userRepository;

    public List<TasklistResponse> findByUserId(UUID userId) {
        return tasklistRepository.findByUserIdOrderByCreatedAtAsc(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TasklistResponse findByIdAndUserId(UUID id, UUID userId) {
        Tasklist tasklist = tasklistRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Tasklist not found"));
        return toResponse(tasklist);
    }

    public TasklistResponse create(TasklistRequest request, UUID userId) {
        // Verificar se já existe uma lista com o mesmo nome para o usuário
        if (tasklistRepository.existsByNameAndUserId(request.getName(), userId)) {
            throw new DuplicateResourceException("Tasklist name already exists");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tasklist tasklist = Tasklist.builder()
                .name(request.getName())
                .description(request.getDescription())
                .color(request.getColor())
                .user(user)
                .build();

        Tasklist savedTasklist = tasklistRepository.save(tasklist);
        return toResponse(savedTasklist);
    }

    public TasklistResponse update(UUID id, TasklistRequest request, UUID userId) {
        Tasklist tasklist = tasklistRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Tasklist not found"));

        // Verificar se o novo nome já existe (exceto para a própria lista)
        if (!tasklist.getName().equals(request.getName()) && 
            tasklistRepository.existsByNameAndUserId(request.getName(), userId)) {
            throw new DuplicateResourceException("Tasklist name already exists");
        }

        tasklist.setName(request.getName());
        tasklist.setDescription(request.getDescription());
        tasklist.setColor(request.getColor());

        Tasklist updatedTasklist = tasklistRepository.save(tasklist);
        return toResponse(updatedTasklist);
    }

    public void delete(UUID id, UUID userId) {
        Tasklist tasklist = tasklistRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Tasklist not found"));

        tasklistRepository.delete(tasklist);
    }

    private TasklistResponse toResponse(Tasklist tasklist) {
        return TasklistResponse.builder()
                .id(tasklist.getId())
                .name(tasklist.getName())
                .description(tasklist.getDescription())
                .color(tasklist.getColor())
                .taskCount(tasklist.getTaskCount())
                .completedTaskCount(tasklist.getCompletedTaskCount())
                .createdAt(tasklist.getCreatedAt())
                .updatedAt(tasklist.getUpdatedAt())
                .build();
    }
}