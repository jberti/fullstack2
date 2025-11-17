package br.com.jtech.tasklist.service;

import br.com.jtech.tasklist.dto.TasklistRequest;
import br.com.jtech.tasklist.dto.TasklistResponse;
import br.com.jtech.tasklist.entity.Tasklist;
import br.com.jtech.tasklist.entity.User;
import br.com.jtech.tasklist.exception.DuplicateResourceException;
import br.com.jtech.tasklist.exception.ResourceNotFoundException;
import br.com.jtech.tasklist.repository.TasklistRepository;
import br.com.jtech.tasklist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TasklistServiceTest {

    @Mock
    private TasklistRepository tasklistRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TasklistService tasklistService;

    private User testUser;
    private Tasklist testTasklist;
    private UUID userId;
    private UUID tasklistId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        tasklistId = UUID.randomUUID();

        testUser = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .password("encoded-password")
                .build();

        testTasklist = Tasklist.builder()
                .id(tasklistId)
                .name("Test List")
                .description("Test Description")
                .color("#1976D2")
                .user(testUser)
                .build();
    }

    @Test
    void shouldFindTasklistsByUserId() {
        // Given
        List<Tasklist> tasklists = Arrays.asList(testTasklist);
        when(tasklistRepository.findByUserIdOrderByCreatedAtAsc(userId)).thenReturn(tasklists);

        // When
        List<TasklistResponse> result = tasklistService.findByUserId(userId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test List");
        assertThat(result.get(0).getDescription()).isEqualTo("Test Description");
        assertThat(result.get(0).getColor()).isEqualTo("#1976D2");
    }

    @Test
    void shouldCreateTasklist() {
        // Given
        TasklistRequest request = new TasklistRequest();
        request.setName("New List");
        request.setDescription("New Description");
        request.setColor("#FF5722");

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(tasklistRepository.existsByNameAndUserId("New List", userId)).thenReturn(false);
        when(tasklistRepository.save(any(Tasklist.class))).thenReturn(testTasklist);

        // When
        TasklistResponse result = tasklistService.create(request, userId);

        // Then
        assertThat(result).isNotNull();
        verify(tasklistRepository).save(any(Tasklist.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicateTasklist() {
        // Given
        TasklistRequest request = new TasklistRequest();
        request.setName("Existing List");

        when(tasklistRepository.existsByNameAndUserId("Existing List", userId)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> tasklistService.create(request, userId))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Tasklist name already exists");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        TasklistRequest request = new TasklistRequest();
        request.setName("New List");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> tasklistService.create(request, userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void shouldUpdateTasklist() {
        // Given
        TasklistRequest request = new TasklistRequest();
        request.setName("Updated List");
        request.setDescription("Updated Description");
        request.setColor("#4CAF50");

        when(tasklistRepository.findByIdAndUserId(tasklistId, userId)).thenReturn(Optional.of(testTasklist));
        when(tasklistRepository.existsByNameAndUserId("Updated List", userId)).thenReturn(false);
        when(tasklistRepository.save(any(Tasklist.class))).thenReturn(testTasklist);

        // When
        TasklistResponse result = tasklistService.update(tasklistId, request, userId);

        // Then
        assertThat(result).isNotNull();
        verify(tasklistRepository).save(testTasklist);
    }

    @Test
    void shouldDeleteTasklist() {
        // Given
        when(tasklistRepository.findByIdAndUserId(tasklistId, userId)).thenReturn(Optional.of(testTasklist));

        // When
        tasklistService.delete(tasklistId, userId);

        // Then
        verify(tasklistRepository).delete(testTasklist);
    }

    @Test
    void shouldThrowExceptionWhenTasklistNotFoundForDelete() {
        // Given
        when(tasklistRepository.findByIdAndUserId(tasklistId, userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> tasklistService.delete(tasklistId, userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Tasklist not found");
    }
}