package ru.practicum.shareit.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponesDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void addUser_ShouldCreateUser() {
        // Arrange
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("Test User");
        userRequestDto.setEmail("test@example.com");

        // Act
        UserResponesDto result = userService.addUser(userRequestDto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange
        UserRequestDto user1 = new UserRequestDto();
        user1.setName("User 1");
        user1.setEmail("user1@example.com");

        UserRequestDto user2 = new UserRequestDto();
        user2.setName("User 2");
        user2.setEmail("user2@example.com");

        userService.addUser(user1);
        userService.addUser(user2);

        // Act
        List<UserResponesDto> users = userService.getAllUsers();

        // Assert
        assertNotNull(users);
        assertTrue(users.size() >= 2);
        assertTrue(users.stream().anyMatch(u -> "User 1".equals(u.getName())));
        assertTrue(users.stream().anyMatch(u -> "User 2".equals(u.getName())));
    }

    @Test
    void getUserById_ShouldReturnUser() {
        // Arrange
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("Test User");
        userRequestDto.setEmail("test@example.com");
        UserResponesDto createdUser = userService.addUser(userRequestDto);

        // Act
        UserResponesDto result = userService.getUserById(createdUser.getId());

        // Assert
        assertNotNull(result);
        assertEquals(createdUser.getId(), result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void updateUser_ShouldUpdateUser() {
        // Arrange
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("Original User");
        userRequestDto.setEmail("original@example.com");
        UserResponesDto createdUser = userService.addUser(userRequestDto);

        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setName("Updated User");
        updateDto.setEmail("updated@example.com");

        // Act
        UserResponesDto result = userService.updateUser(createdUser.getId(), updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(createdUser.getId(), result.getId());
        assertEquals("Updated User", result.getName());
        assertEquals("updated@example.com", result.getEmail());
    }

    @Test
    void updateUser_ShouldUpdateOnlyName() {
        // Arrange
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("Original User");
        userRequestDto.setEmail("original@example.com");
        UserResponesDto createdUser = userService.addUser(userRequestDto);

        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setName("Updated User");
        updateDto.setEmail(null);

        // Act
        UserResponesDto result = userService.updateUser(createdUser.getId(), updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(createdUser.getId(), result.getId());
        assertEquals("Updated User", result.getName());
        assertEquals("original@example.com", result.getEmail()); // email не изменился
    }

    @Test
    void updateUser_ShouldUpdateOnlyEmail() {
        // Arrange
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("Original User");
        userRequestDto.setEmail("original@example.com");
        UserResponesDto createdUser = userService.addUser(userRequestDto);

        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setName(null);
        updateDto.setEmail("updated@example.com");

        // Act
        UserResponesDto result = userService.updateUser(createdUser.getId(), updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(createdUser.getId(), result.getId());
        assertEquals("Original User", result.getName()); // name не изменился
        assertEquals("updated@example.com", result.getEmail());
    }

    @Test
    void addUser_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        UserRequestDto user1 = new UserRequestDto();
        user1.setName("User 1");
        user1.setEmail("same@example.com");

        UserRequestDto user2 = new UserRequestDto();
        user2.setName("User 2");
        user2.setEmail("same@example.com");

        userService.addUser(user1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.addUser(user2));
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        // Arrange
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("Test User");
        userRequestDto.setEmail("test@example.com");
        UserResponesDto createdUser = userService.addUser(userRequestDto);

        // Act
        userService.deleteUser(createdUser.getId());

        // Assert - пользователь должен быть удален
        List<UserResponesDto> users = userService.getAllUsers();
        assertFalse(users.stream().anyMatch(u -> u.getId().equals(createdUser.getId())));
    }
}
