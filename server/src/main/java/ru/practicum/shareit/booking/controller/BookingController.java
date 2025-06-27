package ru.practicum.shareit.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(name = "Бронирования", description = "API для работы с бронированиями")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Создание нового бронирования")
    public ResponseEntity<BookingResponseDto> createBooking(
            @RequestHeader("X-Sharer-User-Id") @Parameter(description = "ID пользователя") Long userId,
            @RequestBody @Parameter(description = "Данные бронирования") BookingRequestDto bookingRequestDto) {
        return ResponseEntity.ok(bookingService.createBooking(userId, bookingRequestDto));
    }

    @PatchMapping("/{bookingId}")
    @Operation(summary = "Подтверждение или отклонение бронирования")
    public ResponseEntity<BookingResponseDto> approveBooking(
            @RequestHeader("X-Sharer-User-Id") @Parameter(description = "ID пользователя") Long userId,
            @PathVariable @Parameter(description = "ID бронирования") Long bookingId,
            @RequestParam @Parameter(description = "Подтверждение бронирования") boolean approved) {
        log.info("User {} approves booking {} with status {}", userId, bookingId, approved);
        return ResponseEntity.ok(bookingService.approveBooking(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    @Operation(summary = "Получение информации о бронировании")
    public ResponseEntity<BookingResponseDto> getBooking(
            @RequestHeader("X-Sharer-User-Id") @Parameter(description = "ID пользователя") Long userId,
            @PathVariable @Parameter(description = "ID бронирования") Long bookingId) {
        return ResponseEntity.ok(bookingService.getBooking(userId, bookingId));
    }

    @GetMapping
    @Operation(summary = "Получение списка бронирований пользователя")
    public ResponseEntity<List<BookingResponseDto>> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") @Parameter(description = "ID пользователя") Long userId,
            @RequestParam(defaultValue = "ALL") @Parameter(description = "Статус бронирования") String state,
            @RequestParam(defaultValue = "0") @Parameter(description = "Индекс первого элемента") int from,
            @RequestParam(defaultValue = "10") @Parameter(description = "Количество элементов для отображения") int size) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId, state, from, size));
    }

    @GetMapping("/owner")
    @Operation(summary = "Получение списка бронирований для вещей пользователя")
    public ResponseEntity<List<BookingResponseDto>> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") @Parameter(description = "ID пользователя") Long userId,
            @RequestParam(defaultValue = "ALL") @Parameter(description = "Статус бронирования") String state,
            @RequestParam(defaultValue = "0") @Parameter(description = "Индекс первого элемента") int from,
            @RequestParam(defaultValue = "10") @Parameter(description = "Количество элементов для отображения") int size) {
        return ResponseEntity.ok(bookingService.getOwnerBookings(userId, state, from, size));
    }
}