package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.user.converter.UserRequestToUserConverter;
import ru.practicum.shareit.user.converter.UserToUserResponesConverter;
import ru.practicum.shareit.user.dto.User;
import ru.practicum.shareit.user.dto.UserRequestModel;
import ru.practicum.shareit.user.dto.UserResponesModel;
import ru.practicum.shareit.user.dto.UserUpdateModel;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRequestToUserConverter userRequestToUserConverter;
    private final UserToUserResponesConverter userToUserResponesConverter;

    @Override
    public UserResponesModel addUser(UserRequestModel user) {
        if (userRepository.getMailes().contains(user.getEmail())) {
            throw new IllegalArgumentException("Пользователь с email " + user.getEmail() + " уже существует.");

        }
        User addedUser = userRepository.addUser(userRequestToUserConverter.convert(user));
        return userToUserResponesConverter.convert(addedUser);
    }

    @Override
    public List<UserResponesModel> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(userToUserResponesConverter::convert)
                .toList();
    }

    @Override
    public UserResponesModel getUserById(Long id) {
        return userToUserResponesConverter.convert(userRepository.getUserById(id));
    }

    @Override
    public UserResponesModel updateUser(Long id, UserUpdateModel userRequest) {
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
        return userToUserResponesConverter.convert(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteUserById(id);
    }
}
