package ru.practicum.shareit.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemResponesDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingResponseDtoJsonTest {

    private ObjectMapper objectMapper;
    private BookingResponseDto bookingResponseDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(1L);
        bookingResponseDto.setStart(LocalDateTime.of(2024, 1, 15, 10, 0));
        bookingResponseDto.setEnd(LocalDateTime.of(2024, 1, 16, 10, 0));
        bookingResponseDto.setStatus(Booking.BookingStatus.APPROVED);

        var item = new ItemResponesDto();
        item.setId(1L);
        item.setName("Дрель");
        item.setDescription("Мощная дрель");
        item.setAvailable(true);
        bookingResponseDto.setItem(item);

        var booker = new UserResponseDto();
        booker.setId(1L);
        booker.setName("Booker");
        booker.setEmail("booker@email.com");
        bookingResponseDto.setBooker(booker);
    }

    @Test
    void serialize_ShouldSerializeCorrectly() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(bookingResponseDto);
        assertNotNull(json);
        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"start\":"));
        assertTrue(json.contains("\"end\":"));
        assertTrue(json.contains("\"status\":\"APPROVED\""));
        assertTrue(json.contains("\"item\":"));
        assertTrue(json.contains("\"booker\":"));
    }

    @Test
    void deserialize_ShouldDeserializeCorrectly() throws JsonProcessingException {
        String json = """ 
                    {
                    "id": 1,
                    "start": "2024-01-15T10:00:00",
                    "end": "2024-01-16T10:00:00",
                    "status": "APPROVED",
                    "item": {
                        "id": 1,
                        "name": "Дрель",
                        "description": "Мощная дрель",
                        "available": true
                    },
                    "booker": {
                        "id": 1,
                        "name": "Booker",
                        "email": "booker@email.com"
                    }
                }""";

        BookingResponseDto result = objectMapper.readValue(json, BookingResponseDto.class);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LocalDateTime.of(2024, 1, 15, 10, 0), result.getStart());
        assertEquals(LocalDateTime.of(2024, 1, 16, 10, 0), result.getEnd());
        assertEquals(Booking.BookingStatus.APPROVED, result.getStatus());
        assertNotNull(result.getItem());
        assertEquals(1L, result.getItem().getId());
        assertEquals("Дрель", result.getItem().getName());
        assertNotNull(result.getBooker());
        assertEquals(1L, result.getBooker().getId());
        assertEquals("Booker", result.getBooker().getName());
    }

    @Test
    void deserialize_ShouldHandleDifferentDateTimeFormats() throws JsonProcessingException {
        String json = """
                {
                    "id": 1,
                    "start": "2024-01-15T10:00:00.000",
                    "end": "2024-01-16T10:00:00.000",
                    "status": "WAITING"
                }""";

        BookingResponseDto result = objectMapper.readValue(json, BookingResponseDto.class);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LocalDateTime.of(2024, 1, 15, 10, 0), result.getStart());
        assertEquals(LocalDateTime.of(2024, 1, 16, 10, 0), result.getEnd());
        assertEquals(Booking.BookingStatus.WAITING, result.getStatus());
    }

    @Test
    void deserialize_ShouldHandleNullValues() throws JsonProcessingException {
        String json = """
                {
                    "id": null,
                    "start": null,
                    "end": null,
                    "status": null,
                    "item": null,
                    "booker": null
                }""";

        BookingResponseDto result = objectMapper.readValue(json, BookingResponseDto.class);

        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getStart());
        assertNull(result.getEnd());
        assertNull(result.getStatus());
        assertNull(result.getItem());
        assertNull(result.getBooker());
    }

    @Test
    void roundTrip_ShouldPreserveData() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(bookingResponseDto);
        BookingResponseDto result = objectMapper.readValue(json, BookingResponseDto.class);

        assertEquals(bookingResponseDto.getId(), result.getId());
        assertEquals(bookingResponseDto.getStart(), result.getStart());
        assertEquals(bookingResponseDto.getEnd(), result.getEnd());
        assertEquals(bookingResponseDto.getStatus(), result.getStatus());
        assertNotNull(result.getItem());
        assertEquals(bookingResponseDto.getItem().getId(), result.getItem().getId());
        assertNotNull(result.getBooker());
        assertEquals(bookingResponseDto.getBooker().getId(), result.getBooker().getId());
    }
}