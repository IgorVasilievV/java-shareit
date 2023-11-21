package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.dto.BookingDtoIn;

public interface BookingValidationService {
    void validateBeforeCreate(Long userId, BookingDtoIn bookingDtoIn);

    void validateBeforeSetStatus(Long ownerId, Long bookingId);

    void validateBeforeSearchById(Long userId, Long bookingId);

    void validateBeforeSearchByUserId(Long userId, String state);
}
