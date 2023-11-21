package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.dto.BookingDtoOut;

import java.util.List;
import java.util.Set;

public interface BookingService {
    Booking create(Long userId, BookingDtoIn bookingDtoIn);

    Booking setStatusBooking(Long ownerId, Long bookingId, Boolean approved);

    Booking findBookingById(Long userId, Long bookingId);

    List<BookingDtoOut> findBookingByUser(Long ownerId, String state);

    List<Booking> findBookingAllItemsByUser(Long ownerId, String state);
}
