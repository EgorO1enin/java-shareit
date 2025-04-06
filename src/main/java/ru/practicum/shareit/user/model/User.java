package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@Data
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;

}
