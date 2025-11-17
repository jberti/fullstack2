package br.com.jtech.tasklist.controller;

import br.com.jtech.tasklist.dto.TaskRequest;
import br.com.jtech.tasklist.entity.Task;
import br.com.jtech.tasklist.entity.Tasklist;
import br.com.jtech.tasklist.entity.User;
import br.com.jtech.tasklist.repository.TaskRepository;
import br.com.jtech.tasklist.repository.TasklistRepository;
import br.com.jtech.tasklist.repository.UserRepository;
import br.com.jtech.tasklist.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TasklistRepository tasklistRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private String authToken;
    private Tasklist testTasklist;
    private Task testTask;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        tasklistRepository.deleteAll();
        userRepository.deleteAll();
        
        testUser = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password(passwordEncoder.encode("password123"))
                .build();
        
        testUser = userRepository.save(testUser);
        authToken = "Bearer " + jwtService.generateToken(testUser.getId().toString());

        testTasklist = Tasklist.builder()
                .name("Test List")
                .description("Test Description")
                .color("#1976D2")
                .user(testUser)
                .build();
        
        testTasklist = tasklistRepository.save(testTasklist);

        testTask = Task.builder()
                .title("Test Task")
                .description("Test Description")
                .completed(false)
                .user(testUser)
                .tasklist(testTasklist)
                .build();
        
        testTask = taskRepository.save(testTask);
    }

    @Test
    void shouldGetAllUserTasks() throws Exception {
        mockMvc.perform(get("/api/v1/tasks")
                .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[0].description").value("Test Description"))
                .andExpect(jsonPath("$[0].completed").value(false))
                .andExpect(jsonPath("$[0].tasklistName").value("Test List"));
    }

    @Test
    void shouldGetTasksByTasklist() throws Exception {
        mockMvc.perform(get("/api/v1/tasklists/" + testTasklist.getId() + "/tasks")
                .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }

    @Test
    void shouldCreateTask() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("New Task");
        request.setDescription("New Description");
        request.setCompleted(false);
        request.setTasklistId(testTasklist.getId());

        mockMvc.perform(post("/api/v1/tasks")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.tasklistId").value(testTasklist.getId().toString()));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("Updated Task");
        request.setDescription("Updated Description");
        request.setCompleted(true);

        mockMvc.perform(put("/api/v1/tasks/" + testTask.getId())
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/" + testTask.getId())
                .header("Authorization", authToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/tasks")
                .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldFailToCreateTaskWithoutTasklist() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("New Task");
        request.setDescription("New Description");
        // tasklistId não definido

        mockMvc.perform(post("/api/v1/tasks")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Tasklist not found"));
    }

    @Test
    void shouldFailToAccessOtherUserTask() throws Exception {
        User otherUser = User.builder()
                .name("Other User")
                .email("other@example.com")
                .password(passwordEncoder.encode("password123"))
                .build();
        
        otherUser = userRepository.save(otherUser);
        String otherToken = "Bearer " + jwtService.generateToken(otherUser.getId().toString());

        mockMvc.perform(get("/api/v1/tasks/" + testTask.getId())
                .header("Authorization", otherToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFailWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isUnauthorized());
    }
}