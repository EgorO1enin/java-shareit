package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemResponesModel {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private User owner;
    private Integer requestId;
}
