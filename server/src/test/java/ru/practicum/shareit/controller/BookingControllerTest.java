package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemResponesDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;

    @Test
    @DisplayName("POST /bookings — создание бронирования")
    void createBooking_shouldReturnCreatedBooking() throws Exception {
        var request = new BookingRequestDto();
        request.setItemId(1L);
        request.setStart(LocalDateTime.now().plusDays(1));
        request.setEnd(LocalDateTime.now().plusDays(2));
        var response = new BookingResponseDto();
        response.setId(1L);
        response.setStart(request.getStart());
        response.setEnd(request.getEnd());
        response.setStatus(ru.practicum.shareit.booking.model.Booking.BookingStatus.WAITING);
        Mockito.when(bookingService.createBooking(eq(1L), any(BookingRequestDto.class))).thenReturn(response);
        mockMvc.perform(post("/bookings")
                .header("X-Sharer-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("PATCH /bookings/{bookingId} — подтверждение/отклонение бронирования")
    void approveBooking_shouldReturnUpdatedBooking() throws Exception {
        var response = new BookingResponseDto();
        response.setId(1L);
        response.setStatus(ru.practicum.shareit.booking.model.Booking.BookingStatus.APPROVED);
        Mockito.when(bookingService.approveBooking(eq(1L), eq(1L), eq(true))).thenReturn(response);
        mockMvc.perform(patch("/bookings/1")
                .header("X-Sharer-User-Id", "1")
                .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("GET /bookings/{bookingId} — получение бронирования по id")
    void getBooking_shouldReturnBookingById() throws Exception {
        var response = new BookingResponseDto();
        response.setId(1L);
        response.setStart(LocalDateTime.now().plusDays(1));
        response.setEnd(LocalDateTime.now().plusDays(2));
        response.setStatus(ru.practicum.shareit.booking.model.Booking.BookingStatus.APPROVED);
        Mockito.when(bookingService.getBooking(1L, 1L)).thenReturn(response);
        mockMvc.perform(get("/bookings/1")
                .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("GET /bookings — получение всех бронирований пользователя")
    void getUserBookings_shouldReturnListOfBookings() throws Exception {
        var booking1 = new BookingResponseDto();
        booking1.setId(1L);
        booking1.setStatus(ru.practicum.shareit.booking.model.Booking.BookingStatus.APPROVED);
        var booking2 = new BookingResponseDto();
        booking2.setId(2L);
        booking2.setStatus(ru.practicum.shareit.booking.model.Booking.BookingStatus.WAITING);
        Mockito.when(bookingService.getUserBookings(eq(1L), eq("ALL"), eq(0), eq(10))).thenReturn(List.of(booking1, booking2));
        mockMvc.perform(get("/bookings")
                .header("X-Sharer-User-Id", "1")
                .param("state", "ALL")
                .param("from", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @DisplayName("GET /bookings/owner — получение всех бронирований для вещей пользователя")
    void getOwnerBookings_shouldReturnListOfOwnerBookings() throws Exception {
        var booking1 = new BookingResponseDto();
        booking1.setId(1L);
        booking1.setStatus(ru.practicum.shareit.booking.model.Booking.BookingStatus.APPROVED);
        var booking2 = new BookingResponseDto();
        booking2.setId(2L);
        booking2.setStatus(ru.practicum.shareit.booking.model.Booking.BookingStatus.WAITING);
        Mockito.when(bookingService.getOwnerBookings(eq(1L), eq("ALL"), eq(0), eq(10))).thenReturn(List.of(booking1, booking2));
        mockMvc.perform(get("/bookings/owner")
                .header("X-Sharer-User-Id", "1")
                .param("state", "ALL")
                .param("from", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }
} 