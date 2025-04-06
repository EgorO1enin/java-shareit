package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.converter.ItemRequestToItemConverter;
import ru.practicum.shareit.item.converter.ItemToResponesModelConverter;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponesDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRequestToItemConverter itemRequestToItemConverter;
    private final ItemToResponesModelConverter itemToResponesModelConverter;
    private final ItemRepository itemRepository;

    @Override
    public ItemResponesDto addItem(Long userId, ItemRequestDto itemRequestModel) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с id " + userId + " не найден.");
        }
        Item item = itemRequestToItemConverter.convert(itemRequestModel);
        item.setOwner(user);
        return itemToResponesModelConverter.convert(itemRepository.addItem(item));
    }

    @Override
    public ItemResponesDto getItemById(Long id) {
        Item item = itemRepository.getItemById(id);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Вещь с id " + id + " не найдена.");
        }
        return itemToResponesModelConverter.convert(item);
    }

    @Override
    public ItemResponesDto updateItem(Long userId, Long itemId, ItemRequestDto itemRequestModel) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с id " + userId + " не найден.");
        }
        Item updatedItem = itemRepository.getItemById(itemId);
        if (updatedItem == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Вещь с id " + itemId + " не найдена.");
        }
        if (itemRequestModel.getName() != null) {
            updatedItem.setName(itemRequestModel.getName());
        }
        if (itemRequestModel.getDescription() != null) {
            updatedItem.setDescription(itemRequestModel.getDescription());
        }
        if (itemRequestModel.getAvailable() != null) {
            updatedItem.setAvailable(itemRequestModel.getAvailable());
        }
        return itemToResponesModelConverter.convert(itemRepository.updateItem(updatedItem));
    }

    @Override
    public List<ItemResponesDto> getAllItemsOfUser(Long userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с id " + userId + " не найден.");
        }
        return itemRepository.getItemsOfUser(userId).stream()
                .map(itemToResponesModelConverter::convert)
                .toList();
    }

    @Override
    public List<ItemResponesDto> searchItems(String text) {
        if (text == null || text.isEmpty()) {
            return List.of();
        }

        return itemRepository.searchItems(text).stream().map(itemToResponesModelConverter::convert).toList();
    }
}
