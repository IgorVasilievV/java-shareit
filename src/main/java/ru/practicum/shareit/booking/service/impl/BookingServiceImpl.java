package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.dto.BookingDtoWithoutEntity;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingValidationService;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemDbStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDbStorage;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingStorage bookingStorage;
    private final UserDbStorage userDbStorage;
    private final ItemDbStorage itemDbStorage;
    private final BookingValidationService bookingValidationService;

    @Transactional
    @Override
    public Booking create(Long userId, BookingDtoIn bookingDtoIn) {
        bookingValidationService.validateBeforeCreate(userId, bookingDtoIn);
        Item item = itemDbStorage.findById(bookingDtoIn.getItemId()).get();
        User user = userDbStorage.findById(userId).get();
        Booking booking = BookingMapper.toBooking(bookingDtoIn, item, user, Status.WAITING.toString());
        return bookingStorage.save(booking);
    }

    @Transactional
    @Override
    public Booking setStatusBooking(Long ownerId, Long bookingId, Boolean approved) {
        bookingValidationService.validateBeforeSetStatus(ownerId, bookingId);
        Booking booking = bookingStorage.findById(bookingId).get();
        if (approved) {
            booking.setStatus(Status.APPROVED.toString());
        } else {
            booking.setStatus(Status.REJECTED.toString());
        }
        return bookingStorage.save(booking);
    }

    @Override
    public Booking findBookingById(Long userId, Long bookingId) {
        bookingValidationService.validateBeforeSearchById(userId, bookingId);
        Booking booking = bookingStorage.findById(bookingId).get();
        return booking;//BookingMapper.toBookingDtoOut(booking, itemDbStorage.findById(booking.getItemId()).get(), userDbStorage.findById(booking.getBookerId()).get());
    }

    @Override
    public List<BookingDtoOut> findBookingByUser(Long ownerId, String state) {
        bookingValidationService.validateBeforeSearchByUserId(ownerId, state);
        State stateEnum = State.valueOf(state);

        switch (stateEnum) {
            case ALL:
                List<BookingDtoWithoutEntity> bookings = bookingStorage.findAllByBookerIdOrderByStartDesc(ownerId);
                return bookings.stream()
                        .map(b -> BookingMapper
                                .toBookingDtoOut(b, itemDbStorage.findById(b.getItemId()).get(), userDbStorage.findById(b.getBookerId()).get()))
                        .collect(Collectors.toList());
            case PAST:
                bookings = bookingStorage.findAllByBookerIdAndEndBeforeOrderByStartDesc(
                        ownerId, LocalDateTime.now());

                return bookings.stream()
                        .map(b -> BookingMapper
                                .toBookingDtoOut(b, itemDbStorage.findById(b.getItemId()).get(), userDbStorage.findById(b.getBookerId()).get()))
                        .collect(Collectors.toList());
            case FUTURE:
                bookings = bookingStorage.findAllByBookerIdAndStartAfterOrderByStartDesc(
                        ownerId, LocalDateTime.now());

                return bookings.stream()
                        .map(b -> BookingMapper
                                .toBookingDtoOut(b, itemDbStorage.findById(b.getItemId()).get(), userDbStorage.findById(b.getBookerId()).get()))
                        .collect(Collectors.toList());
            case CURRENT:
                bookings = bookingStorage.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        ownerId, LocalDateTime.now(), LocalDateTime.now());

                return bookings.stream()
                        .map(b -> BookingMapper
                                .toBookingDtoOut(b, itemDbStorage.findById(b.getItemId()).get(), userDbStorage.findById(b.getBookerId()).get()))
                        .collect(Collectors.toList());
            case WAITING:
                bookings = bookingStorage.findAllByBookerIdAndStatusOrderByStartDesc(
                        ownerId, Status.WAITING.toString());

                return bookings.stream()
                        .map(b -> BookingMapper
                                .toBookingDtoOut(b, itemDbStorage.findById(b.getItemId()).get(), userDbStorage.findById(b.getBookerId()).get()))
                        .collect(Collectors.toList());
            case REJECTED:
                bookings = bookingStorage.findAllByBookerIdAndStatusOrderByStartDesc(
                        ownerId, Status.REJECTED.toString());

                return bookings.stream()
                        .map(b -> BookingMapper
                                .toBookingDtoOut(b, itemDbStorage.findById(b.getItemId()).get(), userDbStorage.findById(b.getBookerId()).get()))
                        .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<Booking> findBookingAllItemsByUser(Long ownerId, String state) {
        bookingValidationService.validateBeforeSearchByUserId(ownerId, state);
        State stateEnum = State.valueOf(state);

        switch (stateEnum) {
            case ALL:
                return bookingStorage.findBookingItemByUser(ownerId);
            case PAST:
                return bookingStorage.findBookingItemByUserPast(
                        ownerId, LocalDateTime.now());
            case FUTURE:
                return bookingStorage.findBookingItemByUserFuture(
                        ownerId, LocalDateTime.now());
            case CURRENT:
                return bookingStorage.findBookingItemByUserCurrent(
                        ownerId, LocalDateTime.now(), LocalDateTime.now());
            case WAITING:
                return bookingStorage.findBookingItemByUserStatus(
                        ownerId, Status.WAITING.toString());
            case REJECTED:
                return bookingStorage.findBookingItemByUserStatus(
                        ownerId, Status.REJECTED.toString());
        }

        return Collections.emptyList();
    }

}
