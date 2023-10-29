package ru.practicum.shareit.user.storage.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.*;

@Repository
public class UserInMemoryStorage implements UserStorage {
    private List<User> users = new ArrayList<>();
    private long lastId = 0;

    @Override
    public User create(User user) {
        user.setId(getId());
        users.add(user);
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
        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .get();
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public void deleteById(long id) {
        users.removeIf(u -> u.getId() == id);
    }

    private long getId() {
// хочется оставить данный код, как пример, на будущее
        /*long lastId = users.stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);*/

        return ++lastId;
    }
}
