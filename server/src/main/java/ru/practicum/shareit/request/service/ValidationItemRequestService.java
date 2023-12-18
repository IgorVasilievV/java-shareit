package ru.practicum.shareit.request.service;

public interface ValidationItemRequestService {
    void validateBeforeCreate(Long userId);

    void validateBeforeSearchById(Long userId, Long requestId);

    void validatePagination(Integer from, Integer size);
}
