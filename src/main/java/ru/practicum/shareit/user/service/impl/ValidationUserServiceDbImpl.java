package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.ValidationUserService;
import ru.practicum.shareit.user.storage.UserDbStorage;

import java.util.regex.Pattern;

@Service("ValidationUserServiceDbImpl")
@RequiredArgsConstructor
public class ValidationUserServiceDbImpl implements ValidationUserService {
    private final UserDbStorage userStorage;

    @Override
    public void validateBeforeCreate(UserDto userDto) throws ValidationException {
        validateEmailOnNull(userDto);
        validateEmailOnCorrect(userDto);
    }

    @Override
    public void validateBeforeUpdate(Long id, UserDto userDto) throws NotFoundException, ValidationException {
        validateSearch(id);
        validateEmailOnCorrect(userDto);
    }

    @Override
    public void validateSearch(Long id) throws NotFoundException {
        if (!userStorage.existsById(id)) {
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
}
