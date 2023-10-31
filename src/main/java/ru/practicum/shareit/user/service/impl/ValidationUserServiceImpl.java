package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.ConflictException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.ValidationUserService;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ValidationUserServiceImpl implements ValidationUserService {
    private final UserStorage userStorage;

    @Override
    public void validateBeforeCreate(UserDto userDto) throws ValidationException {
        validateEmailOnNull(userDto);
        validateEmailOnCorrect(userDto);
        validateEmailOnConflict(-1, userDto);
    }

    @Override
    public void validateBeforeUpdate(long id, UserDto userDto) throws NotFoundException, ValidationException {
        validateSearch(id);
        validateEmailOnCorrect(userDto);
        validateEmailOnConflict(id, userDto);
    }

    @Override
    public void validateSearch(long id) throws NotFoundException {
        if (userStorage.getAll().stream().noneMatch(u -> u.getId() == id)) {
            throw new NotFoundException("User with id = " + id + " not found");
        }
    }

    private void validateEmailOnNull(UserDto userDto) throws ValidationException {
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new ValidationException("User don't have email");
        }
    }

    private void validateEmailOnCorrect(UserDto userDto) {
        if (userDto.getEmail() != null && !Pattern.matches("^(\\S+)(@)(\\S+)$", userDto.getEmail())) {
            throw new ValidationException("Incorrect format email");
        }
    }


    private void validateEmailOnConflict(long id, UserDto userDto) {
        if (userStorage.getAll().stream()
                .filter(u -> u.getId() != id)
                .anyMatch(u -> u.getEmail().equals(userDto.getEmail()))) {
            throw new ConflictException("User with email: " + userDto.getEmail() + " already exist");
        }
    }
}
