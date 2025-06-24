package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;

/**
 * Маппер для преобразования между ItemRequest и DTO
 */
@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ItemRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestor", ignore = true)
    @Mapping(target = "created", ignore = true)
    ItemRequest toEntity(ItemRequestRequestDto dto);

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "requestor", expression = "java(userMapper.toUserResponseDto(entity.getRequestor()))")
    ItemRequestResponseDto toResponseDto(ItemRequest entity, UserMapper userMapper);
}