package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private int id; // уникальный идентификатор бронирования
    private LocalDateTime start; // дата и время начала бронирования
    private LocalDateTime end; // дата и время конца бронирования
    private Item item; // вещь, которую бронируют
    private User booker; // пользователь, который бронирует
    private BookingStatus status; // статус бронирования

    // Перечисление для статуса бронирования
    public enum BookingStatus {
        WAITING, // новое бронирование, ожидает одобрения
        APPROVED // бронирование одобрено
    }
}
