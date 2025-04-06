package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.converter.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponesDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponesDto addUser(UserRequestDto user) {
        if (userRepository.getMailes().contains(user.getEmail())) {
            throw new IllegalArgumentException("Пользователь с email " + user.getEmail() + " уже существует.");

        }
        User addedUser = userRepository.addUser(userMapper.toUser(user));
        return userMapper.toUserResponesDto(addedUser);
    }

    @Override
    public List<UserResponesDto> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(userMapper::toUserResponesDto)
                .toList();
    }

    @Override
    public UserResponesDto getUserById(Long id) {
        return userMapper.toUserResponesDto(userRepository.getUserById(id));
    }

    @Override
    public UserResponesDto updateUser(Long id, UserUpdateDto userRequest) {
        if (userRepository.getMailes().contains(userRequest.getEmail())) {
            throw new IllegalArgumentException("Пользователь с email " + userRequest.getEmail() + " уже существует.");

        }
        User user = userRepository.getUserById(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с ID " + id + " не найден.");
        }
        if (userRequest.getName() != null) {
            user.setName(userRequest.getName());
        }
        if (userRequest.getEmail() != null) {
            user.setEmail(userRequest.getEmail());
        }
        User updatedUser = userRepository.updateUser(id, user);
        return userMapper.toUserResponesDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteUserById(id);
    }
}
