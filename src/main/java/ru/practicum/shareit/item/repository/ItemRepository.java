package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final Map<Long, Item> items = new HashMap<>(); // Хранилище вещей
    private Long itemIdCounter = 1L;

    public Item addItem(Item item) {
        item.setId(itemIdCounter++);
        items.put(item.getId(), item);
        return item;
    }

    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items.values());
    }

    public Item getItemById(Long id) {
        return items.get(id);
    }

    public List<Item> getItemsOfUser(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .toList();
    }

    public List<Item> searchItems(String searchText) {
        List<Item> searchedItems = new ArrayList<>();
        for (Item item : items.values()) {
            // Проверяем, что вещь доступна для аренды
            if (item.isAvailable()) {
                // Проверяем, содержит ли название или описание искомый текст
                boolean matchesName = item.getName().toLowerCase().contains(searchText.toLowerCase());
                boolean matchesDescription = item.getDescription().toLowerCase().contains(searchText.toLowerCase());

                if (matchesName || matchesDescription) {
                    searchedItems.add(item);
                }
            }
        }

        return searchedItems;
    }
}
