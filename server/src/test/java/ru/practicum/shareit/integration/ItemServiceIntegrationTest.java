package ru.practicum.shareit.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponesDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ItemServiceIntegrationTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemRequestService itemRequestService;
    @Autowired
    private BookingService bookingService;

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
    @DisplayName("addItem: должен добавлять вещь с requestId")
    void addItem_shouldAddItemWithRequestId() {
        var user = userService.addUser(new UserRequestDto("User1a", "user1a@email.com"));
        var request = itemRequestService.createRequest(user.getId(), new ItemRequestRequestDto("Нужна отвертка"));
        var itemDto = new ItemRequestDto("Отвертка", "Крестовая отвертка", true, request.getId());
        ItemResponesDto item = itemService.addItem(user.getId(), itemDto);
        assertThat(item).isNotNull();
        assertThat(item.getId()).isNotNull();
        assertThat(item.getName()).isEqualTo(itemDto.getName());
        assertThat(item.getRequestId()).isEqualTo(request.getId());
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
    @DisplayName("updateItem: должен обновлять только имя вещи")
    void updateItem_shouldUpdateOnlyName() {
        var user = userService.addUser(new UserRequestDto("User2a", "user2a@email.com"));
        var itemDto = new ItemRequestDto("Молоток", "Стальной молоток", true, null);
        var item = itemService.addItem(user.getId(), itemDto);
        var updateDto = new ItemRequestDto("Молоток обновленный", null, null, null);
        var updated = itemService.updateItem(user.getId(), item.getId(), updateDto);
        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(item.getId());
        assertThat(updated.getName()).isEqualTo("Молоток обновленный");
        assertThat(updated.getDescription()).isEqualTo("Стальной молоток");
        assertThat(updated.getAvailable()).isTrue();
    }

    @Test
    @DisplayName("updateItem: должен обновлять только описание вещи")
    void updateItem_shouldUpdateOnlyDescription() {
        var user = userService.addUser(new UserRequestDto("User2b", "user2b@email.com"));
        var itemDto = new ItemRequestDto("Молоток", "Стальной молоток", true, null);
        var item = itemService.addItem(user.getId(), itemDto);
        var updateDto = new ItemRequestDto(null, "Молоток с резиновой ручкой", null, null);
        var updated = itemService.updateItem(user.getId(), item.getId(), updateDto);
        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(item.getId());
        assertThat(updated.getName()).isEqualTo("Молоток");
        assertThat(updated.getDescription()).isEqualTo("Молоток с резиновой ручкой");
        assertThat(updated.getAvailable()).isTrue();
    }

    @Test
    @DisplayName("updateItem: должен обновлять только доступность вещи")
    void updateItem_shouldUpdateOnlyAvailability() {
        var user = userService.addUser(new UserRequestDto("User2c", "user2c@email.com"));
        var itemDto = new ItemRequestDto("Молоток", "Стальной молоток", true, null);
        var item = itemService.addItem(user.getId(), itemDto);
        var updateDto = new ItemRequestDto(null, null, false, null);
        var updated = itemService.updateItem(user.getId(), item.getId(), updateDto);
        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(item.getId());
        assertThat(updated.getName()).isEqualTo("Молоток");
        assertThat(updated.getDescription()).isEqualTo("Стальной молоток");
        assertThat(updated.getAvailable()).isFalse();
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
    @DisplayName("getItem: должен возвращать вещь для другого пользователя")
    void getItem_shouldReturnItemForOtherUser() {
        var owner = userService.addUser(new UserRequestDto("Owner", "owner@email.com"));
        var user = userService.addUser(new UserRequestDto("User3a", "user3a@email.com"));
        var itemDto = new ItemRequestDto("Пассатижи", "Пассатижи с изоляцией", true, null);
        var item = itemService.addItem(owner.getId(), itemDto);
        var found = itemService.getItem(user.getId(), item.getId());
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(item.getId());
        assertThat(found.getName()).isEqualTo(itemDto.getName());
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
    @DisplayName("getUserItems: должен возвращать пустой список для пользователя без вещей")
    void getUserItems_shouldReturnEmptyListForUserWithoutItems() {
        var user = userService.addUser(new UserRequestDto("User4a", "user4a@email.com"));
        var items = itemService.getUserItems(user.getId());
        assertThat(items).isNotNull();
        assertThat(items).isEmpty();
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

    @Test
    @DisplayName("searchItems: должен находить вещи по описанию")
    void searchItems_shouldFindItemsByDescription() {
        var user = userService.addUser(new UserRequestDto("User5a", "user5a@email.com"));
        itemService.addItem(user.getId(), new ItemRequestDto("Инструмент", "Электрический лобзик", true, null));
        itemService.addItem(user.getId(), new ItemRequestDto("Отвертка", "Крестовая отвертка", true, null));
        var found = itemService.searchItems("электрический", 0, 10);
        assertThat(found).isNotNull();
        assertThat(found.stream().anyMatch(i -> i.getDescription().toLowerCase().contains("электрический"))).isTrue();
    }

    @Test
    @DisplayName("searchItems: должен возвращать только доступные вещи")
    void searchItems_shouldReturnOnlyAvailableItems() {
        var user = userService.addUser(new UserRequestDto("User5b", "user5b@email.com"));
        itemService.addItem(user.getId(), new ItemRequestDto("Лобзик", "Электрический лобзик", true, null));
        itemService.addItem(user.getId(), new ItemRequestDto("Отвертка", "Крестовая отвертка", false, null));
        var found = itemService.searchItems("лобзик", 0, 10);
        assertThat(found).isNotNull();
        assertThat(found.stream().allMatch(ItemResponesDto::getAvailable)).isTrue();
    }

    @Test
    @DisplayName("searchItems: должен возвращать пустой список при пустом поиске")
    void searchItems_shouldReturnEmptyListForEmptySearch() {
        var user = userService.addUser(new UserRequestDto("User5c", "user5c@email.com"));
        itemService.addItem(user.getId(), new ItemRequestDto("Лобзик", "Электрический лобзик", true, null));
        var found = itemService.searchItems("", 0, 10);
        assertThat(found).isNotNull();
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("searchItems: должен возвращать пустой список при поиске недоступных вещей")
    void searchItems_shouldReturnEmptyListForUnavailableItems() {
        var user = userService.addUser(new UserRequestDto("User5d", "user5d@email.com"));
        itemService.addItem(user.getId(), new ItemRequestDto("Лобзик", "Электрический лобзик", false, null));
        var found = itemService.searchItems("лобзик", 0, 10);
        assertThat(found).isNotNull();
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("addComment: должен добавлять комментарий к вещи")
    void addComment_shouldAddCommentToItem() {
        var owner = userService.addUser(new UserRequestDto("Owner", "owner@email.com"));
        var user = userService.addUser(new UserRequestDto("User6", "user6@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Дрель", "Дрель ударная", true, null));

        // Создаем бронирование для пользователя
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().minusDays(2));
        bookingRequest.setEnd(LocalDateTime.now().minusDays(1));
        var booking = bookingService.createBooking(user.getId(), bookingRequest);

        // Подтверждаем бронирование
        bookingService.approveBooking(owner.getId(), booking.getId(), true);

        var commentDto = new CommentDto();
        commentDto.setText("Отличная дрель!");

        var comment = itemService.addComment(user.getId(), item.getId(), commentDto);
        assertThat(comment).isNotNull();
        assertThat(comment.getId()).isNotNull();
        assertThat(comment.getText()).isEqualTo("Отличная дрель!");
        assertThat(comment.getAuthorName()).isEqualTo("User6");
    }

    @Test
    @DisplayName("addComment: владелец не может комментировать свою вещь")
    void addComment_shouldNotAllowOwnerToComment() {
        var owner = userService.addUser(new UserRequestDto("Owner", "owner@email.com"));
        var user = userService.addUser(new UserRequestDto("User6a", "user6a@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Дрель", "Дрель ударная", true, null));

        // Создаем бронирование для другого пользователя
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().minusDays(2));
        bookingRequest.setEnd(LocalDateTime.now().minusDays(1));
        var booking = bookingService.createBooking(user.getId(), bookingRequest);

        // Подтверждаем бронирование
        bookingService.approveBooking(owner.getId(), booking.getId(), true);

        var commentDto = new CommentDto();
        commentDto.setText("Моя дрель работает отлично!");

        // Владелец не может комментировать свою вещь
        assertThatThrownBy(() -> itemService.addComment(owner.getId(), item.getId(), commentDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Пользователь не брал вещь в аренду");
    }

    @Test
    @DisplayName("getItem: должен возвращать вещь с комментариями")
    void getItem_shouldReturnItemWithComments() {
        var owner = userService.addUser(new UserRequestDto("Owner", "owner@email.com"));
        var user = userService.addUser(new UserRequestDto("User7", "user7@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Дрель", "Дрель ударная", true, null));

        // Создаем бронирование для пользователя
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().minusDays(2));
        bookingRequest.setEnd(LocalDateTime.now().minusDays(1));
        var booking = bookingService.createBooking(user.getId(), bookingRequest);

        // Подтверждаем бронирование
        bookingService.approveBooking(owner.getId(), booking.getId(), true);

        var commentDto = new CommentDto();
        commentDto.setText("Отличная дрель!");
        itemService.addComment(user.getId(), item.getId(), commentDto);

        var found = itemService.getItem(user.getId(), item.getId());
        assertThat(found).isNotNull();
        assertThat(found.getComments()).isNotNull();
        assertThat(found.getComments()).isNotEmpty();
        assertThat(found.getComments().get(0).getText()).isEqualTo("Отличная дрель!");
    }

    @Test
    @DisplayName("getUserItems: должен возвращать вещи с комментариями")
    void getUserItems_shouldReturnItemsWithComments() {
        var owner = userService.addUser(new UserRequestDto("Owner", "owner@email.com"));
        var user = userService.addUser(new UserRequestDto("User8", "user8@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Дрель", "Дрель ударная", true, null));

        // Создаем бронирование для пользователя
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().minusDays(2));
        bookingRequest.setEnd(LocalDateTime.now().minusDays(1));
        var booking = bookingService.createBooking(user.getId(), bookingRequest);

        // Подтверждаем бронирование
        bookingService.approveBooking(owner.getId(), booking.getId(), true);

        var commentDto = new CommentDto();
        commentDto.setText("Отличная дрель!");
        itemService.addComment(user.getId(), item.getId(), commentDto);

        var items = itemService.getUserItems(owner.getId());
        assertThat(items).isNotNull();
        assertThat(items).isNotEmpty();
        assertThat(items.get(0).getComments()).isNotNull();
        assertThat(items.get(0).getComments()).isNotEmpty();
    }
}