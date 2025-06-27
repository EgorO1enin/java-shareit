package ru.practicum.shareit.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.respones.ErrorResponse;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseJsonTest {

    private ObjectMapper objectMapper;
    private ErrorResponse errorResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        errorResponse = new ErrorResponse();
        errorResponse.setError("Ошибка валидации");
        errorResponse.setMessage("Поле name не может быть пустым");
        errorResponse.setStatus(400);
        errorResponse.setTimestamp(LocalDateTime.of(2024, 1, 15, 10, 30, 0));
    }

    @Test
    void serialize_ShouldSerializeCorrectly() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(errorResponse);
        assertNotNull(json);
        assertTrue(json.contains("\"error\":\"Ошибка валидации\""));
        assertTrue(json.contains("\"message\":\"Поле name не может быть пустым\""));
        assertTrue(json.contains("\"status\":400"));
        assertTrue(json.contains("\"timestamp\":"));
    }

    @Test
    void deserialize_ShouldDeserializeCorrectly() throws JsonProcessingException {
        String json = "{\n" +
                "    \"error\": \"Ошибка валидации\",\n" +
                "    \"message\": \"Поле name не может быть пустым\",\n" +
                "    \"status\": 400,\n" +
                "    \"timestamp\": \"2024-01-15T10:30:00\"\n" +
                "}";

        ErrorResponse result = objectMapper.readValue(json, ErrorResponse.class);

        assertNotNull(result);
        assertEquals("Ошибка валидации", result.getError());
        assertEquals("Поле name не может быть пустым", result.getMessage());
        assertEquals(400, result.getStatus());
        assertNotNull(result.getTimestamp());
        assertEquals(LocalDateTime.of(2024, 1, 15, 10, 30, 0), result.getTimestamp());
    }

    @Test
    void deserialize_ShouldHandleNullValues() throws JsonProcessingException {
        String json = "{\n" +
                "    \"error\": null,\n" +
                "    \"message\": null,\n" +
                "    \"status\": 0,\n" +
                "    \"timestamp\": null\n" +
                "}";

        ErrorResponse result = objectMapper.readValue(json, ErrorResponse.class);

        assertNotNull(result);
        assertNull(result.getError());
        assertNull(result.getMessage());
        assertEquals(0, result.getStatus());
        assertNull(result.getTimestamp());
    }

    @Test
    void deserialize_ShouldHandleMissingFields() throws JsonProcessingException {
        String json = "{}";

        ErrorResponse result = objectMapper.readValue(json, ErrorResponse.class);

        assertNotNull(result);
        assertNull(result.getError());
        assertNull(result.getMessage());
        assertEquals(0, result.getStatus());
        assertNull(result.getTimestamp());
    }

    @Test
    void deserialize_ShouldHandleDifferentErrorTypes() throws JsonProcessingException {
        String json = "{\n" +
                "    \"error\": \"Not Found\",\n" +
                "    \"message\": \"Пользователь с id 999 не найден\",\n" +
                "    \"status\": 404,\n" +
                "    \"timestamp\": \"2024-01-15T10:30:00\"\n" +
                "}";

        ErrorResponse result = objectMapper.readValue(json, ErrorResponse.class);

        assertNotNull(result);
        assertEquals("Not Found", result.getError());
        assertEquals("Пользователь с id 999 не найден", result.getMessage());
        assertEquals(404, result.getStatus());
        assertEquals(LocalDateTime.of(2024, 1, 15, 10, 30, 0), result.getTimestamp());
    }

    @Test
    void deserialize_ShouldHandleServerError() throws JsonProcessingException {
        String json = "{\n" +
                "    \"error\": \"Internal Server Error\",\n" +
                "    \"message\": \"Произошла внутренняя ошибка сервера\",\n" +
                "    \"status\": 500,\n" +
                "    \"timestamp\": \"2024-01-15T10:30:00\"\n" +
                "}";

        ErrorResponse result = objectMapper.readValue(json, ErrorResponse.class);

        assertNotNull(result);
        assertEquals("Internal Server Error", result.getError());
        assertEquals("Произошла внутренняя ошибка сервера", result.getMessage());
        assertEquals(500, result.getStatus());
        assertEquals(LocalDateTime.of(2024, 1, 15, 10, 30, 0), result.getTimestamp());
    }

    @Test
    void roundTrip_ShouldPreserveData() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(errorResponse);
        ErrorResponse result = objectMapper.readValue(json, ErrorResponse.class);

        assertEquals(errorResponse.getError(), result.getError());
        assertEquals(errorResponse.getMessage(), result.getMessage());
        assertEquals(errorResponse.getStatus(), result.getStatus());
        assertNotNull(result.getTimestamp());
    }

    @Test
    void serialize_ShouldHandleEmptyErrorResponse() throws JsonProcessingException {
        ErrorResponse emptyResponse = new ErrorResponse();
        String json = objectMapper.writeValueAsString(emptyResponse);
        assertNotNull(json);
        assertTrue(json.contains("{}") || json.contains("null"));
    }
}