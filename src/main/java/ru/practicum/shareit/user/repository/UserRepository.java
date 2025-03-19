package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long nextId = 1L;
    private List<String> mailes = new ArrayList<>();

    public List<String> getMailes() {
        for (User user : users.values()) {
            mailes.add(user.getEmail());
        }
        return mailes;
    }

    public User addUser(User user) {
        user.setId(nextId++);
        users.put(user.getId(), user);
        return user;
    }

    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }

    public User getUserById(Long id) {
        return users.get(id);
    }

    public User updateUser(Long id, User user) {
        users.put(id, user);
        return users.get(id);
    }

    public void deleteUserById(Long id) {
        users.remove(id);
    }
}
