package ru.practicum.shareit.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ItemRequestServiceIntegrationTest {
    @Autowired
    private ItemRequestService itemRequestService;
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("createRequest: должен создавать новый запрос на вещь")
    void createRequest_shouldCreateNewRequest() {
        var user = userService.addUser(new UserRequestDto("Requester1", "requester1@email.com"));
        var requestDto = new ItemRequestRequestDto();
        requestDto.setDescription("Нужна дрель");
        ItemRequestResponseDto request = itemRequestService.createRequest(user.getId(), requestDto);
        assertThat(request).isNotNull();
        assertThat(request.getId()).isNotNull();
        assertThat(request.getDescription()).isEqualTo(requestDto.getDescription());
    }

    @Test
    @DisplayName("getUserRequests: должен возвращать все запросы пользователя")
    void getUserRequests_shouldReturnAllRequestsForUser() {
        var user = userService.addUser(new UserRequestDto("Requester2", "requester2@email.com"));
        var requestDto1 = new ItemRequestRequestDto();
        requestDto1.setDescription("Нужен молоток");
        var requestDto2 = new ItemRequestRequestDto();
        requestDto2.setDescription("Нужна отвертка");
        itemRequestService.createRequest(user.getId(), requestDto1);
        itemRequestService.createRequest(user.getId(), requestDto2);
        List<ItemRequestResponseDto> requests = itemRequestService.getUserRequests(user.getId());
        assertThat(requests).isNotNull();
        assertThat(requests.size()).isGreaterThanOrEqualTo(2);
        assertThat(requests.stream().map(ItemRequestResponseDto::getDescription)).contains(requestDto1.getDescription(), requestDto2.getDescription());
    }

    @Test
    @DisplayName("getAllRequests: должен возвращать все запросы других пользователей с пагинацией")
    void getAllRequests_shouldReturnAllRequestsFromOthers() {
        var user1 = userService.addUser(new UserRequestDto("Requester3", "requester3@email.com"));
        var user2 = userService.addUser(new UserRequestDto("Requester4", "requester4@email.com"));
        var requestDto1 = new ItemRequestRequestDto();
        requestDto1.setDescription("Нужна пила");
        var requestDto2 = new ItemRequestRequestDto();
        requestDto2.setDescription("Нужна рулетка");
        itemRequestService.createRequest(user1.getId(), requestDto1);
        itemRequestService.createRequest(user1.getId(), requestDto2);
        List<ItemRequestResponseDto> requests = itemRequestService.getAllRequests(user2.getId(), 0, 10);
        assertThat(requests).isNotNull();
        assertThat(requests.size()).isGreaterThanOrEqualTo(2);
        assertThat(requests.stream().map(ItemRequestResponseDto::getDescription)).contains(requestDto1.getDescription(), requestDto2.getDescription());
    }

    @Test
    @DisplayName("getRequestById: должен возвращать запрос по id для пользователя")
    void getRequestById_shouldReturnRequestByIdForUser() {
        var user = userService.addUser(new UserRequestDto("Requester5", "requester5@email.com"));
        var requestDto = new ItemRequestRequestDto();
        requestDto.setDescription("Нужен степлер");
        var created = itemRequestService.createRequest(user.getId(), requestDto);
        var found = itemRequestService.getRequestById(user.getId(), created.getId());
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(created.getId());
        assertThat(found.getDescription()).isEqualTo(requestDto.getDescription());
    }
} 