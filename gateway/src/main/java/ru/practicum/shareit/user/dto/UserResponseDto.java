package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
}