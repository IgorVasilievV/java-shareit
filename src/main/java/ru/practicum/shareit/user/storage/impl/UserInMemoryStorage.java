package ru.practicum.shareit.user.storage.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.*;

@Repository
public class UserInMemoryStorage implements UserStorage {
    private Map<Long, User> users = new HashMap<>();
    private long lastId = 0;

    @Override
    public User create(User user) {
        user.setId(getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(long id, User user) {
        User lastUser = getById(id);
        if (user.getName() != null) {
            lastUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            lastUser.setEmail(user.getEmail());
        }
        return lastUser;
    }

    @Override
    public User getById(long id) {
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(long id) {
        users.remove(id);
    }

    private long getId() {
        return ++lastId;
    }
}
