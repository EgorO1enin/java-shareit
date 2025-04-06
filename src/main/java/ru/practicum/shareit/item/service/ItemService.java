package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponesDto;

import java.util.List;

@Service
public interface ItemService {
    ItemResponesDto addItem(Long userId, ItemRequestDto itemRequestModel);

    ItemResponesDto getItemById(Long id);

    ItemResponesDto updateItem(Long userId, Long itemId, ItemRequestDto itemRequestModel);

    List<ItemResponesDto> getAllItemsOfUser(Long userId);

    List<ItemResponesDto> searchItems(String text);
}
