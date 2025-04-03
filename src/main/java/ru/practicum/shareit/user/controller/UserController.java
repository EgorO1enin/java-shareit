package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestModel;
import ru.practicum.shareit.user.dto.UserResponesModel;
import ru.practicum.shareit.user.dto.UserUpdateModel;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponesModel addUser(@Valid @RequestBody final UserRequestModel user) {
        return userService.addUser(user);
    }

    @GetMapping
    public List<UserResponesModel> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserResponesModel getUser(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PatchMapping("/{userId}")
    public UserResponesModel updateUser(@PathVariable Long userId,
                                        @Valid @RequestBody final UserUpdateModel user) {
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
