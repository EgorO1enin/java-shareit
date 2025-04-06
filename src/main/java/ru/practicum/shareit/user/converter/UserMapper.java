package ru.practicum.shareit.user.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserResponesDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserRequestDto;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequestDto user);
    UserResponesDto toUserResponesDto(User user);
}
/*@Component
public class UserRequestToUserConverter {
    public User convert(final UserRequestDto user) {
        User source = new User();
        source.setName(user.getName());
        source.setEmail(user.getEmail());
        return source;
    }
}*/
