package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserClient userClient;

    @Test
    void testAddUser() throws Exception {
        UserRequestDto dto = new UserRequestDto("name", "test@test.com");
        when(userClient.addUser(any())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userClient.getAllUsers()).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUser() throws Exception {
        when(userClient.getUser(anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateUser() throws Exception {
        UserUpdateDto dto = new UserUpdateDto("name", "test@test.com");
        when(userClient.updateUser(anyLong(), any())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUser() throws Exception {
        when(userClient.deleteUser(anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }
} 