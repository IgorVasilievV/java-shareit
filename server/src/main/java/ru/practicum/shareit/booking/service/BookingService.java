package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {
    BookingDtoOut create(Long userId, BookingDtoIn bookingDtoIn);

    BookingDtoOut setStatusBooking(Long ownerId, Long bookingId, Boolean approved);

    BookingDtoOut findBookingById(Long userId, Long bookingId);

    List<BookingDtoOut> findBookingByUser(Long ownerId, String state, Integer from, Integer size);

    List<BookingDtoOut> findBookingAllItemsByUser(Long ownerId, String state, Integer from, Integer size);
}
