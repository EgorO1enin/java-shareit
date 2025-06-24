package ru.practicum.shareit.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponesDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemResponesDtoJsonTest {

    private ObjectMapper objectMapper;
    private ItemResponesDto itemResponesDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        itemResponesDto = new ItemResponesDto();
        itemResponesDto.setId(1L);
        itemResponesDto.setName("Дрель");
        itemResponesDto.setDescription("Мощная дрель");
        itemResponesDto.setAvailable(true);
        itemResponesDto.setOwnerId(1L);
        itemResponesDto.setRequestId(null);

        var lastBooking = new BookingResponseDto();
        lastBooking.setId(1L);
        lastBooking.setStart(LocalDateTime.of(2024, 1, 10, 10, 0));
        lastBooking.setEnd(LocalDateTime.of(2024, 1, 12, 10, 0));
        lastBooking.setStatus(Booking.BookingStatus.APPROVED);
        var lastBooker = new UserResponseDto();
        lastBooker.setId(2L);
        lastBooker.setName("Last Booker");
        lastBooker.setEmail("last@email.com");
        lastBooking.setBooker(lastBooker);
        itemResponesDto.setLastBooking(lastBooking);

        var nextBooking = new BookingResponseDto();
        nextBooking.setId(2L);
        nextBooking.setStart(LocalDateTime.of(2024, 1, 20, 10, 0));
        nextBooking.setEnd(LocalDateTime.of(2024, 1, 22, 10, 0));
        nextBooking.setStatus(Booking.BookingStatus.WAITING);
        var nextBooker = new UserResponseDto();
        nextBooker.setId(3L);
        nextBooker.setName("Next Booker");
        nextBooker.setEmail("next@email.com");
        nextBooking.setBooker(nextBooker);
        itemResponesDto.setNextBooking(nextBooking);

        var comment1 = new CommentResponseDto();
        comment1.setId(1L);
        comment1.setText("Отличная вещь!");
        comment1.setAuthorName("Commenter1");
        comment1.setCreated(LocalDateTime.of(2024, 1, 13, 10, 0));
        var comment2 = new CommentResponseDto();
        comment2.setId(2L);
        comment2.setText("Рекомендую!");
        comment2.setAuthorName("Commenter2");
        comment2.setCreated(LocalDateTime.of(2024, 1, 14, 10, 0));
        itemResponesDto.setComments(List.of(comment1, comment2));
    }

    @Test
    void serialize_ShouldSerializeCorrectly() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(itemResponesDto);
        assertNotNull(json);
        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"name\":\"Дрель\""));
        assertTrue(json.contains("\"description\":\"Мощная дрель\""));
        assertTrue(json.contains("\"available\":true"));
        assertTrue(json.contains("\"ownerId\":1"));
        assertTrue(json.contains("\"lastBooking\":"));
        assertTrue(json.contains("\"nextBooking\":"));
        assertTrue(json.contains("\"comments\":"));
    }

    @Test
    void deserialize_ShouldDeserializeCorrectly() throws JsonProcessingException {
        String json = """
                {
                    "id": 1,
                    "name": "Дрель",
                    "description": "Мощная дрель",
                    "available": true,
                    "ownerId": 1,
                    "requestId": null,
                    "lastBooking": {
                        "id": 1,
                        "start": "2024-01-10T10:00:00",
                        "end": "2024-01-12T10:00:00",
                        "status": "APPROVED",
                        "booker": {
                            "id": 2,
                            "name": "Last Booker",
                            "email": "last@email.com"
                        }
                    },
                    "nextBooking": {
                        "id": 2,
                        "start": "2024-01-20T10:00:00",
                        "end": "2024-01-22T10:00:00",
                        "status": "WAITING",
                        "booker": {
                            "id": 3,
                            "name": "Next Booker",
                            "email": "next@email.com"
                        }
                    },
                    "comments": [
                        {
                            "id": 1,
                            "text": "Отличная вещь!",
                            "authorName": "Commenter1",
                            "created": "2024-01-13T10:00:00"
                        },
                        {
                            "id": 2,
                            "text": "Рекомендую!",
                            "authorName": "Commenter2",
                            "created": "2024-01-14T10:00:00"
                        }
                    ]
                }""";

        ItemResponesDto result = objectMapper.readValue(json, ItemResponesDto.class);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Дрель", result.getName());
        assertEquals("Мощная дрель", result.getDescription());
        assertTrue(result.getAvailable());
        assertEquals(1L, result.getOwnerId());
        assertNull(result.getRequestId());

        assertNotNull(result.getLastBooking());
        assertEquals(1L, result.getLastBooking().getId());
        assertEquals(LocalDateTime.of(2024, 1, 10, 10, 0), result.getLastBooking().getStart());
        assertEquals(Booking.BookingStatus.APPROVED, result.getLastBooking().getStatus());
        assertNotNull(result.getLastBooking().getBooker());
        assertEquals(2L, result.getLastBooking().getBooker().getId());

        assertNotNull(result.getNextBooking());
        assertEquals(2L, result.getNextBooking().getId());
        assertEquals(LocalDateTime.of(2024, 1, 20, 10, 0), result.getNextBooking().getStart());
        assertEquals(Booking.BookingStatus.WAITING, result.getNextBooking().getStatus());
        assertNotNull(result.getNextBooking().getBooker());
        assertEquals(3L, result.getNextBooking().getBooker().getId());

        assertNotNull(result.getComments());
        assertEquals(2, result.getComments().size());
        assertEquals("Отличная вещь!", result.getComments().get(0).getText());
        assertEquals("Рекомендую!", result.getComments().get(1).getText());
    }

    @Test
    void deserialize_ShouldHandleNullValues() throws JsonProcessingException {
        String json = """
                {
                    "id": null,
                    "name": null,
                    "description": null,
                    "available": null,
                    "ownerId": null,
                    "requestId": null,
                    "lastBooking": null,
                    "nextBooking": null,
                    "comments": null
                }""";

        ItemResponesDto result = objectMapper.readValue(json, ItemResponesDto.class);

        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getName());
        assertNull(result.getDescription());
        assertNull(result.getAvailable());
        assertNull(result.getOwnerId());
        assertNull(result.getRequestId());
        assertNull(result.getLastBooking());
        assertNull(result.getNextBooking());
        assertNull(result.getComments());
    }

    @Test
    void deserialize_ShouldHandleEmptyComments() throws JsonProcessingException {
        String json = """
                {
                    "id": 1,
                    "name": "Дрель",
                    "description": "Мощная дрель",
                    "available": true,
                    "ownerId": 1,
                    "comments": []
                }""";

        ItemResponesDto result = objectMapper.readValue(json, ItemResponesDto.class);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Дрель", result.getName());
        assertNotNull(result.getComments());
        assertTrue(result.getComments().isEmpty());
    }

    @Test
    void roundTrip_ShouldPreserveData() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(itemResponesDto);
        ItemResponesDto result = objectMapper.readValue(json, ItemResponesDto.class);

        assertEquals(itemResponesDto.getId(), result.getId());
        assertEquals(itemResponesDto.getName(), result.getName());
        assertEquals(itemResponesDto.getDescription(), result.getDescription());
        assertEquals(itemResponesDto.getAvailable(), result.getAvailable());
        assertEquals(itemResponesDto.getOwnerId(), result.getOwnerId());
        assertEquals(itemResponesDto.getRequestId(), result.getRequestId());

        assertNotNull(result.getLastBooking());
        assertEquals(itemResponesDto.getLastBooking().getId(), result.getLastBooking().getId());
        assertNotNull(result.getNextBooking());
        assertEquals(itemResponesDto.getNextBooking().getId(), result.getNextBooking().getId());
        assertNotNull(result.getComments());
        assertEquals(itemResponesDto.getComments().size(), result.getComments().size());
    }
}