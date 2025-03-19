package ru.practicum.shareit.user.converter;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.User;
import ru.practicum.shareit.user.dto.UserRequestModel;

@Component
public class UserRequestToUserConverter {
    public User convert(final UserRequestModel user) {
        User source = new User();
        source.setName(user.getName());
        source.setEmail(user.getEmail());
        return source;
    }
}
