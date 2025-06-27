package ru.practicum.shareit.item.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponesDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Tag(name = "Управление вещами", description = "API для работы с вещами (предметами) для аренды")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @Operation(
            summary = "Добавить новую вещь",
            description = "Создает новую вещь, доступную для аренды другими пользователями",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вещь успешно добавлена"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные вещи"
                    )
            })
    public ResponseEntity<ItemResponesDto> addItem(
            @RequestHeader("X-Sharer-User-Id")
            @Parameter(description = "ID владельца вещи", required = true, example = "1")
            Long userId,
            @RequestBody
            @Parameter(description = "Данные новой вещи", required = true)
            ItemRequestDto itemDto) {
        return ResponseEntity.ok(itemService.addItem(userId, itemDto));
    }

    @PatchMapping("/{itemId}")
    @Operation(
            summary = "Обновить вещь",
            description = "Обновляет данные существующей вещи. Только владелец может обновлять вещь",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вещь успешно обновлена"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Вещь не найдена или пользователь не является владельцем"
                    )
            })
    public ResponseEntity<ItemResponesDto> updateItem(
            @RequestHeader("X-Sharer-User-Id")
            @Parameter(description = "ID владельца вещи", required = true, example = "1")
            Long userId,
            @PathVariable
            @Parameter(description = "ID вещи для обновления", required = true, example = "1")
            Long itemId,
            @RequestBody
            @Parameter(description = "Обновленные данные вещи", required = true)
            ItemRequestDto itemDto) {
        return ResponseEntity.ok(itemService.updateItem(userId, itemId, itemDto));
    }

    @GetMapping("/{itemId}")
    @Operation(
            summary = "Получить вещь по ID",
            description = "Возвращает полную информацию о вещи по её идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о вещи"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Вещь не найдена"
                    )
            })
    public ResponseEntity<ItemResponesDto> getItem(
            @RequestHeader("X-Sharer-User-Id")
            @Parameter(description = "ID пользователя", required = true, example = "1")
            Long userId,
            @PathVariable
            @Parameter(description = "ID вещи", required = true, example = "1")
            Long itemId) {
        return ResponseEntity.ok(itemService.getItem(userId, itemId));
    }

    @GetMapping
    @Operation(
            summary = "Получить все вещи пользователя",
            description = "Возвращает список всех вещей, принадлежащих указанному пользователю",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список вещей пользователя"
                    )
            })
    public ResponseEntity<List<ItemResponesDto>> getUserItems(
            @RequestHeader("X-Sharer-User-Id")
            @Parameter(description = "ID пользователя", required = true, example = "1")
            Long userId) {
        return ResponseEntity.ok(itemService.getUserItems(userId));
    }

    @GetMapping("/search")
    @Operation(
            summary = "Поиск вещей",
            description = "Осуществляет поиск доступных для аренды вещей по названию или описанию",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список найденных вещей"
                    )
            })
    public ResponseEntity<List<ItemResponesDto>> searchItems(
            @RequestParam
            @Parameter(description = "Текст для поиска", required = true, example = "дрель")
            String text,
            @RequestParam(defaultValue = "0")
            @PositiveOrZero
            @Parameter(description = "Индекс первого элемента")
            int from,
            @RequestParam(defaultValue = "10")
            @Positive
            @Parameter(description = "Количество элементов для отображения")
            int size) {
        return ResponseEntity.ok(itemService.searchItems(text, from, size));
    }

    @PostMapping("/{itemId}/comment")
    @Operation(
            summary = "Добавление комментария к вещи",
            description = "Добавляет комментарий к вещи. Только пользователь, который брал вещь в аренду, может оставить комментарий",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Комментарий успешно добавлен"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Пользователь не брал вещь в аренду"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Вещь или пользователь не найдены"
                    )
            })

    public ResponseEntity<CommentResponseDto> addComment(
            @RequestHeader("X-Sharer-User-Id")
            @Parameter(description = "ID пользователя")
            Long userId,
            @PathVariable
            @Parameter(description = "ID вещи")
            Long itemId,
            @RequestBody
            @Parameter(description = "Данные комментария")
            CommentDto commentDto) {
        return ResponseEntity.ok(itemService.addComment(userId, itemId, commentDto));
    }
}