package ru.practicum.shareit.item.converter;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemRequestToItemConverter {
    public Item convert(final ItemRequestDto item) {
        Item source = new Item();
        source.setName(item.getName());
        source.setDescription(item.getDescription());
        source.setAvailable(item.getAvailable());
        //на данном этапе этот код не нужен поэтому я его закоментил))
        //item.getRequestId() != null ? item.getRequestId().getId() : null
        return source;
    }
}
