package ru.practicum.shareit.exception.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.respones.ErrorResponse;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("handleNotFoundException: должен обрабатывать NotFoundException")
    void handleNotFoundException_shouldHandleNotFoundException() {
        var exception = new NotFoundException("Пользователь не найден");
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());
        ResponseEntity<ErrorResponse> response = handler.handleNotFoundException(exception, request);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Not Found");
        assertThat(response.getBody().getMessage()).isEqualTo("Пользователь не найден");
        assertThat(response.getBody().getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("handleBadRequestException: должен обрабатывать BadRequestException")
    void handleBadRequestException_shouldHandleBadRequestException() {
        var exception = new BadRequestException("Неверные параметры запроса");
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());
        ResponseEntity<ErrorResponse> response = handler.handleBadRequestException(exception, request);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Bad Request");
        assertThat(response.getBody().getMessage()).isEqualTo("Неверные параметры запроса");
        assertThat(response.getBody().getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("handleAllExceptions: должен обрабатывать ForbiddenException")
    void handleAllExceptions_shouldHandleForbiddenException() {
        var exception = new ForbiddenException("Доступ запрещен");
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());
        ResponseEntity<ErrorResponse> response = handler.handleAllExceptions(exception, request);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Internal Server Error");
        assertThat(response.getBody().getMessage()).isEqualTo("Произошла непредвиденная ошибка");
        assertThat(response.getBody().getStatus()).isEqualTo(500);
    }

    @Test
    @DisplayName("handleAllExceptions: должен обрабатывать IllegalArgumentException")
    void handleAllExceptions_shouldHandleIllegalArgumentException() {
        var exception = new IllegalArgumentException("Неверный аргумент");
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());
        ResponseEntity<ErrorResponse> response = handler.handleAllExceptions(exception, request);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Internal Server Error");
        assertThat(response.getBody().getMessage()).isEqualTo("Произошла непредвиденная ошибка");
        assertThat(response.getBody().getStatus()).isEqualTo(500);
    }

    @Test
    @DisplayName("handleAllExceptions: должен обрабатывать общие исключения")
    void handleAllExceptions_shouldHandleGeneralException() {
        var exception = new RuntimeException("Внутренняя ошибка сервера");
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());
        ResponseEntity<ErrorResponse> response = handler.handleAllExceptions(exception, request);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Internal Server Error");
        assertThat(response.getBody().getMessage()).isEqualTo("Произошла непредвиденная ошибка");
        assertThat(response.getBody().getStatus()).isEqualTo(500);
    }

    @Test
    @DisplayName("handleAllExceptions: должен обрабатывать исключения с пустым сообщением")
    void handleAllExceptions_shouldHandleExceptionWithEmptyMessage() {
        var exception = new RuntimeException("");
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());
        ResponseEntity<ErrorResponse> response = handler.handleAllExceptions(exception, request);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Internal Server Error");
        assertThat(response.getBody().getMessage()).isEqualTo("Произошла непредвиденная ошибка");
        assertThat(response.getBody().getStatus()).isEqualTo(500);
    }

    @Test
    @DisplayName("handleAllExceptions: должен обрабатывать исключения с null сообщением")
    void handleAllExceptions_shouldHandleExceptionWithNullMessage() {
        var exception = new RuntimeException((String) null);
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());
        ResponseEntity<ErrorResponse> response = handler.handleAllExceptions(exception, request);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Internal Server Error");
        assertThat(response.getBody().getMessage()).isEqualTo("Произошла непредвиденная ошибка");
        assertThat(response.getBody().getStatus()).isEqualTo(500);
    }

    @Test
    @DisplayName("handleNotFoundException: должен обрабатывать исключения с пустым сообщением")
    void handleNotFoundException_shouldHandleExceptionWithEmptyMessage() {
        var exception = new NotFoundException("");
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());
        ResponseEntity<ErrorResponse> response = handler.handleNotFoundException(exception, request);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Not Found");
        assertThat(response.getBody().getMessage()).isEqualTo("");
        assertThat(response.getBody().getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("handleBadRequestException: должен обрабатывать исключения с пустым сообщением")
    void handleBadRequestException_shouldHandleExceptionWithEmptyMessage() {
        var exception = new BadRequestException("");
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());
        ResponseEntity<ErrorResponse> response = handler.handleBadRequestException(exception, request);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Bad Request");
        assertThat(response.getBody().getMessage()).isEqualTo("");
        assertThat(response.getBody().getStatus()).isEqualTo(400);
    }
}