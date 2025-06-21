package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotNull(message = "Имя пользователя не может быть пустым.")
    private String name;
    @NotNull(message = "Email не может быть пустым.")
    @Email(message = "Некорректный email.")
    private String email;
}