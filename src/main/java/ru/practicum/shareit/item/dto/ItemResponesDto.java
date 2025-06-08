package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserResponesDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemResponesDto {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private UserResponesDto owner;
    private Integer requestId;
}
