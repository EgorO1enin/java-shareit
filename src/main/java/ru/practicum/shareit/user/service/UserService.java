package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserRequestModel;
import ru.practicum.shareit.user.dto.UserResponesModel;
import ru.practicum.shareit.user.dto.UserUpdateModel;

import java.util.List;

@Service
public interface UserService {
    UserResponesModel addUser(UserRequestModel user);

    List<UserResponesModel> getAllUsers();

    UserResponesModel getUserById(Long id);

    UserResponesModel updateUser(Long id, UserUpdateModel userRequest);

    void deleteUser(Long id);
}
