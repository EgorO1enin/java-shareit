package ru.practicum.shareit.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("addUser: должен создавать нового пользователя")
    void addUser_shouldCreateNewUser() {
        var userRequest = new UserRequestDto("John Doe", "john@example.com");
        UserResponseDto user = userService.addUser(userRequest);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("addUser: должен создавать пользователя с уникальным email")
    void addUser_shouldCreateUserWithUniqueEmail() {
        var userRequest1 = new UserRequestDto("John Doe", "john@example.com");
        var userRequest2 = new UserRequestDto("Jane Doe", "jane@example.com");
        UserResponseDto user1 = userService.addUser(userRequest1);
        UserResponseDto user2 = userService.addUser(userRequest2);
        assertThat(user1).isNotNull();
        assertThat(user2).isNotNull();
        assertThat(user1.getEmail()).isEqualTo("john@example.com");
        assertThat(user2.getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    @DisplayName("getUserById: должен возвращать пользователя по id")
    void getUserById_shouldReturnUserById() {
        var userRequest = new UserRequestDto("John Doe", "john@example.com");
        var createdUser = userService.addUser(userRequest);
        UserResponseDto user = userService.getUserById(createdUser.getId());
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(createdUser.getId());
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("getAllUsers: должен возвращать всех пользователей")
    void getAllUsers_shouldReturnAllUsers() {
        var userRequest1 = new UserRequestDto("John Doe", "john@example.com");
        var userRequest2 = new UserRequestDto("Jane Doe", "jane@example.com");
        userService.addUser(userRequest1);
        userService.addUser(userRequest2);
        List<UserResponseDto> users = userService.getAllUsers();
        assertThat(users).isNotEmpty();
        assertThat(users).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("updateUser: должен обновлять пользователя")
    void updateUser_shouldUpdateUser() {
        var userRequest = new UserRequestDto("John Doe", "john@example.com");
        var createdUser = userService.addUser(userRequest);
        var updateRequest = new UserUpdateDto();
        updateRequest.setName("John Updated");
        updateRequest.setEmail("john.updated@example.com");
        UserResponseDto updatedUser = userService.updateUser(createdUser.getId(), updateRequest);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(createdUser.getId());
        assertThat(updatedUser.getName()).isEqualTo("John Updated");
        assertThat(updatedUser.getEmail()).isEqualTo("john.updated@example.com");
    }

    @Test
    @DisplayName("updateUser: должен обновлять только имя пользователя")
    void updateUser_shouldUpdateOnlyName() {
        var userRequest = new UserRequestDto("John Doe", "john@example.com");
        var createdUser = userService.addUser(userRequest);
        var updateRequest = new UserUpdateDto();
        updateRequest.setName("John Updated");
        UserResponseDto updatedUser = userService.updateUser(createdUser.getId(), updateRequest);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(createdUser.getId());
        assertThat(updatedUser.getName()).isEqualTo("John Updated");
        assertThat(updatedUser.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("updateUser: должен обновлять только email пользователя")
    void updateUser_shouldUpdateOnlyEmail() {
        var userRequest = new UserRequestDto("John Doe", "john@example.com");
        var createdUser = userService.addUser(userRequest);
        var updateRequest = new UserUpdateDto();
        updateRequest.setEmail("john.updated@example.com");
        UserResponseDto updatedUser = userService.updateUser(createdUser.getId(), updateRequest);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(createdUser.getId());
        assertThat(updatedUser.getName()).isEqualTo("John Doe");
        assertThat(updatedUser.getEmail()).isEqualTo("john.updated@example.com");
    }

    @Test
    @DisplayName("deleteUser: должен удалять пользователя")
    void deleteUser_shouldDeleteUser() {
        var userRequest = new UserRequestDto("John Doe", "john@example.com");
        var createdUser = userService.addUser(userRequest);
        userService.deleteUser(createdUser.getId());
        var allUsers = userService.getAllUsers();
        assertThat(allUsers).noneMatch(user -> user.getId().equals(createdUser.getId()));
    }

    @Test
    @DisplayName("getUserById: должен возвращать пользователя после обновления")
    void getUserById_shouldReturnUpdatedUser() {
        var userRequest = new UserRequestDto("John Doe", "john@example.com");
        var createdUser = userService.addUser(userRequest);
        var updateRequest = new UserUpdateDto();
        updateRequest.setName("John Updated");
        updateRequest.setEmail("john.updated@example.com");
        userService.updateUser(createdUser.getId(), updateRequest);
        UserResponseDto user = userService.getUserById(createdUser.getId());
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(createdUser.getId());
        assertThat(user.getName()).isEqualTo("John Updated");
        assertThat(user.getEmail()).isEqualTo("john.updated@example.com");
    }

    @Test
    @DisplayName("getAllUsers: должен возвращать пустой список после удаления всех пользователей")
    void getAllUsers_shouldReturnEmptyListAfterDeletingAllUsers() {
        var userRequest1 = new UserRequestDto("John Doe", "john@example.com");
        var userRequest2 = new UserRequestDto("Jane Doe", "jane@example.com");
        var user1 = userService.addUser(userRequest1);
        var user2 = userService.addUser(userRequest2);
        userService.deleteUser(user1.getId());
        userService.deleteUser(user2.getId());
        var users = userService.getAllUsers();
        assertThat(users).noneMatch(user -> user.getId().equals(user1.getId()) || user.getId().equals(user2.getId()));
    }
}