package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto update(long id, UserDto userDto);

    UserDto getById(long id);

    List<UserDto> getAll();

    void deleteById(long id);
}
