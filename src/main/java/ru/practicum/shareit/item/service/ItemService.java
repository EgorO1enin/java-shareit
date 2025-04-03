package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemRequestModel;
import ru.practicum.shareit.item.dto.ItemResponesModel;

import java.util.List;

@Service
public interface ItemService {
    ItemResponesModel addItem(Long userId, ItemRequestModel itemRequestModel);

    List<ItemResponesModel> getItems();

    ItemResponesModel getItemById(Long id);

    ItemResponesModel updateItem(Long UserId, Long itemId, ItemRequestModel itemRequestModel);

    List<ItemResponesModel> getAllItemsOfUser(Long userId);

    List<ItemResponesModel> searchItems(String text);
}
