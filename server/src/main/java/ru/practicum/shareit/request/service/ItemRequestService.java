package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

/**
 * Сервис для работы с запросами на добавление вещей
 */
public interface ItemRequestService {

    /**
     * Создать новый запрос на добавление вещи
     */
    ItemRequestResponseDto createRequest(Long userId, ItemRequestRequestDto requestDto);

    /**
     * Получить все запросы пользователя
     */
    List<ItemRequestResponseDto> getUserRequests(Long userId);

    /**
     * Получить все запросы других пользователей с пагинацией
     */
    List<ItemRequestResponseDto> getAllRequests(Long userId, int from, int size);

    /**
     * Получить конкретный запрос по ID
     */
    ItemRequestResponseDto getRequestById(Long userId, Long requestId);
}