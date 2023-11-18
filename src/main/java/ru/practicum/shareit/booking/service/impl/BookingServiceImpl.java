package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;
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
    public Booking create(Long userId, BookingDto bookingDto) {
        bookingValidationService.validateBeforeCreate(userId, bookingDto);
        Item item = itemDbStorage.findById(bookingDto.getItemId()).get();
        User user = userDbStorage.findById(userId).get();
        Booking booking = BookingMapper.toBooking(bookingDto, item, user, Status.WAITING.toString());
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
        return bookingStorage.findById(bookingId).get();
    }

    @Override
    public List<Booking> findBookingByUser(Long ownerId, String state) {
        bookingValidationService.validateBeforeSearchByUserId(ownerId, state);
        State stateEnum = State.valueOf(state);
        switch (stateEnum) {
            case ALL:
                return bookingStorage.findAllByBookerIdOrderByStartDesc(ownerId);
            case PAST:
                return bookingStorage.findAllByBookerIdAndEndBeforeOrderByStartDesc(
                        ownerId, LocalDateTime.now());
            case FUTURE:
                return bookingStorage.findAllByBookerIdAndStartAfterOrderByStartDesc(
                        ownerId, LocalDateTime.now());
            case CURRENT:
                return bookingStorage.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        ownerId, LocalDateTime.now(), LocalDateTime.now());
            case WAITING:
                return bookingStorage.findAllByBookerIdAndStatusOrderByStartDesc(
                        ownerId, Status.WAITING.toString());
            case REJECTED:
                return bookingStorage.findAllByBookerIdAndStatusOrderByStartDesc(
                        ownerId, Status.REJECTED.toString());
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
