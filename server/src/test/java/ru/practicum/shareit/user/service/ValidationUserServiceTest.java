package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.impl.ValidationUserServiceDbImpl;
import ru.practicum.shareit.user.storage.UserDbStorage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationUserServiceTest {

    @Mock
    private UserDbStorage userStorage;
    @InjectMocks
    ValidationUserServiceDbImpl validationUserService;

    @Test
    void validateBeforeCreate_shouldReturnExceptionWhenEmailIsBlankOrNull() {
        UserDto userDto = UserDto.builder().build();

        assertThrows(ValidationException.class, () -> validationUserService.validateBeforeCreate(userDto));
    }

    @Test
    void validateBeforeCreate_shouldReturnExceptionWhenEmailIncorrect() {
        UserDto userDto = UserDto.builder().email("email").build();

        assertThrows(ValidationException.class, () -> validationUserService.validateBeforeCreate(userDto));
    }

    @Test
    void validateBeforeUpdate_shouldReturnExceptionWhenUserNotExist() {
        UserDto userDto = UserDto.builder().build();
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> validationUserService.validateBeforeUpdate(1L, userDto));
    }

    @Test
    void validateBeforeUpdate_shouldReturnExceptionWhenEmailIncorrect() {
        UserDto userDto = UserDto.builder().email("email").build();
        when(userStorage.existsById(anyLong())).thenReturn(true);

        assertThrows(ValidationException.class, () -> validationUserService.validateBeforeUpdate(1L, userDto));
    }
}