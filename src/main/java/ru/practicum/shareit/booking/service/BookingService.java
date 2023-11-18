package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;

import java.util.List;

public interface BookingService {
    Booking create(Long userId, BookingDto bookingDto);

    Booking setStatusBooking(Long ownerId, Long bookingId, Boolean approved);

    Booking findBookingById(Long userId, Long bookingId);

    List<Booking> findBookingByUser(Long ownerId, String state);

    List<Booking> findBookingAllItemsByUser(Long ownerId, String state);
}
