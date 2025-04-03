package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemRequestModel;
import ru.practicum.shareit.item.dto.ItemResponesModel;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // Добавление новой вещи
    @PostMapping
    public ItemResponesModel addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @Valid
                                     @RequestBody ItemRequestModel itemDto) {
        return itemService.addItem(userId, itemDto);
    }


    @PatchMapping("/{itemId}")
    public ItemResponesModel updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable Long itemId,
                                        @RequestBody ItemRequestModel itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    // Просмотр информации о конкретной вещи
    @GetMapping("/{itemId}")
    public ItemResponesModel getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemResponesModel> getAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllItemsOfUser(userId);
    }

    // Поиск доступных вещей
    @GetMapping("/search")
    public List<ItemResponesModel> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }

}