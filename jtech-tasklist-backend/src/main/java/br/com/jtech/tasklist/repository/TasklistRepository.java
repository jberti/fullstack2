package br.com.jtech.tasklist.repository;

import br.com.jtech.tasklist.entity.Tasklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TasklistRepository extends JpaRepository<Tasklist, UUID> {
    
    List<Tasklist> findByUserIdOrderByCreatedAtAsc(UUID userId);
    
    Optional<Tasklist> findByIdAndUserId(UUID id, UUID userId);
    
    boolean existsByNameAndUserId(String name, UUID userId);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.tasklist.id = :tasklistId")
    Long countTasksByTasklistId(@Param("tasklistId") UUID tasklistId);
}