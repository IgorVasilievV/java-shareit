package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.dto.UserDto;

public interface ValidationUserService {
    void validateBeforeCreate(UserDto userDto);

    void validateBeforeUpdate(long id, UserDto userDto);

    void validateSearch(long id);

}
