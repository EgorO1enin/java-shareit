package ru.practicum.shareit.user.converter;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserResponesDto;

@Component
public class UserToUserResponesConverter {
    public UserResponesDto convert(final User user) {
        UserResponesDto source = new UserResponesDto();
        source.setId(user.getId());
        source.setName(user.getName());
        source.setEmail(user.getEmail());
        return source;
    }
}
