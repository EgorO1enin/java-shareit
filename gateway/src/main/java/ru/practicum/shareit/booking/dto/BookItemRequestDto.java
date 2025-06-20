package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные для создания бронирования")
public class BookItemRequestDto {
	@Schema(description = "ID вещи", example = "1")
	private long itemId;
	@FutureOrPresent
	@Schema(description = "Дата и время начала бронирования", example = "2024-06-01T10:00:00")
	private LocalDateTime start;
	@Future
	@Schema(description = "Дата и время окончания бронирования", example = "2024-06-02T10:00:00")
	private LocalDateTime end;
}
