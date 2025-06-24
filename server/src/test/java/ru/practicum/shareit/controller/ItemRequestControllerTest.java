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
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    @DisplayName("POST /requests — создание запроса на вещь")
    void createRequest_shouldReturnCreatedRequest() throws Exception {
        var request = new ItemRequestRequestDto();
        request.setDescription("Нужна дрель");
        var response = new ItemRequestResponseDto();
        response.setId(1L);
        response.setDescription(request.getDescription());
        response.setCreated(LocalDateTime.now());
        Mockito.when(itemRequestService.createRequest(eq(1L), any(ItemRequestRequestDto.class))).thenReturn(response);
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value(request.getDescription()));
    }

    @Test
    @DisplayName("GET /requests — получение всех запросов пользователя")
    void getUserRequests_shouldReturnListOfRequests() throws Exception {
        var request1 = new ItemRequestResponseDto();
        request1.setId(1L);
        request1.setDescription("Нужна дрель");
        request1.setCreated(LocalDateTime.now());
        var request2 = new ItemRequestResponseDto();
        request2.setId(2L);
        request2.setDescription("Нужен молоток");
        request2.setCreated(LocalDateTime.now());
        Mockito.when(itemRequestService.getUserRequests(1L)).thenReturn(List.of(request1, request2));
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @DisplayName("GET /requests/all — получение всех запросов других пользователей")
    void getAllRequests_shouldReturnListOfOtherRequests() throws Exception {
        var request1 = new ItemRequestResponseDto();
        request1.setId(1L);
        request1.setDescription("Нужна пила");
        request1.setCreated(LocalDateTime.now());
        var request2 = new ItemRequestResponseDto();
        request2.setId(2L);
        request2.setDescription("Нужна рулетка");
        request2.setCreated(LocalDateTime.now());
        Mockito.when(itemRequestService.getAllRequests(eq(1L), eq(0), eq(10))).thenReturn(List.of(request1, request2));
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @DisplayName("GET /requests/{requestId} — получение запроса по id")
    void getRequestById_shouldReturnRequestById() throws Exception {
        var response = new ItemRequestResponseDto();
        response.setId(1L);
        response.setDescription("Нужен степлер");
        response.setCreated(LocalDateTime.now());
        Mockito.when(itemRequestService.getRequestById(1L, 1L)).thenReturn(response);
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Нужен степлер"));
    }
}