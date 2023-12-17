package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.SecurityException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.service.ValidationItemService;
import ru.practicum.shareit.item.storage.ItemDbStorage;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.storage.UserDbStorage;

import java.time.LocalDateTime;

@Service("ValidationItemServiceDbImpl")
@RequiredArgsConstructor
public class ValidationItemServiceDbImpl implements ValidationItemService {

    private final ItemDbStorage itemStorage;
    private final UserDbStorage userStorage;
    private final BookingStorage bookingStorage;
    private final ItemRequestStorage itemRequestStorage;

    @Override
    public void validateBeforeCreate(Long ownerId, ItemDto itemDto) throws NotFoundException, ValidationException {
        validateWithoutOwner(ownerId);
        validateContainsOwner(ownerId);
        validateData(itemDto);
    }

    @Override
    public void validateBeforeUpdate(Long ownerId, Long id, ItemDto itemDto)
            throws NotFoundException, ValidationException, SecurityException {
        validateSearch(id);
        validateWithoutOwner(ownerId);
        validateContainsOwner(ownerId);
        validateSecurity(ownerId, id);
    }

    @Override
    public void validateSearch(Long id) throws NotFoundException {
        if (!itemStorage.existsById(id)) {
            throw new NotFoundException("Item with id = " + id + " not found");
        }
    }

    @Override
    public void validateSearchByUser(Long ownerId) {
        validateWithoutOwner(ownerId);
        validateContainsOwner(ownerId);
    }

    private void validateContainsOwner(Long ownerId) throws NotFoundException {
        if (!userStorage.existsById(ownerId)) {
            throw new NotFoundException("Unknown user with id = " + ownerId);
        }
    }

    private void validateWithoutOwner(Long ownerId) throws ValidationException {
        if (ownerId.equals(-1L)) {
            throw new ValidationException("Item without owner");
        }
    }

    private void validateData(ItemDto itemDto) throws ValidationException {
        if (itemDto.getName() == null || itemDto.getName().isBlank() ||
                itemDto.getDescription() == null || itemDto.getDescription().isBlank() ||
                itemDto.getAvailable() == null) {
            throw new ValidationException("Item without allNecessaryData");
        }
    }

    private void validateSecurity(Long ownerId, Long id) throws SecurityException {
        if (!itemStorage.findById(id).get().getOwner().getId().equals(ownerId)) {
            throw new SecurityException("User don't owner");
        }
    }

    @Override
    public void validateComment(Long userId, Long itemId) throws ValidationException {
        validateContainsOwner(userId);
        validateSearch(itemId);
        validateBookingIsFinished(userId, itemId);

    }

    @Override
    public void validateItemRequest(Long requestId) {
        if (!itemRequestStorage.existsById(requestId)) {
            throw new NotFoundException("Request not found with id = " + requestId);
        }
    }

    private void validateBookingIsFinished(Long userId, Long itemId) throws ValidationException {
        Booking booking = bookingStorage.findFirstByItemIdAndBookerIdAndStatus(itemId, userId, Status.APPROVED.toString());
        if (booking == null) {
            throw new ValidationException("User isn't booker");
        }
        if (booking.getEnd().isAfter(LocalDateTime.now())) {
            throw new ValidationException("Rent didn't finish");
        }
    }
}
