package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

@Service
public interface UserService {
    UserResponseDto addUser(UserRequestDto user);

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long id);

    UserResponseDto updateUser(Long id, UserUpdateDto userRequest);

    void deleteUser(Long id);
}
