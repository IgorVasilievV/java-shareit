package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.SecurityException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.service.impl.ValidationItemServiceDbImpl;
import ru.practicum.shareit.item.storage.ItemDbStorage;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDbStorage;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationItemServiceTest {

    @Mock
    private ItemDbStorage itemStorage;

    @Mock
    private UserDbStorage userStorage;

    @Mock
    private BookingStorage bookingStorage;

    @Mock
    private ItemRequestStorage itemRequestStorage;

    @InjectMocks
    private ValidationItemServiceDbImpl validationItemService;

    @Test
    void validateBeforeCreate_shouldReturnExceptionWhenUserIsBlanc() {
        ItemDto itemDto = ItemDto.builder().build();

        assertThrows(ValidationException.class, () -> validationItemService.validateBeforeCreate(-1L, itemDto));
    }

    @Test
    void validateBeforeCreate_shouldReturnExceptionWhenUserNotExist() {
        ItemDto itemDto = ItemDto.builder().build();
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> validationItemService.validateBeforeCreate(1L, itemDto));
    }

    @Test
    void validateBeforeCreate_shouldReturnExceptionWhenItemDtoWithoutName() {
        ItemDto itemDto = ItemDto.builder().build();
        when(userStorage.existsById(anyLong())).thenReturn(true);

        assertThrows(ValidationException.class, () -> validationItemService.validateBeforeCreate(1L, itemDto));
    }

    @Test
    void validateBeforeCreate_shouldReturnExceptionWhenItemDtoWithoutDescription() {
        ItemDto itemDto = ItemDto.builder().name("name").build();
        when(userStorage.existsById(anyLong())).thenReturn(true);

        assertThrows(ValidationException.class, () -> validationItemService.validateBeforeCreate(1L, itemDto));
    }

    @Test
    void validateBeforeCreate_shouldReturnExceptionWhenItemDtoWithoutAvailable() {
        ItemDto itemDto = ItemDto.builder().name("name").description("description").build();
        when(userStorage.existsById(anyLong())).thenReturn(true);

        assertThrows(ValidationException.class, () -> validationItemService.validateBeforeCreate(1L, itemDto));
    }

    @Test
    void validateBeforeUpdate_shouldReturnExceptionWhenItemNotExist() {
        ItemDto itemDto = ItemDto.builder().build();
        when(itemStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> validationItemService.validateBeforeUpdate(1L, 1L, itemDto));
    }

    @Test
    void validateBeforeUpdate_shouldReturnExceptionWhenUserIsBlanc() {
        ItemDto itemDto = ItemDto.builder().build();
        when(itemStorage.existsById(anyLong())).thenReturn(true);

        assertThrows(ValidationException.class,
                () -> validationItemService.validateBeforeUpdate(-1L, 1L, itemDto));
    }

    @Test
    void validateBeforeUpdate_shouldReturnExceptionWhenUserNotExist() {
        ItemDto itemDto = ItemDto.builder().build();
        when(itemStorage.existsById(anyLong())).thenReturn(true);
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> validationItemService.validateBeforeUpdate(1L, 1L, itemDto));
    }

    @Test
    void validateBeforeUpdate_shouldReturnExceptionWhenUserIsNotOwner() {
        ItemDto itemDto = ItemDto.builder().build();
        Item item = Item.builder().owner(User.builder().id(1L).build()).build();
        when(itemStorage.existsById(anyLong())).thenReturn(true);
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(SecurityException.class,
                () -> validationItemService.validateBeforeUpdate(2L, 1L, itemDto));
    }

    @Test
    void validateSearch_shouldReturnExceptionWhenItemNotExist() {
        when(itemStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> validationItemService.validateSearch(1L));
    }

    @Test
    void validateSearchByUser_shouldReturnExceptionWhenUserIsBlanc() {
        assertThrows(ValidationException.class,
                () -> validationItemService.validateSearchByUser(-1L));
    }

    @Test
    void validateSearchByUser_shouldReturnExceptionWhenUserNotExist() {
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> validationItemService.validateSearchByUser(1L));
    }

    @Test
    void validateComment_shouldReturnExceptionWhenUserNotExist() {
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> validationItemService.validateComment(1L, 1L));
    }

    @Test
    void validateComment_shouldReturnExceptionWhenItemNotExist() {
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(itemStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> validationItemService.validateComment(1L, 1L));
    }

    @Test
    void validateComment_shouldReturnExceptionWhenUserIsNotBooker() {
        Booking booking = null;
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(itemStorage.existsById(anyLong())).thenReturn(true);
        when(bookingStorage.findFirstByItemIdAndBookerIdAndStatus(anyLong(), anyLong(), anyString()))
                .thenReturn(booking);

        assertThrows(ValidationException.class,
                () -> validationItemService.validateComment(1L, 1L));
    }

    @Test
    void validateComment_shouldReturnExceptionWhenRentDidNotFinish() {
        Booking booking = Booking.builder().end(LocalDateTime.now().plusHours(1)).build();
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(itemStorage.existsById(anyLong())).thenReturn(true);
        when(bookingStorage.findFirstByItemIdAndBookerIdAndStatus(anyLong(), anyLong(), anyString()))
                .thenReturn(booking);

        assertThrows(ValidationException.class,
                () -> validationItemService.validateComment(1L, 1L));
    }

    @Test
    void validateItemRequest() {
        when(itemRequestStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> validationItemService.validateItemRequest(1L));
    }
}