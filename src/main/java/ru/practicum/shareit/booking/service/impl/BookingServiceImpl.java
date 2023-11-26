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
    public BookingDtoOut create(Long userId, BookingDtoIn bookingDtoIn) {
        bookingValidationService.validateBeforeCreate(userId, bookingDtoIn);
        Item item = itemDbStorage.findById(bookingDtoIn.getItemId()).get();
        User user = userDbStorage.findById(userId).get();
        Booking booking = BookingMapper.toBooking(bookingDtoIn, item, user, Status.WAITING.toString());
        return BookingMapper.toBookingDtoOut(bookingStorage.save(booking));
    }

    @Transactional
    @Override
    public BookingDtoOut setStatusBooking(Long ownerId, Long bookingId, Boolean approved) {
        bookingValidationService.validateBeforeSetStatus(ownerId, bookingId);
        Booking booking = bookingStorage.findById(bookingId).get();
        if (approved) {
            booking.setStatus(Status.APPROVED.toString());
        } else {
            booking.setStatus(Status.REJECTED.toString());
        }
        return BookingMapper.toBookingDtoOut(bookingStorage.save(booking));
    }

    @Override
    public BookingDtoOut findBookingById(Long userId, Long bookingId) {
        bookingValidationService.validateBeforeSearchById(userId, bookingId);
        return BookingMapper.toBookingDtoOut(bookingStorage.findById(bookingId).get());
    }

    @Override
    public List<BookingDtoOut> findBookingByUser(Long ownerId, String state) {
        bookingValidationService.validateBeforeSearchByUserId(ownerId, state);
        State stateEnum = State.valueOf(state);

        switch (stateEnum) {
            case ALL:
                return BookingMapper.bookingDtoOutList(bookingStorage.findAllByBookerIdOrderByStartDesc(ownerId));
            case PAST:
                return BookingMapper.bookingDtoOutList(bookingStorage.findAllByBookerIdAndEndBeforeOrderByStartDesc(
                        ownerId, LocalDateTime.now()));
            case FUTURE:
                return BookingMapper.bookingDtoOutList(bookingStorage.findAllByBookerIdAndStartAfterOrderByStartDesc(
                        ownerId, LocalDateTime.now()));
            case CURRENT:
                return BookingMapper.bookingDtoOutList(bookingStorage
                        .findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                                ownerId, LocalDateTime.now(), LocalDateTime.now()));
            case WAITING:
                return BookingMapper.bookingDtoOutList(bookingStorage.findAllByBookerIdAndStatusOrderByStartDesc(
                        ownerId, Status.WAITING.toString()));
            case REJECTED:
                return BookingMapper.bookingDtoOutList(bookingStorage.findAllByBookerIdAndStatusOrderByStartDesc(
                        ownerId, Status.REJECTED.toString()));
        }
        return Collections.emptyList();
    }

    @Override
    public List<BookingDtoOut> findBookingAllItemsByUser(Long ownerId, String state) {
        bookingValidationService.validateBeforeSearchByUserId(ownerId, state);
        State stateEnum = State.valueOf(state);

        switch (stateEnum) {
            case ALL:
                return BookingMapper.bookingDtoOutList(bookingStorage.findAllByItem_owner_idOrderByStartDesc(ownerId));
            case PAST:
                return BookingMapper.bookingDtoOutList(bookingStorage.findAllByItem_owner_idAndEndBeforeOrderByStartDesc(
                        ownerId, LocalDateTime.now()));
            case FUTURE:
                return BookingMapper.bookingDtoOutList(bookingStorage.findAllByItem_owner_idAndStartAfterOrderByStartDesc(
                        ownerId, LocalDateTime.now()));
            case CURRENT:
                return BookingMapper.bookingDtoOutList(bookingStorage.findAllByItem_owner_idAndStartBeforeAndEndAfterOrderByStartDesc(
                        ownerId, LocalDateTime.now(), LocalDateTime.now()));
            case WAITING:
                return BookingMapper.bookingDtoOutList(bookingStorage.findAllByItem_owner_idAndStatusOrderByStartDesc(
                        ownerId, Status.WAITING.toString()));
            case REJECTED:
                return BookingMapper.bookingDtoOutList(bookingStorage.findAllByItem_owner_idAndStatusOrderByStartDesc(
                        ownerId, Status.REJECTED.toString()));
        }

        return Collections.emptyList();
    }

}
