package br.com.jtech.tasklist.controller;

import br.com.jtech.tasklist.dto.TasklistRequest;
import br.com.jtech.tasklist.entity.Tasklist;
import br.com.jtech.tasklist.entity.User;
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
class TasklistControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TasklistRepository tasklistRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private String authToken;
    private Tasklist testTasklist;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void shouldGetTasklists() throws Exception {
        mockMvc.perform(get("/api/v1/tasklists")
                .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Test List"))
                .andExpect(jsonPath("$[0].description").value("Test Description"))
                .andExpect(jsonPath("$[0].color").value("#1976D2"));
    }

    @Test
    void shouldCreateTasklist() throws Exception {
        TasklistRequest request = new TasklistRequest();
        request.setName("New List");
        request.setDescription("New Description");
        request.setColor("#FF5722");

        mockMvc.perform(post("/api/v1/tasklists")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New List"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.color").value("#FF5722"))
                .andExpect(jsonPath("$.taskCount").value(0))
                .andExpect(jsonPath("$.completedTaskCount").value(0));
    }

    @Test
    void shouldUpdateTasklist() throws Exception {
        TasklistRequest request = new TasklistRequest();
        request.setName("Updated List");
        request.setDescription("Updated Description");
        request.setColor("#4CAF50");

        mockMvc.perform(put("/api/v1/tasklists/" + testTasklist.getId())
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated List"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.color").value("#4CAF50"));
    }

    @Test
    void shouldDeleteTasklist() throws Exception {
        mockMvc.perform(delete("/api/v1/tasklists/" + testTasklist.getId())
                .header("Authorization", authToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/tasklists")
                .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldFailWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/tasklists"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldFailToAccessOtherUserTasklist() throws Exception {
        User otherUser = User.builder()
                .name("Other User")
                .email("other@example.com")
                .password(passwordEncoder.encode("password123"))
                .build();
        
        otherUser = userRepository.save(otherUser);
        String otherToken = "Bearer " + jwtService.generateToken(otherUser.getId().toString());

        TasklistRequest request = new TasklistRequest();
        request.setName("Updated List");
        request.setDescription("Updated Description");
        request.setColor("#4CAF50");

        mockMvc.perform(put("/api/v1/tasklists/" + testTasklist.getId())
                .header("Authorization", otherToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFailWithDuplicateName() throws Exception {
        TasklistRequest request = new TasklistRequest();
        request.setName("Test List"); // Nome já existe
        request.setDescription("Another Description");

        mockMvc.perform(post("/api/v1/tasklists")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Tasklist name already exists"));
    }
}