package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemResponesDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemRequestResponseDto {
    private Long id;
    private String description;
    private UserResponseDto requestor;
    private LocalDateTime created;
    private List<ItemResponesDto> items;
} 