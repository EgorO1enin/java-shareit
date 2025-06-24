package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto addUser(UserRequestDto user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Пользователь с email " + user.getEmail() + " уже существует.");
        }
        User newUser = userMapper.toUser(user);
        return userMapper.toUserResponseDto(userRepository.save(newUser));
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponseDto)
                .toList();
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        return userMapper.toUserResponseDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден")));
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
        if (userUpdateDto.getName() != null) {
            user.setName(userUpdateDto.getName());
        }
        if (userUpdateDto.getEmail() != null) {
            if (!userUpdateDto.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(userUpdateDto.getEmail())) {
                throw new IllegalArgumentException("Пользователь с email " + userUpdateDto.getEmail() + " уже существует.");
            }
            user.setEmail(userUpdateDto.getEmail());
        }
        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        userRepository.deleteById(id);
    }
}
