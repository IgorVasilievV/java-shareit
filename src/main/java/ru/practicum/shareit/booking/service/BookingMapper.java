package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.dto.BookingForItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {

    public static BookingForItemDto toBookingForItemDto(Booking booking) {
        BookingForItemDto bookingForItemDto;
        if (booking != null) {
            bookingForItemDto = BookingForItemDto.builder()
                    .id(booking.getId())
                    .bookerId(booking.getBooker().getId())
                    .start(booking.getStart())
                    .end(booking.getEnd())
                    .build();
        } else {
            bookingForItemDto = null;
        }
        return bookingForItemDto;
    }

    public static Booking toBooking(BookingDtoIn bookingDtoIn, Item itemFromDto, User user, String status) {
        return Booking.builder()
                .item(itemFromDto)
                .booker(user)
                .status(status)
                .start(bookingDtoIn.getStart())
                .end(bookingDtoIn.getEnd())
                .build();
    }

    public static BookingDtoOut toBookingDtoOut(Booking booking) {
        return BookingDtoOut.builder()
                .id(booking.getId())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }

    public static List<BookingDtoOut> bookingDtoOutList(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDtoOut)
                .collect(Collectors.toList());
    }

}
