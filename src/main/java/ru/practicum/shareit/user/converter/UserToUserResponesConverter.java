package ru.practicum.shareit.user.converter;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.User;
import ru.practicum.shareit.user.dto.UserResponesModel;

@Component
public class UserToUserResponesConverter {
    public UserResponesModel convert(final User user) {
        UserResponesModel source = new UserResponesModel();
        source.setId(user.getId());
        source.setName(user.getName());
        source.setEmail(user.getEmail());
        return source;
    }
}
