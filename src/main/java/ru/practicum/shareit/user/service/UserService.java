package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponesDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

@Service
public interface UserService {
    UserResponesDto addUser(UserRequestDto user);

    List<UserResponesDto> getAllUsers();

    UserResponesDto getUserById(Long id);

    UserResponesDto updateUser(Long id, UserUpdateDto userRequest);

    void deleteUser(Long id);
}
