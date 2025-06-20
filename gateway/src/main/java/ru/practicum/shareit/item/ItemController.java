package ru.practicum.shareit.item;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Вещи", description = "Управление вещами")
@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @Operation(summary = "Добавить вещь")
    @PostMapping
    public ResponseEntity<Object> addItem(
            @Parameter(description = "ID пользователя", required = true) @RequestHeader("X-Sharer-User-Id") Long userId,
            @Parameter(description = "Данные вещи", required = true) @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Creating item {}, userId={}", itemRequestDto, userId);
        return itemClient.addItem(userId, itemRequestDto);
    }

    @Operation(summary = "Обновить вещь")
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
            @Parameter(description = "ID пользователя", required = true) @RequestHeader("X-Sharer-User-Id") Long userId,
            @Parameter(description = "ID вещи", required = true) @PathVariable Long itemId,
            @Parameter(description = "Данные для обновления вещи", required = true) @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Update item {}: {}, userId={}", itemId, itemRequestDto, userId);
        return itemClient.updateItem(userId, itemId, itemRequestDto);
    }

    @Operation(summary = "Получить вещь по ID")
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(
            @Parameter(description = "ID пользователя", required = true) @RequestHeader("X-Sharer-User-Id") Long userId,
            @Parameter(description = "ID вещи", required = true) @PathVariable Long itemId) {
        log.info("Get item {}, userId={}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @Operation(summary = "Получить все вещи пользователя")
    @GetMapping
    public ResponseEntity<Object> getUserItems(
            @Parameter(description = "ID пользователя", required = true) @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all items for user {}", userId);
        return itemClient.getUserItems(userId);
    }

    @Operation(summary = "Поиск вещей")
    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(
            @Parameter(description = "Текст для поиска", required = true) @RequestParam String text,
            @Parameter(description = "Номер первого элемента для пагинации", example = "0") @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Parameter(description = "Количество элементов для пагинации", example = "10") @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Search items: text={}, from={}, size={}", text, from, size);
        return itemClient.searchItems(text, from, size);
    }

    @Operation(summary = "Добавить комментарий к вещи")
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @Parameter(description = "ID пользователя", required = true) @RequestHeader("X-Sharer-User-Id") Long userId,
            @Parameter(description = "ID вещи", required = true) @PathVariable Long itemId,
            @Parameter(description = "Данные комментария", required = true) @RequestBody Object commentDto) {
        log.info("Add comment to item {}, userId={}", itemId, userId);
        return itemClient.addComment(userId, itemId, commentDto);
    }
} 