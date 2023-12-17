package ru.practicum.shareit.request.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.request.service.ValidationItemRequestService;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.storage.UserDbStorage;

@Service
@RequiredArgsConstructor
public class ValidationItemRequestServiceImpl implements ValidationItemRequestService {
    private final UserDbStorage userStorage;
    private final ItemRequestStorage itemRequestStorage;

    @Override
    public void validateBeforeCreate(Long userId) {
        validateContainsUser(userId);
    }

    @Override
    public void validateBeforeSearchById(Long userId, Long requestId) {
        validateContainsUser(userId);
        validateContainsRequest(requestId);
    }

    @Override
    public void validatePagination(Integer from, Integer size) {
        if (from < 0 || size <= 0) {
            throw new ValidationException("Incorrect pagination");
        }
    }

    private void validateContainsRequest(Long requestId) {
        if (!itemRequestStorage.existsById(requestId)) {
            throw new NotFoundException("Unknown request with id = " + requestId);
        }
    }

    private void validateContainsUser(Long userId) {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException("Unknown user with id = " + userId);
        }
    }

}
