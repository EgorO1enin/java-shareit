package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponesDto;
import ru.practicum.shareit.user.model.User;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequestDto user);

    UserResponesDto toUserResponesDto(User user);
}
