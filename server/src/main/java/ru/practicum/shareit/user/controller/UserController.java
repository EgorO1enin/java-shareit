package ru.practicum.shareit.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponesDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Tag(name = "Управление пользователями",
        description = "API для работы с пользователями сервиса аренды")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Добавить нового пользователя",
            description = "Создает нового пользователя в системе",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь создан"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя")
            })
    public UserResponesDto addUser(
            @Valid
            @RequestBody
            @Parameter(description = "Данные нового пользователя", required = true)
            final UserRequestDto user) {
        return userService.addUser(user);
    }

    @GetMapping
    @Operation(summary = "Получить всех пользователей",
            description = "Возвращает список всех зарегистрированных пользователей",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список пользователей")
            })
    public List<UserResponesDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Получить пользователя по ID",
            description = "Возвращает информацию о пользователе по его идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Информация о пользователе"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            })
    public UserResponesDto getUser(
            @PathVariable
            @Parameter(description = "ID пользователя", required = true, example = "1")
            Long userId) {
        return userService.getUserById(userId);
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "Обновить данные пользователя",
            description = "Обновляет информацию о существующем пользователе",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные пользователя обновлены"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные для обновления"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            })
    public UserResponesDto updateUser(
            @PathVariable
            @Parameter(description = "ID пользователя для обновления", required = true, example = "1")
            Long userId,
            @Valid
            @RequestBody
            @Parameter(description = "Обновленные данные пользователя", required = true)
            final UserUpdateDto user) {
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Удалить пользователя",
            description = "Удаляет пользователя из системы по его идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь удален"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            })
    public void deleteUser(
            @PathVariable
            @Parameter(description = "ID пользователя для удаления", required = true, example = "1")
            Long userId) {
        userService.deleteUser(userId);
    }
}