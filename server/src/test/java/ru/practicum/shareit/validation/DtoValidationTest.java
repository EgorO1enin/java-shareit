package ru.practicum.shareit.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void userRequestDto_ShouldBeValid_WhenValidData() {
        UserRequestDto dto = new UserRequestDto();
        dto.setName("Test User");
        dto.setEmail("test@example.com");
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void userRequestDto_ShouldBeInvalid_WhenNameIsNull() {
        UserRequestDto dto = new UserRequestDto();
        dto.setName(null);
        dto.setEmail("test@example.com");
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void userRequestDto_ShouldBeInvalid_WhenEmailIsNull() {
        UserRequestDto dto = new UserRequestDto();
        dto.setName("Test User");
        dto.setEmail(null);
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void userRequestDto_ShouldBeInvalid_WhenEmailIsInvalid() {
        UserRequestDto dto = new UserRequestDto();
        dto.setName("Test User");
        dto.setEmail("invalid-email");
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void userUpdateDto_ShouldBeValid_WhenValidData() {
        UserUpdateDto dto = new UserUpdateDto();
        dto.setName("Updated User");
        dto.setEmail("updated@example.com");
        Set<ConstraintViolation<UserUpdateDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void itemRequestDto_ShouldBeValid_WhenValidData() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setName("Test Item");
        dto.setDescription("Test Description");
        dto.setAvailable(true);
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void itemRequestDto_ShouldBeInvalid_WhenNameIsNull() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setName(null);
        dto.setDescription("Test Description");
        dto.setAvailable(true);
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void itemRequestDto_ShouldBeInvalid_WhenNameIsEmpty() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setName("");
        dto.setDescription("Test Description");
        dto.setAvailable(true);
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void itemRequestDto_ShouldBeInvalid_WhenDescriptionIsNull() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setName("Test Item");
        dto.setDescription(null);
        dto.setAvailable(true);
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void itemRequestDto_ShouldBeInvalid_WhenDescriptionIsEmpty() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setName("Test Item");
        dto.setDescription("");
        dto.setAvailable(true);
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void itemRequestDto_ShouldBeInvalid_WhenAvailableIsNull() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setName("Test Item");
        dto.setDescription("Test Description");
        dto.setAvailable(null);
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("available")));
    }

    @Test
    void bookingRequestDto_ShouldBeValid_WhenValidData() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));
        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void bookingRequestDto_ShouldBeInvalid_WhenItemIdIsNull() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(null);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));
        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("itemId")));
    }

    @Test
    void bookingRequestDto_ShouldBeInvalid_WhenStartIsNull() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(null);
        dto.setEnd(LocalDateTime.now().plusDays(2));
        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("start")));
    }

    @Test
    void bookingRequestDto_ShouldBeInvalid_WhenEndIsNull() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(null);
        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("end")));
    }

    @Test
    void itemRequestRequestDto_ShouldBeValid_WhenValidData() {

        ItemRequestRequestDto dto = new ItemRequestRequestDto();
        dto.setDescription("Test Request Description");
        Set<ConstraintViolation<ItemRequestRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void itemRequestRequestDto_ShouldBeInvalid_WhenDescriptionIsNull() {
        ItemRequestRequestDto dto = new ItemRequestRequestDto();
        dto.setDescription(null);
        Set<ConstraintViolation<ItemRequestRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void itemRequestRequestDto_ShouldBeInvalid_WhenDescriptionIsEmpty() {
        ItemRequestRequestDto dto = new ItemRequestRequestDto();
        dto.setDescription("");
        Set<ConstraintViolation<ItemRequestRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }
}