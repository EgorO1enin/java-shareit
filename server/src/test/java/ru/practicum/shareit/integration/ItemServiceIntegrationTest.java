package ru.practicum.shareit.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponesDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ItemServiceIntegrationTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("addItem: должен добавлять новую вещь для пользователя")
    void addItem_shouldAddNewItemForUser() {
        var user = userService.addUser(new UserRequestDto("User1", "user1@email.com"));
        var itemDto = new ItemRequestDto("Отвертка", "Крестовая отвертка", true, null);
        ItemResponesDto item = itemService.addItem(user.getId(), itemDto);
        assertThat(item).isNotNull();
        assertThat(item.getId()).isNotNull();
        assertThat(item.getName()).isEqualTo(itemDto.getName());
        assertThat(item.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(item.getAvailable()).isEqualTo(itemDto.getAvailable());
    }

    @Test
    @DisplayName("updateItem: должен обновлять существующую вещь пользователя")
    void updateItem_shouldUpdateExistingItemForUser() {
        var user = userService.addUser(new UserRequestDto("User2", "user2@email.com"));
        var itemDto = new ItemRequestDto("Молоток", "Стальной молоток", true, null);
        var item = itemService.addItem(user.getId(), itemDto);
        var updateDto = new ItemRequestDto("Молоток обновленный", "Молоток с резиновой ручкой", false, null);
        var updated = itemService.updateItem(user.getId(), item.getId(), updateDto);
        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(item.getId());
        assertThat(updated.getName()).isEqualTo(updateDto.getName());
        assertThat(updated.getDescription()).isEqualTo(updateDto.getDescription());
        assertThat(updated.getAvailable()).isEqualTo(updateDto.getAvailable());
    }

    @Test
    @DisplayName("getItem: должен возвращать вещь по id для пользователя")
    void getItem_shouldReturnItemByIdForUser() {
        var user = userService.addUser(new UserRequestDto("User3", "user3@email.com"));
        var itemDto = new ItemRequestDto("Пассатижи", "Пассатижи с изоляцией", true, null);
        var item = itemService.addItem(user.getId(), itemDto);
        var found = itemService.getItem(user.getId(), item.getId());
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(item.getId());
        assertThat(found.getName()).isEqualTo(itemDto.getName());
        assertThat(found.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(found.getAvailable()).isEqualTo(itemDto.getAvailable());
    }

    @Test
    @DisplayName("getUserItems: должен возвращать все вещи пользователя")
    void getUserItems_shouldReturnAllItemsForUser() {
        var user = userService.addUser(new UserRequestDto("User4", "user4@email.com"));
        var item1 = itemService.addItem(user.getId(), new ItemRequestDto("Дрель", "Дрель ударная", true, null));
        var item2 = itemService.addItem(user.getId(), new ItemRequestDto("Пила", "Пила ручная", false, null));
        var items = itemService.getUserItems(user.getId());
        assertThat(items).isNotNull();
        assertThat(items).hasSizeGreaterThanOrEqualTo(2);
        assertThat(items.stream().map(ItemResponesDto::getId)).contains(item1.getId(), item2.getId());
    }

    @Test
    @DisplayName("searchItems: должен находить вещи по тексту поиска")
    void searchItems_shouldFindItemsByText() {
        var user = userService.addUser(new UserRequestDto("User5", "user5@email.com"));
        itemService.addItem(user.getId(), new ItemRequestDto("Лобзик", "Электрический лобзик", true, null));
        itemService.addItem(user.getId(), new ItemRequestDto("Отвертка", "Крестовая отвертка", true, null));
        var found = itemService.searchItems("лобзик", 0, 10);
        assertThat(found).isNotNull();
        assertThat(found.stream().anyMatch(i -> i.getName().toLowerCase().contains("лобзик"))).isTrue();
    }
}