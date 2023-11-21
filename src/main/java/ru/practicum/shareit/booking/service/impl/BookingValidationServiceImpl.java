package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.service.BookingValidationService;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.StateException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemDbStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDbStorage;

@Service
@RequiredArgsConstructor
public class BookingValidationServiceImpl implements BookingValidationService {
    private final ItemDbStorage itemStorage;
    private final UserDbStorage userStorage;
    private final BookingStorage bookingStorage;

    @Override
    public void validateBeforeCreate(Long userId, BookingDtoIn bookingDtoIn) throws ValidationException, NotFoundException {
        validateContainsUser(userId);
        validateUserIsNotOwnerAndItemExistAndAvailable(userId, bookingDtoIn);
        validateDate(bookingDtoIn);
    }

    @Override
    public void validateBeforeSetStatus(Long ownerId, Long bookingId) throws NotFoundException {
        validateBookingExist(bookingId);
        validateUserIsOwner(ownerId, bookingId);
        validateStatus(bookingId);
    }

    @Override
    public void validateBeforeSearchById(Long userId, Long bookingId) throws NotFoundException, ValidationException {
        validateBookingExist(bookingId);
        validateContainsUser(userId);
        validateUserIsOwnerOrBooker(userId, bookingId);
    }

    @Override
    public void validateBeforeSearchByUserId(Long userId, String state) throws NotFoundException, StateException {
        validateContainsUser(userId);
        validateState(state);
    }

    private void validateState(String state) throws StateException {
        try {
            State.valueOf(state);
        } catch (Exception e) {
            throw new StateException("Unknown state: " + state);
        }
    }

    private void validateUserIsOwnerOrBooker(Long userId, Long bookingId) throws NotFoundException {
        Item item = bookingStorage.findById(bookingId).get().getItem();
        User booker = bookingStorage.findById(bookingId).get().getBooker();
        if (item != null && userId.equals(item.getOwner().getId())) {
            return;
        }
        if (userId.equals(booker.getId())) {
            return;
        }
        throw new NotFoundException("User isn't booker");

    }

    private void validateStatus(Long bookingId) throws ValidationException {
        String status = bookingStorage.findById(bookingId).get().getStatus();
        if (!Status.WAITING.toString().equals(status)) {
            throw new ValidationException("Status already changed");
        }
    }

    private void validateBookingExist(Long bookingId) throws NotFoundException {
        if (!bookingStorage.existsById(bookingId)) {
            throw new NotFoundException("Unknown booking with id = " + bookingId);
        }
    }

    private void validateUserIsOwner(Long ownerId, Long bookingId) throws NotFoundException {
        Item item = bookingStorage.findById(bookingId).get().getItem();
        if (item == null || !ownerId.equals(item.getOwner().getId())) {
            throw new NotFoundException("User isn't owner");
        }
    }

    private void validateContainsUser(Long userId) throws NotFoundException {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException("Unknown user with id = " + userId);
        }
    }

    private void validateUserIsNotOwnerAndItemExistAndAvailable(Long userId, BookingDtoIn bookingDtoIn)
            throws ValidationException, NotFoundException {
        Item item = itemStorage.findById(bookingDtoIn.getItemId())
                .orElseThrow(() -> new NotFoundException("Item with id = " + bookingDtoIn.getItemId() + "not found"));
        if (userId.equals(item.getOwner().getId())) {
            throw new NotFoundException("User can't be owner");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Item must be available");
        }
    }

    private void validateDate(BookingDtoIn bookingDtoIn) throws ValidationException {
        if (bookingDtoIn.getStart().isAfter(bookingDtoIn.getEnd()) || bookingDtoIn.getStart().equals(bookingDtoIn.getEnd())) {
            throw new ValidationException("Incorrect date");
        }
    }
}
