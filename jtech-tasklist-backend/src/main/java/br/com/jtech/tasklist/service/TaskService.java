package br.com.jtech.tasklist.service;

import br.com.jtech.tasklist.dto.TaskRequest;
import br.com.jtech.tasklist.dto.TaskResponse;
import br.com.jtech.tasklist.entity.Task;
import br.com.jtech.tasklist.entity.Tasklist;
import br.com.jtech.tasklist.entity.User;
import br.com.jtech.tasklist.exception.ResourceNotFoundException;
import br.com.jtech.tasklist.repository.TaskRepository;
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
public class TaskService {

    private final TaskRepository taskRepository;
    private final TasklistRepository tasklistRepository;
    private final UserRepository userRepository;

    public List<TaskResponse> findByUserId(UUID userId) {
        return taskRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> findByTasklistId(UUID tasklistId, UUID userId) {
        // Verificar se a tasklist pertence ao usuário
        tasklistRepository.findByIdAndUserId(tasklistId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Tasklist not found"));

        return taskRepository.findByTasklistIdOrderByCreatedAtDesc(tasklistId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse findByIdAndUserId(UUID id, UUID userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return toResponse(task);
    }

    public TaskResponse create(TaskRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tasklist tasklist = tasklistRepository.findByIdAndUserId(request.getTasklistId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Tasklist not found"));

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(request.getCompleted() != null ? request.getCompleted() : false)
                .user(user)
                .tasklist(tasklist)
                .build();

        Task savedTask = taskRepository.save(task);
        return toResponse(savedTask);
    }

    public TaskResponse update(UUID id, TaskRequest request, UUID userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.getCompleted() != null ? request.getCompleted() : task.getCompleted());

        // Se mudou a tasklist, verificar se a nova pertence ao usuário
        if (request.getTasklistId() != null && !request.getTasklistId().equals(task.getTasklist().getId())) {
            Tasklist newTasklist = tasklistRepository.findByIdAndUserId(request.getTasklistId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Tasklist not found"));
            task.setTasklist(newTasklist);
        }

        Task updatedTask = taskRepository.save(task);
        return toResponse(updatedTask);
    }

    public void delete(UUID id, UUID userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        taskRepository.delete(task);
    }

    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.getCompleted())
                .tasklistId(task.getTasklist().getId())
                .tasklistName(task.getTasklist().getName())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}