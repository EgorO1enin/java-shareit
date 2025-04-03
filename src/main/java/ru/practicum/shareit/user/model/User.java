package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
}
