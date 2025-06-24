package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Test
    @DisplayName("POST /users — создание пользователя")
    void addUser_shouldReturnCreatedUser() throws Exception {
        var request = new UserRequestDto("Test User", "test@email.com");
        var response = new UserResponseDto();
        response.setId(1L);
        response.setName(request.getName());
        response.setEmail(request.getEmail());
        Mockito.when(userService.addUser(any(UserRequestDto.class))).thenReturn(response);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.email").value(request.getEmail()));
    }

    @Test
    @DisplayName("GET /users — получение всех пользователей")
    void getAllUsers_shouldReturnListOfUsers() throws Exception {
        var user1 = new UserResponseDto();
        user1.setId(1L); user1.setName("User1"); user1.setEmail("user1@email.com");
        var user2 = new UserResponseDto();
        user2.setId(2L); user2.setName("User2"); user2.setEmail("user2@email.com");
        Mockito.when(userService.getAllUsers()).thenReturn(List.of(user1, user2));
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @DisplayName("GET /users/{userId} — получение пользователя по id")
    void getUser_shouldReturnUserById() throws Exception {
        var user = new UserResponseDto();
        user.setId(1L); user.setName("User1"); user.setEmail("user1@email.com");
        Mockito.when(userService.getUserById(1L)).thenReturn(user);
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("User1"));
    }

    @Test
    @DisplayName("PATCH /users/{userId} — обновление пользователя")
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        var update = new UserUpdateDto();
        update.setName("Updated"); update.setEmail("updated@email.com");
        var response = new UserResponseDto();
        response.setId(1L); response.setName(update.getName()); response.setEmail(update.getEmail());
        Mockito.when(userService.updateUser(eq(1L), any(UserUpdateDto.class))).thenReturn(response);
        mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(update.getName()))
                .andExpect(jsonPath("$.email").value(update.getEmail()));
    }

    @Test
    @DisplayName("DELETE /users/{userId} — удаление пользователя")
    void deleteUser_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
        Mockito.verify(userService).deleteUser(1L);
    }
} 