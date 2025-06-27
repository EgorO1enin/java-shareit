package ru.practicum.shareit.request.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemResponesDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с запросами на добавление вещей
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public ItemRequestResponseDto createRequest(Long userId, ItemRequestRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
        ItemRequest itemRequest = itemRequestMapper.toEntity(requestDto);
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        ItemRequest savedRequest = itemRequestRepository.save(itemRequest);
        return enrichRequestWithItems(savedRequest);
    }

    @Override
    public List<ItemRequestResponseDto> getUserRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
        List<ItemRequest> requests = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        return requests.stream()
                .map(this::enrichRequestWithItems)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestResponseDto> getAllRequests(Long userId, int from, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<ItemRequest> requests = itemRequestRepository
                .findAllByRequestorIdNotOrderByCreatedDesc(userId, pageRequest)
                .getContent();
        return requests.stream()
                .map(this::enrichRequestWithItems)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestResponseDto getRequestById(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с ID " + requestId + " не найден"));
        return enrichRequestWithItems(request);
    }

    /**
     * Обогащает запрос информацией о вещах, которые были добавлены в ответ на него
     */
    private ItemRequestResponseDto enrichRequestWithItems(ItemRequest request) {
        ItemRequestResponseDto dto = itemRequestMapper.toResponseDto(request, userMapper);
        List<Item> items = itemRepository.findByRequestId(request.getId());
        List<ItemResponesDto> itemResponses = items.stream()
                .map(itemMapper::toItemRespones)
                .collect(Collectors.toList());
        dto.setItems(itemResponses);
        return dto;
    }
}