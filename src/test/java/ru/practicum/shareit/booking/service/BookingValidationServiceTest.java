package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.impl.BookingValidationServiceImpl;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.StateException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemDbStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDbStorage;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingValidationServiceTest {

    @Mock
    private ItemDbStorage itemStorage;

    @Mock
    private UserDbStorage userStorage;

    @Mock
    private BookingStorage bookingStorage;

    @InjectMocks
    BookingValidationServiceImpl bookingValidationService;

    @Test
    void validateBeforeCreate_shouldReturnExceptionWhenUserNotExist() {
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder().build();
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> bookingValidationService.validateBeforeCreate(1L, bookingDtoIn));
    }

    @Test
    void validateBeforeCreate_shouldReturnExceptionWhenItemIsNotExist() {
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder().itemId(1L).build();
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(itemStorage.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingValidationService.validateBeforeCreate(1L, bookingDtoIn));
    }

    @Test
    void validateBeforeCreate_shouldReturnExceptionWhenUserIsOwner() {
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder().itemId(1L).build();
        Item item = Item.builder().owner(User.builder().id(1L).build()).build();
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class,
                () -> bookingValidationService.validateBeforeCreate(1L, bookingDtoIn));
    }

    @Test
    void validateBeforeCreate_shouldReturnExceptionWhenAvailableIsFalse() {
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder().itemId(1L).build();
        Item item = Item.builder().owner(User.builder().id(2L).build()).available(false).build();
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class,
                () -> bookingValidationService.validateBeforeCreate(1L, bookingDtoIn));
    }

    @Test
    void validateBeforeCreate_shouldReturnExceptionWhenIncorrectBookingDtoIn() {
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now())
                .build();
        Item item = Item.builder().owner(User.builder().id(2L).build()).available(true).build();
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class,
                () -> bookingValidationService.validateBeforeCreate(1L, bookingDtoIn));
    }

    @Test
    void validateBeforeSetStatus_shouldReturnExceptionWhenBookingIsNotExist() {
        when(bookingStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> bookingValidationService.validateBeforeSetStatus(1L, 1L));
    }

    @Test
    void validateBeforeSetStatus_shouldReturnExceptionWhenUserIsNotOwner() {
        Booking booking = Booking.builder()
                .item(Item.builder()
                        .owner(User.builder()
                                .id(2L)
                                .build())
                        .build())
                .build();
        when(bookingStorage.existsById(anyLong())).thenReturn(true);
        when(bookingStorage.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class,
                () -> bookingValidationService.validateBeforeSetStatus(1L, 1L));
    }

    @Test
    void validateBeforeSetStatus_shouldReturnExceptionWhenStatusNotWaiting() {
        Booking booking = Booking.builder()
                .item(Item.builder()
                        .owner(User.builder()
                                .id(1L)
                                .build())
                        .build())
                .status("Status")
                .build();
        when(bookingStorage.existsById(anyLong())).thenReturn(true);
        when(bookingStorage.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class,
                () -> bookingValidationService.validateBeforeSetStatus(1L, 1L));
    }

    @Test
    void validateBeforeSearchById_shouldReturnExceptionWhenBookingIsNotExist() {
        when(bookingStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> bookingValidationService.validateBeforeSearchById(1L, 1L));

    }

    @Test
    void validateBeforeSearchById_shouldReturnExceptionWhenUserNotExist() {
        when(bookingStorage.existsById(anyLong())).thenReturn(true);
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> bookingValidationService.validateBeforeSearchById(1L, 1L));

    }

    @Test
    void validateBeforeSearchById_shouldNotReturnExceptionWhenUserIsOwner() {
        Booking booking = Booking.builder()
                .item(Item.builder()
                        .owner(User.builder()
                                .id(1L)
                                .build())
                        .build())
                .booker(User.builder()
                        .id(2L)
                        .build())
                .build();
        when(bookingStorage.existsById(anyLong())).thenReturn(true);
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(bookingStorage.findById(anyLong())).thenReturn(Optional.of(booking));

        assertDoesNotThrow(() -> bookingValidationService.validateBeforeSearchById(1L, 1L));
    }

    @Test
    void validateBeforeSearchById_shouldNotReturnExceptionWhenUserIsBooker() {
        Booking booking = Booking.builder()
                .item(Item.builder()
                        .owner(User.builder()
                                .id(2L)
                                .build())
                        .build())
                .booker(User.builder()
                        .id(1L)
                        .build())
                .build();
        when(bookingStorage.existsById(anyLong())).thenReturn(true);
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(bookingStorage.findById(anyLong())).thenReturn(Optional.of(booking));

        assertDoesNotThrow(() -> bookingValidationService.validateBeforeSearchById(1L, 1L));
    }

    @Test
    void validateBeforeSearchById_shouldReturnExceptionWhenUserIsNotOwnerOrBooker() {
        Booking booking = Booking.builder()
                .item(Item.builder()
                        .owner(User.builder()
                                .id(2L)
                                .build())
                        .build())
                .booker(User.builder()
                        .id(2L)
                        .build())
                .build();
        when(bookingStorage.existsById(anyLong())).thenReturn(true);
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(bookingStorage.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class,
                () -> bookingValidationService.validateBeforeSearchById(1L, 1L));
    }

    @Test
    void validateBeforeSearchByUserId_shouldReturnExceptionWhenUserNotExist() {
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> bookingValidationService.validateBeforeSearchByUserId(1L, "ALL"));
    }

    @Test
    void validateBeforeSearchByUserId_shouldReturnExceptionWhenUndefinedStatus() {
        when(userStorage.existsById(anyLong())).thenReturn(true);

        assertThrows(StateException.class,
                () -> bookingValidationService.validateBeforeSearchByUserId(1L, "Status"));
    }

    @Test
    void validateBeforeSearchByUserId_shouldNotReturnExceptionWhenCurrentlyStatus() {
        when(userStorage.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> bookingValidationService.validateBeforeSearchByUserId(1L, "ALL"));
    }
}