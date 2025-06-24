package ru.practicum.shareit.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BookingServiceIntegrationTest {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    @Test
    @DisplayName("createBooking: должен создавать бронирование для существующего пользователя и вещи")
    void createBooking_shouldCreateBookingForExistingUserAndItem() {
        // Arrange: создаем пользователя и вещь
        var user = userService.addUser(new UserRequestDto("Booker", "booker@email.com"));
        var owner = userService.addUser(new UserRequestDto("Owner", "owner@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Дрель", "Мощная дрель", true, null));

        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));

        // Act
        BookingResponseDto booking = bookingService.createBooking(user.getId(), bookingRequest);

        // Assert
        assertThat(booking).isNotNull();
        assertThat(booking.getId()).isNotNull();
        assertThat(booking.getItem().getId()).isEqualTo(item.getId());
        assertThat(booking.getBooker().getId()).isEqualTo(user.getId());
        assertThat(booking.getStart()).isEqualTo(bookingRequest.getStart());
        assertThat(booking.getEnd()).isEqualTo(bookingRequest.getEnd());
    }


    @Test
    @DisplayName("getBooking: должен возвращать бронирование по id для владельца или арендатора")
    void getBooking_shouldReturnBookingForOwnerOrBooker() {
        var user = userService.addUser(new UserRequestDto("Booker3", "booker3@email.com"));
        var owner = userService.addUser(new UserRequestDto("Owner3", "owner3@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Перфоратор", "Перфоратор Bosch", true, null));
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));
        var booking = bookingService.createBooking(user.getId(), bookingRequest);
        var foundByBooker = bookingService.getBooking(user.getId(), booking.getId());
        var foundByOwner = bookingService.getBooking(owner.getId(), booking.getId());
        assertThat(foundByBooker.getId()).isEqualTo(booking.getId());
        assertThat(foundByOwner.getId()).isEqualTo(booking.getId());
    }

    @Test
    @DisplayName("getUserBookings: должен возвращать список бронирований пользователя")
    void getUserBookings_shouldReturnBookingsForUser() {
        var user = userService.addUser(new UserRequestDto("Booker4", "booker4@email.com"));
        var owner = userService.addUser(new UserRequestDto("Owner4", "owner4@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Шуруповерт", "Шуруповерт Makita", true, null));
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.createBooking(user.getId(), bookingRequest);
        var bookings = bookingService.getUserBookings(user.getId(), "ALL", 0, 10);
        assertThat(bookings).isNotEmpty();
        assertThat(bookings.get(0).getBooker().getId()).isEqualTo(user.getId());
    }
}