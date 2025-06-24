package ru.practicum.shareit.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.model.Booking;
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
        BookingResponseDto booking = bookingService.createBooking(user.getId(), bookingRequest);
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

    @Test
    @DisplayName("getUserBookings: должен возвращать текущие бронирования")
    void getUserBookings_shouldReturnCurrentBookings() {
        var user = userService.addUser(new UserRequestDto("Booker5", "booker5@email.com"));
        var owner = userService.addUser(new UserRequestDto("Owner5", "owner5@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Болгарка", "Болгарка DeWalt", true, null));
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().minusHours(1));
        bookingRequest.setEnd(LocalDateTime.now().plusHours(1));
        bookingService.createBooking(user.getId(), bookingRequest);
        var bookings = bookingService.getUserBookings(user.getId(), "CURRENT", 0, 10);
        assertThat(bookings).isNotEmpty();
    }

    @Test
    @DisplayName("getUserBookings: должен возвращать прошлые бронирования")
    void getUserBookings_shouldReturnPastBookings() {
        var user = userService.addUser(new UserRequestDto("Booker6", "booker6@email.com"));
        var owner = userService.addUser(new UserRequestDto("Owner6", "owner6@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Пила", "Циркулярная пила", true, null));
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().minusDays(2));
        bookingRequest.setEnd(LocalDateTime.now().minusDays(1));
        bookingService.createBooking(user.getId(), bookingRequest);
        var bookings = bookingService.getUserBookings(user.getId(), "PAST", 0, 10);
        assertThat(bookings).isNotEmpty();
    }

    @Test
    @DisplayName("getUserBookings: должен возвращать будущие бронирования")
    void getUserBookings_shouldReturnFutureBookings() {
        var user = userService.addUser(new UserRequestDto("Booker7", "booker7@email.com"));
        var owner = userService.addUser(new UserRequestDto("Owner7", "owner7@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Отвертка", "Электрическая отвертка", true, null));
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.createBooking(user.getId(), bookingRequest);
        var bookings = bookingService.getUserBookings(user.getId(), "FUTURE", 0, 10);
        assertThat(bookings).isNotEmpty();
    }

    @Test
    @DisplayName("getUserBookings: должен возвращать ожидающие бронирования")
    void getUserBookings_shouldReturnWaitingBookings() {
        var user = userService.addUser(new UserRequestDto("Booker8", "booker8@email.com"));
        var owner = userService.addUser(new UserRequestDto("Owner8", "owner8@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Молоток", "Молоток строительный", true, null));
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.createBooking(user.getId(), bookingRequest);
        var bookings = bookingService.getUserBookings(user.getId(), "WAITING", 0, 10);
        assertThat(bookings).isNotEmpty();
    }

    @Test
    @DisplayName("getUserBookings: должен возвращать отклоненные бронирования")
    void getUserBookings_shouldReturnRejectedBookings() {
        var user = userService.addUser(new UserRequestDto("Booker9", "booker9@email.com"));
        var owner = userService.addUser(new UserRequestDto("Owner9", "owner9@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Ключ", "Газовый ключ", true, null));

        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));
        var booking = bookingService.createBooking(user.getId(), bookingRequest);
        bookingService.approveBooking(owner.getId(), booking.getId(), false);
        var bookings = bookingService.getUserBookings(user.getId(), "REJECTED", 0, 10);
        assertThat(bookings).isNotEmpty();
    }

    @Test
    @DisplayName("getOwnerBookings: должен возвращать бронирования владельца")
    void getOwnerBookings_shouldReturnOwnerBookings() {
        var user = userService.addUser(new UserRequestDto("Booker10", "booker10@email.com"));
        var owner = userService.addUser(new UserRequestDto("Owner10", "owner10@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Лопата", "Совковая лопата", true, null));
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.createBooking(user.getId(), bookingRequest);
        var bookings = bookingService.getOwnerBookings(owner.getId(), "ALL", 0, 10);
        assertThat(bookings).isNotEmpty();
        assertThat(bookings.get(0).getItem().getId()).isEqualTo(item.getId());
    }

    @Test
    @DisplayName("getOwnerBookings: должен возвращать текущие бронирования владельца")
    void getOwnerBookings_shouldReturnCurrentOwnerBookings() {
        var user = userService.addUser(new UserRequestDto("Booker11", "booker11@email.com"));
        var owner = userService.addUser(new UserRequestDto("Owner11", "owner11@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Топор", "Топор плотницкий", true, null));
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().minusHours(1));
        bookingRequest.setEnd(LocalDateTime.now().plusHours(1));
        bookingService.createBooking(user.getId(), bookingRequest);
        var bookings = bookingService.getOwnerBookings(owner.getId(), "CURRENT", 0, 10);
        assertThat(bookings).isNotEmpty();
    }

    @Test
    @DisplayName("getOwnerBookings: должен возвращать прошлые бронирования владельца")
    void getOwnerBookings_shouldReturnPastOwnerBookings() {
        var user = userService.addUser(new UserRequestDto("Booker12", "booker12@email.com"));
        var owner = userService.addUser(new UserRequestDto("Owner12", "owner12@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Рубанок", "Рубанок электрический", true, null));
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().minusDays(2));
        bookingRequest.setEnd(LocalDateTime.now().minusDays(1));
        bookingService.createBooking(user.getId(), bookingRequest);
        var bookings = bookingService.getOwnerBookings(owner.getId(), "PAST", 0, 10);
        assertThat(bookings).isNotEmpty();
    }

    @Test
    @DisplayName("getOwnerBookings: должен возвращать будущие бронирования владельца")
    void getOwnerBookings_shouldReturnFutureOwnerBookings() {
        var user = userService.addUser(new UserRequestDto("Booker13", "booker13@email.com"));
        var owner = userService.addUser(new UserRequestDto("Owner13", "owner13@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Стамеска", "Стамеска по дереву", true, null));
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.createBooking(user.getId(), bookingRequest);
        var bookings = bookingService.getOwnerBookings(owner.getId(), "FUTURE", 0, 10);
        assertThat(bookings).isNotEmpty();
    }

    @Test
    @DisplayName("getOwnerBookings: должен возвращать ожидающие бронирования владельца")
    void getOwnerBookings_shouldReturnWaitingOwnerBookings() {
        var user = userService.addUser(new UserRequestDto("Booker14", "booker14@email.com"));
        var owner = userService.addUser(new UserRequestDto("Owner14", "owner14@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Ножовка", "Ножовка по металлу", true, null));
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.createBooking(user.getId(), bookingRequest);
        var bookings = bookingService.getOwnerBookings(owner.getId(), "WAITING", 0, 10);
        assertThat(bookings).isNotEmpty();
    }

    @Test
    @DisplayName("getOwnerBookings: должен возвращать отклоненные бронирования владельца")
    void getOwnerBookings_shouldReturnRejectedOwnerBookings() {
        var user = userService.addUser(new UserRequestDto("Booker15", "booker15@email.com"));
        var owner = userService.addUser(new UserRequestDto("Owner15", "owner15@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Кусачки", "Кусачки боковые", true, null));
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));
        var booking = bookingService.createBooking(user.getId(), bookingRequest);
        bookingService.approveBooking(owner.getId(), booking.getId(), false);
        var bookings = bookingService.getOwnerBookings(owner.getId(), "REJECTED", 0, 10);
        assertThat(bookings).isNotEmpty();
    }

    @Test
    @DisplayName("approveBooking: должен одобрять бронирование")
    void approveBooking_shouldApproveBooking() {
        var user = userService.addUser(new UserRequestDto("Booker16", "booker16@email.com"));
        var owner = userService.addUser(new UserRequestDto("Owner16", "owner16@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Плоскогубцы", "Плоскогубцы комбинированные", true, null));
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));
        var booking = bookingService.createBooking(user.getId(), bookingRequest);
        var approvedBooking = bookingService.approveBooking(owner.getId(), booking.getId(), true);
        assertThat(approvedBooking).isNotNull();
        assertThat(approvedBooking.getId()).isEqualTo(booking.getId());
        assertThat(approvedBooking.getStatus()).isEqualTo(Booking.BookingStatus.APPROVED);
    }

    @Test
    @DisplayName("approveBooking: должен отклонять бронирование")
    void approveBooking_shouldRejectBooking() {
        var user = userService.addUser(new UserRequestDto("Booker17", "booker17@email.com"));
        var owner = userService.addUser(new UserRequestDto("Owner17", "owner17@email.com"));
        var item = itemService.addItem(owner.getId(), new ItemRequestDto("Отвертка", "Крестовая отвертка", true, null));
        var bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));
        var booking = bookingService.createBooking(user.getId(), bookingRequest);
        var rejectedBooking = bookingService.approveBooking(owner.getId(), booking.getId(), false);
        assertThat(rejectedBooking).isNotNull();
        assertThat(rejectedBooking.getId()).isEqualTo(booking.getId());
        assertThat(rejectedBooking.getStatus()).isEqualTo(Booking.BookingStatus.REJECTED);
    }
}