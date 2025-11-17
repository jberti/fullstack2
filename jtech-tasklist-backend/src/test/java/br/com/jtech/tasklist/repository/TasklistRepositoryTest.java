package br.com.jtech.tasklist.repository;

import br.com.jtech.tasklist.entity.Task;
import br.com.jtech.tasklist.entity.Tasklist;
import br.com.jtech.tasklist.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TasklistRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TasklistRepository tasklistRepository;

    private User testUser;
    private User otherUser;
    private Tasklist testTasklist;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .build();
        
        testUser = entityManager.persistAndFlush(testUser);

        otherUser = User.builder()
                .name("Other User")
                .email("other@example.com")
                .password("password")
                .build();
        
        otherUser = entityManager.persistAndFlush(otherUser);

        testTasklist = Tasklist.builder()
                .name("Test List")
                .description("Test Description")
                .color("#1976D2")
                .user(testUser)
                .build();
        
        testTasklist = entityManager.persistAndFlush(testTasklist);
    }

    @Test
    void shouldFindTasklistsByUserIdOrderedByCreatedAt() {
        // Given
        Tasklist secondTasklist = Tasklist.builder()
                .name("Second List")
                .description("Second Description")
                .color("#FF5722")
                .user(testUser)
                .build();
        
        entityManager.persistAndFlush(secondTasklist);

        // When
        List<Tasklist> result = tasklistRepository.findByUserIdOrderByCreatedAtAsc(testUser.getId());

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Test List");
        assertThat(result.get(1).getName()).isEqualTo("Second List");
    }

    @Test
    void shouldFindTasklistByIdAndUserId() {
        // When
        Optional<Tasklist> result = tasklistRepository.findByIdAndUserId(testTasklist.getId(), testUser.getId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test List");
    }

    @Test
    void shouldNotFindTasklistFromOtherUser() {
        // When
        Optional<Tasklist> result = tasklistRepository.findByIdAndUserId(testTasklist.getId(), otherUser.getId());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldCheckIfTasklistNameExistsForUser() {
        // When
        boolean exists = tasklistRepository.existsByNameAndUserId("Test List", testUser.getId());
        boolean notExists = tasklistRepository.existsByNameAndUserId("Non Existent", testUser.getId());
        boolean notExistsForOtherUser = tasklistRepository.existsByNameAndUserId("Test List", otherUser.getId());

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
        assertThat(notExistsForOtherUser).isFalse();
    }

    @Test
    void shouldCountTasksByTasklistId() {
        // Given
        Task task1 = Task.builder()
                .title("Task 1")
                .description("Description 1")
                .completed(false)
                .user(testUser)
                .tasklist(testTasklist)
                .build();
        
        Task task2 = Task.builder()
                .title("Task 2")
                .description("Description 2")
                .completed(true)
                .user(testUser)
                .tasklist(testTasklist)
                .build();
        
        entityManager.persistAndFlush(task1);
        entityManager.persistAndFlush(task2);

        // When
        Long count = tasklistRepository.countTasksByTasklistId(testTasklist.getId());

        // Then
        assertThat(count).isEqualTo(2L);
    }

    @Test
    void shouldReturnEmptyListForUserWithNoTasklists() {
        // When
        List<Tasklist> result = tasklistRepository.findByUserIdOrderByCreatedAtAsc(otherUser.getId());

        // Then
        assertThat(result).isEmpty();
    }
}