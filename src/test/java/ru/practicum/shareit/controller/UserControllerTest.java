package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponesDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequestDto userRequestDto;
    private UserResponesDto userResponseDto;
    private UserUpdateDto userUpdateDto;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto();
        userRequestDto.setName("Test User");
        userRequestDto.setEmail("test@example.com");

        userResponseDto = new UserResponesDto();
        userResponseDto.setId(1L);
        userResponseDto.setName("Test User");
        userResponseDto.setEmail("test@example.com");

        userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("Updated User");
        userUpdateDto.setEmail("updated@example.com");
    }

    @Test
    void addUser_ShouldReturnCreatedUser() throws Exception {
        when(userService.addUser(any(UserRequestDto.class))).thenReturn(userResponseDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void addUser_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        userRequestDto.setEmail("invalid-email");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() throws Exception {
        List<UserResponesDto> users = Arrays.asList(userResponseDto);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test User"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userResponseDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        UserResponesDto updatedUser = new UserResponesDto();
        updatedUser.setId(1L);
        updatedUser.setName("Updated User");
        updatedUser.setEmail("updated@example.com");

        when(userService.updateUser(eq(1L), any(UserUpdateDto.class))).thenReturn(updatedUser);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated User"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void updateUser_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        userUpdateDto.setEmail("invalid-email");

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void addUser_ShouldReturnBadRequest_WhenNameIsEmpty() throws Exception {
        userRequestDto.setName("");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_ShouldReturnBadRequest_WhenEmailIsEmpty() throws Exception {
        userRequestDto.setEmail("");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_ShouldReturnBadRequest_WhenNameIsNull() throws Exception {
        userRequestDto.setName(null);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_ShouldReturnBadRequest_WhenEmailIsNull() throws Exception {
        userRequestDto.setEmail(null);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }
}
