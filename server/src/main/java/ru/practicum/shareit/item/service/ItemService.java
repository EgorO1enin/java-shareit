package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponesDto;

import java.util.List;

public interface ItemService {
    ItemResponesDto addItem(Long userId, ItemRequestDto itemRequestDto);

    ItemResponesDto updateItem(Long userId, Long itemId, ItemRequestDto itemRequestDto);

    ItemResponesDto getItem(Long userId, Long itemId);

    List<ItemResponesDto> getUserItems(Long userId);

    List<ItemResponesDto> searchItems(String text, int from, int size);

    CommentResponseDto addComment(Long userId, Long itemId, CommentDto commentDto);
}
