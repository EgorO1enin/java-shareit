package ru.practicum.shareit.request;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;

@Tag(name = "Запросы на вещи", description = "Управление запросами на добавление вещей")
@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @Operation(summary = "Создать запрос на вещь")
    @PostMapping
    public ResponseEntity<Object> createRequest(
            @Parameter(description = "ID пользователя", required = true) @RequestHeader("X-Sharer-User-Id") Long userId,
            @Parameter(description = "Данные запроса", required = true) @RequestBody @Valid ItemRequestRequestDto requestDto) {
        log.info("Create item request {}, userId={}", requestDto, userId);
        return itemRequestClient.createRequest(userId, requestDto);
    }

    @Operation(summary = "Получить свои запросы")
    @GetMapping
    public ResponseEntity<Object> getUserRequests(
            @Parameter(description = "ID пользователя", required = true) @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get item requests for user {}", userId);
        return itemRequestClient.getUserRequests(userId);
    }

    @Operation(summary = "Получить запросы других пользователей")
    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(
            @Parameter(description = "ID пользователя", required = true) @RequestHeader("X-Sharer-User-Id") Long userId,
            @Parameter(description = "Номер первого элемента для пагинации", example = "0") @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Parameter(description = "Количество элементов для пагинации", example = "10") @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get all item requests for user {}, from={}, size={}", userId, from, size);
        return itemRequestClient.getAllRequests(userId, from, size);
    }

    @Operation(summary = "Получить запрос по ID")
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(
            @Parameter(description = "ID пользователя", required = true) @RequestHeader("X-Sharer-User-Id") Long userId,
            @Parameter(description = "ID запроса", required = true) @PathVariable Long requestId) {
        log.info("Get item request {} for user {}", requestId, userId);
        return itemRequestClient.getRequestById(userId, requestId);
    }
}