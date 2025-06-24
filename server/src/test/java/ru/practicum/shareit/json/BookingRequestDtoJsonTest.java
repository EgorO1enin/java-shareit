package ru.practicum.shareit.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingRequestDtoJsonTest {

    private ObjectMapper objectMapper;
    private BookingRequestDto bookingRequestDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(LocalDateTime.of(2024, 1, 15, 10, 0));
        bookingRequestDto.setEnd(LocalDateTime.of(2024, 1, 16, 10, 0));
    }

    @Test
    void serialize_ShouldSerializeCorrectly() throws JsonProcessingException {

        String json = objectMapper.writeValueAsString(bookingRequestDto);


        assertNotNull(json);
        assertTrue(json.contains("\"itemId\":1"));
        assertTrue(json.contains("\"start\":"));
        assertTrue(json.contains("\"end\":"));
    }

    @Test
    void deserialize_ShouldDeserializeCorrectly() throws JsonProcessingException {

        String json = """
                {
                    "itemId": 1,
                    "start": "2024-01-15T10:00:00",
                    "end": "2024-01-16T10:00:00"
                }
                """;


        BookingRequestDto result = objectMapper.readValue(json, BookingRequestDto.class);


        assertNotNull(result);
        assertEquals(1L, result.getItemId());
        assertEquals(LocalDateTime.of(2024, 1, 15, 10, 0), result.getStart());
        assertEquals(LocalDateTime.of(2024, 1, 16, 10, 0), result.getEnd());
    }

    @Test
    void deserialize_ShouldHandleNullValues() throws JsonProcessingException {

        String json = """
                {
                    "itemId": null,
                    "start": null,
                    "end": null
                }
                """;


        BookingRequestDto result = objectMapper.readValue(json, BookingRequestDto.class);


        assertNotNull(result);
        assertNull(result.getItemId());
        assertNull(result.getStart());
        assertNull(result.getEnd());
    }

    @Test
    void deserialize_ShouldHandleMissingFields() throws JsonProcessingException {

        String json = "{}";


        BookingRequestDto result = objectMapper.readValue(json, BookingRequestDto.class);


        assertNotNull(result);
        assertNull(result.getItemId());
        assertNull(result.getStart());
        assertNull(result.getEnd());
    }

    @Test
    void roundTrip_ShouldPreserveData() throws JsonProcessingException {

        String json = objectMapper.writeValueAsString(bookingRequestDto);
        BookingRequestDto result = objectMapper.readValue(json, BookingRequestDto.class);


        assertEquals(bookingRequestDto.getItemId(), result.getItemId());
        assertEquals(bookingRequestDto.getStart(), result.getStart());
        assertEquals(bookingRequestDto.getEnd(), result.getEnd());
    }

    @Test
    void deserialize_ShouldHandleDifferentDateTimeFormats() throws JsonProcessingException {

        String json = """
                {
                    "itemId": 1,
                    "start": "2024-01-15T10:00:00.000",
                    "end": "2024-01-16T10:00:00.000"
                }
                """;


        BookingRequestDto result = objectMapper.readValue(json, BookingRequestDto.class);


        assertNotNull(result);
        assertEquals(1L, result.getItemId());
        assertEquals(LocalDateTime.of(2024, 1, 15, 10, 0), result.getStart());
        assertEquals(LocalDateTime.of(2024, 1, 16, 10, 0), result.getEnd());
    }

    @Test
    void serialize_ShouldHandleNullValues() throws JsonProcessingException {

        bookingRequestDto.setItemId(null);
        bookingRequestDto.setStart(null);
        bookingRequestDto.setEnd(null);


        String json = objectMapper.writeValueAsString(bookingRequestDto);


        assertNotNull(json);
        assertTrue(json.contains("\"itemId\":null"));
        assertTrue(json.contains("\"start\":null"));
        assertTrue(json.contains("\"end\":null"));
    }
}