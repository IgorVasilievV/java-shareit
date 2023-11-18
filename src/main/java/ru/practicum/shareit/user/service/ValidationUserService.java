package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.dto.UserDto;

public interface ValidationUserService {
    void validateBeforeCreate(UserDto userDto);

    void validateBeforeUpdate(Long id, UserDto userDto);

    void validateSearch(Long id);

}
