package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.dto.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@AllArgsConstructor
@Data
public class ItemRequestModel {
    private int id; // уникальный идентификатор запроса
    private String description; // текст запроса, содержащий описание требуемой вещи
    private User requestor; // пользователь, создавший запрос
    private LocalDateTime created;
}
