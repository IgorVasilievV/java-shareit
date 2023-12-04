package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.request.service.impl.ValidationItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.storage.UserDbStorage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationItemRequestServiceTest {

    @Mock
    private UserDbStorage userStorage;

    @Mock
    private ItemRequestStorage itemRequestStorage;

    @InjectMocks
    ValidationItemRequestServiceImpl validationItemRequestService;

    @Test
    void validateBeforeCreate_shouldReturnExceptionWhenUserNotExist() {
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> validationItemRequestService.validateBeforeCreate(1L));
    }

    @Test
    void validateBeforeSearchById_shouldReturnExceptionWhenUserNotExist() {
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> validationItemRequestService.validateBeforeSearchById(1L, 1L));
    }

    @Test
    void validateBeforeSearchById_shouldReturnExceptionWhenRequestNotExist() {
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(itemRequestStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> validationItemRequestService.validateBeforeSearchById(1L, 1L));
    }

    @Test
    void validatePagination_shouldReturnException() {
        assertThrows(ValidationException.class,
                () -> validationItemRequestService.validatePagination(-1, 2));

    }
}