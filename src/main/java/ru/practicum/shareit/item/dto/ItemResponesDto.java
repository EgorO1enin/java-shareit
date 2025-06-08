package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.dto.UserResponesDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemResponesDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private Long requestId;
    private BookingResponseDto lastBooking;
    private BookingResponseDto nextBooking;
    private List<CommentResponseDto> comments;
}
