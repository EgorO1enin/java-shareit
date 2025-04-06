package ru.practicum.shareit.item.converter;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemResponesDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemToResponesModelConverter {
    public ItemResponesDto convert(final Item item) {
        return new ItemResponesDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }
}
