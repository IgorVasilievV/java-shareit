package ru.practicum.shareit.user.storage;


import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(long id, User user);

    User getById(long id);

    List<User> getAll();

    void deleteById(long id);
}
