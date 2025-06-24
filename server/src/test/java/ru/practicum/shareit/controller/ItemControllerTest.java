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
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponesDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;

    @Test
    @DisplayName("POST /items — создание вещи")
    void addItem_shouldReturnCreatedItem() throws Exception {
        var request = new ItemRequestDto("Дрель", "Мощная дрель", true, null);
        var response = new ItemResponesDto();
        response.setId(1L);
        response.setName(request.getName());
        response.setDescription(request.getDescription());
        response.setAvailable(request.getAvailable());
        Mockito.when(itemService.addItem(eq(1L), any(ItemRequestDto.class))).thenReturn(response);
        mockMvc.perform(post("/items")
                .header("X-Sharer-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(request.getName()));
    }

    @Test
    @DisplayName("PATCH /items/{itemId} — обновление вещи")
    void updateItem_shouldReturnUpdatedItem() throws Exception {
        var update = new ItemRequestDto("Дрель обновленная", "Мощная дрель с аккумулятором", false, null);
        var response = new ItemResponesDto();
        response.setId(1L);
        response.setName(update.getName());
        response.setDescription(update.getDescription());
        response.setAvailable(update.getAvailable());
        Mockito.when(itemService.updateItem(eq(1L), eq(1L), any(ItemRequestDto.class))).thenReturn(response);
        mockMvc.perform(patch("/items/1")
                .header("X-Sharer-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(update.getName()));
    }

    @Test
    @DisplayName("GET /items/{itemId} — получение вещи по id")
    void getItem_shouldReturnItemById() throws Exception {
        var item = new ItemResponesDto();
        item.setId(1L);
        item.setName("Дрель");
        item.setDescription("Мощная дрель");
        item.setAvailable(true);
        Mockito.when(itemService.getItem(1L, 1L)).thenReturn(item);
        mockMvc.perform(get("/items/1")
                .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Дрель"));
    }

    @Test
    @DisplayName("GET /items — получение всех вещей пользователя")
    void getUserItems_shouldReturnListOfItems() throws Exception {
        var item1 = new ItemResponesDto();
        item1.setId(1L); item1.setName("Дрель"); item1.setAvailable(true);
        var item2 = new ItemResponesDto();
        item2.setId(2L); item2.setName("Молоток"); item2.setAvailable(false);
        Mockito.when(itemService.getUserItems(1L)).thenReturn(List.of(item1, item2));
        mockMvc.perform(get("/items")
                .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @DisplayName("GET /items/search — поиск вещей")
    void searchItems_shouldReturnFoundItems() throws Exception {
        var item = new ItemResponesDto();
        item.setId(1L); item.setName("Дрель"); item.setAvailable(true);
        Mockito.when(itemService.searchItems("дрель", 0, 10)).thenReturn(List.of(item));
        mockMvc.perform(get("/items/search")
                .param("text", "дрель")
                .param("from", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Дрель"));
    }

    @Test
    @DisplayName("POST /items/{itemId}/comment — добавление комментария")
    void addComment_shouldReturnCreatedComment() throws Exception {
        var commentRequest = new CommentDto();
        commentRequest.setText("Отличная вещь!");
        var commentResponse = new CommentResponseDto();
        commentResponse.setId(1L);
        commentResponse.setText(commentRequest.getText());
        commentResponse.setAuthorName("User");
        commentResponse.setCreated(LocalDateTime.now());
        Mockito.when(itemService.addComment(eq(1L), eq(1L), any(CommentDto.class))).thenReturn(commentResponse);
        mockMvc.perform(post("/items/1/comment")
                .header("X-Sharer-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.text").value(commentRequest.getText()));
    }
} 