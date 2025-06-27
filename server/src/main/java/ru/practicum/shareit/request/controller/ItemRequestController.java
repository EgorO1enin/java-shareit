package ru.practicum.shareit.request.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * Контроллер для работы с запросами на добавление вещей
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Tag(name = "Запросы на добавление вещей", description = "API для работы с запросами на добавление вещей")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    @Operation(summary = "Создание нового запроса на добавление вещи")
    public ResponseEntity<ItemRequestResponseDto> createRequest(
            @RequestHeader("X-Sharer-User-Id") @Parameter(description = "ID пользователя") Long userId,
            @RequestBody @Parameter(description = "Данные запроса") ItemRequestRequestDto requestDto) {
        return ResponseEntity.ok(itemRequestService.createRequest(userId, requestDto));
    }

    @GetMapping
    @Operation(summary = "Получение списка своих запросов")
    public ResponseEntity<List<ItemRequestResponseDto>> getUserRequests(
            @RequestHeader("X-Sharer-User-Id") @Parameter(description = "ID пользователя") Long userId) {
        return ResponseEntity.ok(itemRequestService.getUserRequests(userId));
    }

    @GetMapping("/all")
    @Operation(summary = "Получение списка запросов других пользователей")
    public ResponseEntity<List<ItemRequestResponseDto>> getAllRequests(
            @RequestHeader("X-Sharer-User-Id") @Parameter(description = "ID пользователя") Long userId,
            @RequestParam(defaultValue = "0") @Parameter(description = "Индекс первого элемента") int from,
            @RequestParam(defaultValue = "10") @Parameter(description = "Количество элементов для отображения") int size) {
        return ResponseEntity.ok(itemRequestService.getAllRequests(userId, from, size));
    }

    @GetMapping("/{requestId}")
    @Operation(summary = "Получение информации о конкретном запросе")
    public ResponseEntity<ItemRequestResponseDto> getRequestById(
            @RequestHeader("X-Sharer-User-Id") @Parameter(description = "ID пользователя") Long userId,
            @PathVariable @Parameter(description = "ID запроса") Long requestId) {
        return ResponseEntity.ok(itemRequestService.getRequestById(userId, requestId));
    }
}
