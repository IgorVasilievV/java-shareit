package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;

public interface BookingValidationService {
    void validateBeforeCreate(Long userId, BookingDto bookingDto);

    void validateBeforeSetStatus(Long ownerId, Long bookingId);

    void validateBeforeSearchById(Long userId, Long bookingId);

    void validateBeforeSearchByUserId(Long userId, String state);
}
