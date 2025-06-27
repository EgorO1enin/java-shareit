package ru.practicum.shareit.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Tag(name = "Пользователи", description = "Управление пользователями")
@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @Operation(summary = "Создать пользователя")
    @PostMapping
    public ResponseEntity<Object> addUser(
            @Parameter(description = "Данные пользователя", required = true) @RequestBody @Valid UserRequestDto userRequestDto) {
        log.info("Creating user {}", userRequestDto);
        return userClient.addUser(userRequestDto);
    }

    @Operation(summary = "Получить всех пользователей")
    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Get all users");
        return userClient.getAllUsers();
    }

    @Operation(summary = "Получить пользователя по ID")
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long userId) {
        log.info("Get user {}", userId);
        return userClient.getUser(userId);
    }

    @Operation(summary = "Обновить пользователя")
    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long userId,
            @Parameter(description = "Данные для обновления пользователя", required = true) @RequestBody @Valid UserUpdateDto userUpdateDto) {
        log.info("Update user {}: {}", userId, userUpdateDto);
        return userClient.updateUser(userId, userUpdateDto);
    }

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long userId) {
        log.info("Delete user {}", userId);
        return userClient.deleteUser(userId);
    }
}