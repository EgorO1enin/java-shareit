package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Бронирования", description = "Управление бронированиями вещей")
@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@Operation(summary = "Получить список бронирований пользователя", description = "Позволяет получить список бронирований с фильтрацией по статусу и пагинацией.")
	@GetMapping
	public ResponseEntity<Object> getBookings(
			@Parameter(description = "ID пользователя", required = true) @RequestHeader("X-Sharer-User-Id") Long userId,
			@Parameter(description = "Статус бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)", example = "all")
			@RequestParam(defaultValue = "all") String stateParam,
			@Parameter(description = "Номер первого элемента для пагинации", example = "0")
			@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
			@Parameter(description = "Количество элементов для пагинации", example = "10")
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@Operation(summary = "Создать бронирование", description = "Позволяет пользователю создать новое бронирование вещи.")
	@PostMapping
	public ResponseEntity<Object> bookItem(
			@Parameter(description = "ID пользователя", required = true) @RequestHeader("X-Sharer-User-Id") long userId,
			@Parameter(description = "Данные для создания бронирования", required = true) @RequestBody @Valid BookItemRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@Operation(summary = "Получить бронирование по ID", description = "Возвращает информацию о бронировании по его идентификатору.")
	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(
			@Parameter(description = "ID пользователя", required = true) @RequestHeader("X-Sharer-User-Id") long userId,
			@Parameter(description = "ID бронирования", required = true) @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@PatchMapping("/{bookingId}")
	@Operation(summary = "Подтверждение или отклонение бронирования")
	public ResponseEntity<Object> approveBooking(
			@RequestHeader("X-Sharer-User-Id") @Parameter(description = "ID пользователя") Long userId,
			@PathVariable @Parameter(description = "ID бронирования") Long bookingId,
			@RequestParam @Parameter(description = "Подтверждение бронирования") boolean approved) {
		return bookingClient.approveBooking(userId, bookingId, approved);
	}

	@GetMapping("/owner")
	@Operation(summary = "Получение списка бронирований для вещей пользователя")
	public ResponseEntity<Object> getOwnerBookings(
			@RequestHeader("X-Sharer-User-Id") @Parameter(description = "ID пользователя") Long userId,
			@RequestParam(defaultValue = "ALL") @Parameter(description = "Статус бронирования") String state,
			@RequestParam(defaultValue = "0") @PositiveOrZero @Parameter(description = "Индекс первого элемента") int from,
			@RequestParam(defaultValue = "10") @Positive @Parameter(description = "Количество элементов для отображения") int size) {
		return bookingClient.getOwnerBookings(userId, state, from, size);
	}
}
