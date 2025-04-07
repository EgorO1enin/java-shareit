package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponesDto;
import ru.practicum.shareit.item.model.Item;


@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item toItem(ItemRequestDto item);

    ItemResponesDto toItemRespones(Item item);
}
