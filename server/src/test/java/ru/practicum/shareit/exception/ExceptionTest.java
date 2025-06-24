package ru.practicum.shareit.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ExceptionTest {

    @Test
    @DisplayName("NotFoundException: должен создаваться с сообщением")
    void notFoundException_shouldCreateWithMessage() {
        var exception = new NotFoundException("Пользователь не найден");
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Пользователь не найден");
    }

    @Test
    @DisplayName("NotFoundException: должен создаваться с пустым сообщением")
    void notFoundException_shouldCreateWithEmptyMessage() {
        var exception = new NotFoundException("");
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("");
    }

    @Test
    @DisplayName("NotFoundException: должен создаваться с null сообщением")
    void notFoundException_shouldCreateWithNullMessage() {
        var exception = new NotFoundException(null);
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNull();
    }

    @Test
    @DisplayName("BadRequestException: должен создаваться с сообщением")
    void badRequestException_shouldCreateWithMessage() {
        var exception = new BadRequestException("Неверные параметры запроса");
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Неверные параметры запроса");
    }

    @Test
    @DisplayName("BadRequestException: должен создаваться с пустым сообщением")
    void badRequestException_shouldCreateWithEmptyMessage() {
        var exception = new BadRequestException("");
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("");
    }

    @Test
    @DisplayName("BadRequestException: должен создаваться с null сообщением")
    void badRequestException_shouldCreateWithNullMessage() {
        var exception = new BadRequestException(null);
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNull();
    }

    @Test
    @DisplayName("ForbiddenException: должен создаваться с сообщением")
    void forbiddenException_shouldCreateWithMessage() {
        var exception = new ForbiddenException("Доступ запрещен");
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Доступ запрещен");
    }

    @Test
    @DisplayName("ForbiddenException: должен создаваться с пустым сообщением")
    void forbiddenException_shouldCreateWithEmptyMessage() {
        var exception = new ForbiddenException("");
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("");
    }

    @Test
    @DisplayName("ForbiddenException: должен создаваться с null сообщением")
    void forbiddenException_shouldCreateWithNullMessage() {
        var exception = new ForbiddenException(null);
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNull();
    }

    @Test
    @DisplayName("NotFoundException: должен наследоваться от RuntimeException")
    void notFoundException_shouldExtendRuntimeException() {
        var exception = new NotFoundException("Тест");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("BadRequestException: должен наследоваться от RuntimeException")
    void badRequestException_shouldExtendRuntimeException() {
        var exception = new BadRequestException("Тест");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("ForbiddenException: должен наследоваться от RuntimeException")
    void forbiddenException_shouldExtendRuntimeException() {
        var exception = new ForbiddenException("Тест");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}