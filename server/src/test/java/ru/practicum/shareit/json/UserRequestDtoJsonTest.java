package ru.practicum.shareit.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserRequestDto;

import static org.junit.jupiter.api.Assertions.*;

class UserRequestDtoJsonTest {

    private ObjectMapper objectMapper;
    private UserRequestDto userRequestDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        userRequestDto = new UserRequestDto();
        userRequestDto.setName("Test User");
        userRequestDto.setEmail("test@example.com");
    }

    @Test
    void serialize_ShouldSerializeCorrectly() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(userRequestDto);
        assertNotNull(json);
        assertTrue(json.contains("\"name\":\"Test User\""));
        assertTrue(json.contains("\"email\":\"test@example.com\""));
    }

    @Test
    void deserialize_ShouldDeserializeCorrectly() throws JsonProcessingException {
        String json = """
                {
                    "name": "Test User",
                    "email": "test@example.com"
                }
                """;
        UserRequestDto result = objectMapper.readValue(json, UserRequestDto.class);
        assertNotNull(result);
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void deserialize_ShouldHandleNullValues() throws JsonProcessingException {
        String json = """
                {
                    "name": null,
                    "email": null
                }
                """;
        UserRequestDto result = objectMapper.readValue(json, UserRequestDto.class);
        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getEmail());
    }

    @Test
    void deserialize_ShouldHandleMissingFields() throws JsonProcessingException {
        String json = "{}";
        UserRequestDto result = objectMapper.readValue(json, UserRequestDto.class);
        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getEmail());
    }

    @Test
    void roundTrip_ShouldPreserveData() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(userRequestDto);
        UserRequestDto result = objectMapper.readValue(json, UserRequestDto.class);
        assertEquals(userRequestDto.getName(), result.getName());
        assertEquals(userRequestDto.getEmail(), result.getEmail());
    }

    @Test
    void serialize_ShouldHandleNullValues() throws JsonProcessingException {
        userRequestDto.setName(null);
        userRequestDto.setEmail(null);
        String json = objectMapper.writeValueAsString(userRequestDto);
        assertNotNull(json);
        assertTrue(json.contains("\"name\":null"));
        assertTrue(json.contains("\"email\":null"));
    }

    @Test
    void deserialize_ShouldHandleEmptyStrings() throws JsonProcessingException {

        String json = """
                {
                    "name": "",
                    "email": ""
                }
                """;


        UserRequestDto result = objectMapper.readValue(json, UserRequestDto.class);


        assertNotNull(result);
        assertEquals("", result.getName());
        assertEquals("", result.getEmail());
    }

    @Test
    void deserialize_ShouldHandleUnicodeCharacters() throws JsonProcessingException {
        String json = """
                {
                    "name": "Тестовый Пользователь",
                    "email": "тест@пример.рф"
                }
                """;
        UserRequestDto result = objectMapper.readValue(json, UserRequestDto.class);
        assertNotNull(result);
        assertEquals("Тестовый Пользователь", result.getName());
        assertEquals("тест@пример.рф", result.getEmail());
    }

    @Test
    void serialize_ShouldHandleEmptyStrings() throws JsonProcessingException {
        userRequestDto.setName("");
        userRequestDto.setEmail("");
        String json = objectMapper.writeValueAsString(userRequestDto);
        assertNotNull(json);
        assertTrue(json.contains("\"name\":\"\""));
        assertTrue(json.contains("\"email\":\"\""));
    }
}