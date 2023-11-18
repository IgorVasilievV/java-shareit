package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingForItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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

    public static Booking toBooking(BookingDto bookingDto, Item itemFromDto, User user, String status) {
        return Booking.builder()
                .item(itemFromDto)
                .booker(user)
                .status(status)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .build();
    }

}
