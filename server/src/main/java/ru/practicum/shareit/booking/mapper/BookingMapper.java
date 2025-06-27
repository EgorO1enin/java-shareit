package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "item", source = "item")
    @Mapping(target = "booker", source = "booker")
    BookingResponseDto toBookingResponseDto(Booking booking);
}