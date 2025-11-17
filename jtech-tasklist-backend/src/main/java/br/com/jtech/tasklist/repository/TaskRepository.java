package br.com.jtech.tasklist.repository;

import br.com.jtech.tasklist.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    
    List<Task> findByUserIdOrderByCreatedAtDesc(UUID userId);
    
    List<Task> findByTasklistIdOrderByCreatedAtDesc(UUID tasklistId);
    
    Optional<Task> findByIdAndUserId(UUID id, UUID userId);
    
    Long countByTasklistIdAndCompleted(UUID tasklistId, Boolean completed);
}