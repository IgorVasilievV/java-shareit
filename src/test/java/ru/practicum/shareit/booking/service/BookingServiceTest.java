package ru.practicum.shareit.booking.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemDbStorage;
import ru.practicum.shareit.request.service.ValidationItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDbStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingStorage bookingStorage;

    @Mock
    private UserDbStorage userDbStorage;

    @Mock
    private ItemDbStorage itemDbStorage;

    @Mock
    private BookingValidationService bookingValidationService;

    @Mock
    private ValidationItemRequestService validationItemRequestService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Captor
    ArgumentCaptor<Booking> argumentCaptorBooking;

    @SneakyThrows
    @Test
    void create_shouldReturnBookingDtoOut() {
        Item item = Item.builder().id(1L).build();
        User user = User.builder().id(1L).build();
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder().itemId(1L).build();
        BookingDtoOut bookingDtoOutExpected = BookingDtoOut.builder().build();
        Booking booking = Booking.builder().build();
        when(itemDbStorage.findById(anyLong())).thenReturn(Optional.of(item));
        when(userDbStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingStorage.save(any(Booking.class))).thenReturn(booking);

        BookingDtoOut bookingDtoOutActual = bookingService.create(1L, bookingDtoIn);

        assertEquals(bookingDtoOutExpected, bookingDtoOutActual);
        verify(bookingStorage).save(any(Booking.class));
    }

    @SneakyThrows
    @Test
    void create_shouldReturnException() {
        doThrow(ValidationException.class)
                .when(bookingValidationService).validateBeforeCreate(anyLong(), any(BookingDtoIn.class));

        assertThrows(ValidationException.class, () -> bookingService.create(1L, BookingDtoIn.builder().build()));
        verify(bookingStorage, never()).save(any(Booking.class));
    }

    @SneakyThrows
    @Test
    void setStatusBooking_shouldReturnBookingDtoOutWithApprove() {
        Booking booking = Booking.builder().build();
        Booking bookingExpected = Booking.builder().status("APPROVED").build();
        when(bookingStorage.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingStorage.save(any(Booking.class))).thenReturn(booking);

        bookingService.setStatusBooking(1L, 1L, true);

        verify(bookingStorage).save(argumentCaptorBooking.capture());
        Booking bookingActual = argumentCaptorBooking.getValue();
        assertEquals(bookingExpected.getStatus(), bookingActual.getStatus());
    }

    @Test
    void setStatusBooking_shouldReturnBookingDtoOutWithReject() {
        Booking booking = Booking.builder().build();
        Booking bookingExpected = Booking.builder().status("REJECTED").build();
        when(bookingStorage.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingStorage.save(any(Booking.class))).thenReturn(booking);

        bookingService.setStatusBooking(1L, 1L, false);

        verify(bookingStorage).save(argumentCaptorBooking.capture());
        Booking bookingActual = argumentCaptorBooking.getValue();
        assertEquals(bookingExpected.getStatus(), bookingActual.getStatus());
    }

    @SneakyThrows
    @Test
    void setStatusBooking_shouldReturnException() {
        doThrow(ValidationException.class)
                .when(bookingValidationService).validateBeforeSetStatus(anyLong(), anyLong());

        assertThrows(ValidationException.class,
                () -> bookingService.setStatusBooking(1L, 1L, true));
        verify(bookingStorage, never()).save(any(Booking.class));
    }

    @SneakyThrows
    @Test
    void findBookingById_shouldReturnBookingDtoOut() {
        Booking booking = Booking.builder().id(1L).build();
        BookingDtoOut bookingDtoOutExpected = BookingDtoOut.builder().id(1L).build();
        when(bookingStorage.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingDtoOut bookingDtoOutActual = bookingService.findBookingById(1L, 1L);

        assertEquals(bookingDtoOutExpected, bookingDtoOutActual);
        verify(bookingStorage).findById(anyLong());
    }

    @SneakyThrows
    @Test
    void findBookingById_shouldReturnException() {
        doThrow(ValidationException.class)
                .when(bookingValidationService).validateBeforeSearchById(anyLong(), anyLong());

        assertThrows(ValidationException.class,
                () -> bookingService.findBookingById(1L, 1L));
        verify(bookingStorage, never()).findById(anyLong());
    }

    @SneakyThrows
    @Test
    void findBookingByUser_shouldReturnBookingDtoOutListAtAll() {
        when(bookingStorage.findAllByBookerIdOrderByStartDesc(anyLong()))
                .thenReturn(List.of(Booking.builder().build()));

        bookingService.findBookingByUser(1L, "ALL", null, null);

        verify(bookingStorage).findAllByBookerIdOrderByStartDesc(1L);
    }

    @SneakyThrows
    @Test
    void findBookingByUser_shouldReturnBookingDtoOutListAtPast() {
        when(bookingStorage.findAllByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(Booking.builder().build()));

        bookingService.findBookingByUser(1L, "PAST", null, null);

        verify(bookingStorage).findAllByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class));
    }

    @SneakyThrows
    @Test
    void findBookingByUser_shouldReturnBookingDtoOutListAtFuture() {
        when(bookingStorage.findAllByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(Booking.builder().build()));

        bookingService.findBookingByUser(1L, "FUTURE", null, null);

        verify(bookingStorage).findAllByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class));
    }

    @SneakyThrows
    @Test
    void findBookingByUser_shouldReturnBookingDtoOutListAtCurrent() {
        when(bookingStorage.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(Booking.builder().build()));

        bookingService.findBookingByUser(1L, "CURRENT", null, null);

        verify(bookingStorage).findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @SneakyThrows
    @Test
    void findBookingByUser_shouldReturnBookingDtoOutListAtWaiting() {
        when(bookingStorage.findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), anyString()))
                .thenReturn(List.of(Booking.builder().build()));

        bookingService.findBookingByUser(1L, "WAITING", null, null);

        verify(bookingStorage).findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), anyString());
    }

    @SneakyThrows
    @Test
    void findBookingByUser_shouldReturnBookingDtoOutListAtRejected() {
        when(bookingStorage.findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), anyString()))
                .thenReturn(List.of(Booking.builder().build()));

        bookingService.findBookingByUser(1L, "REJECTED", null, null);

        verify(bookingStorage).findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), anyString());
    }

    @SneakyThrows
    @Test
    void findBookingByUser_shouldReturnExceptionByIdOrState() {
        doThrow(ValidationException.class)
                .when(bookingValidationService).validateBeforeSearchByUserId(anyLong(), anyString());

        assertThrows(ValidationException.class,
                () -> bookingService.findBookingByUser(1L, "ALL", null, null));
        verify(bookingStorage, never())
                .findAllByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class));
    }

    @SneakyThrows
    @Test
    void findBookingByUser_shouldReturnBookingDtoOutPageAtAll() {
        when(bookingStorage.findAllByBookerId(anyLong(), any(PageRequest.class)))
                .thenReturn(new PageImpl(List.of(Booking.builder().build())));

        bookingService.findBookingByUser(1L, "ALL", 0, 2);

        verify(bookingStorage).findAllByBookerId(anyLong(), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void findBookingByUser_shouldReturnBookingDtoOutPageAtPast() {
        when(bookingStorage.findAllByBookerIdAndEndBefore(anyLong(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(List.of(Booking.builder().build())));

        bookingService.findBookingByUser(1L, "PAST", 0, 2);

        verify(bookingStorage)
                .findAllByBookerIdAndEndBefore(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void findBookingByUser_shouldReturnBookingDtoOutPageAtFuture() {
        when(bookingStorage.findAllByBookerIdAndStartAfter(anyLong(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(List.of(Booking.builder().build())));

        bookingService.findBookingByUser(1L, "FUTURE", 0, 2);

        verify(bookingStorage)
                .findAllByBookerIdAndStartAfter(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void findBookingByUser_shouldReturnBookingDtoOutPageAtCurrent() {
        when(bookingStorage.findAllByBookerIdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class),
                any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(List.of(Booking.builder().build())));

        bookingService.findBookingByUser(1L, "CURRENT", 0, 2);

        verify(bookingStorage)
                .findAllByBookerIdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class),
                        any(LocalDateTime.class), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void findBookingByUser_shouldReturnBookingDtoOutPageAtWaiting() {
        when(bookingStorage.findAllByBookerIdAndStatus(anyLong(), anyString(), any(PageRequest.class)))
                .thenReturn(new PageImpl(List.of(Booking.builder().build())));

        bookingService.findBookingByUser(1L, "WAITING", 0, 2);

        verify(bookingStorage).findAllByBookerIdAndStatus(anyLong(), anyString(), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void findBookingByUser_shouldReturnBookingDtoOutPageAtRejected() {
        when(bookingStorage.findAllByBookerIdAndStatus(anyLong(), anyString(), any(PageRequest.class)))
                .thenReturn(new PageImpl(List.of(Booking.builder().build())));

        bookingService.findBookingByUser(1L, "REJECTED", 0, 2);

        verify(bookingStorage).findAllByBookerIdAndStatus(anyLong(), anyString(), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void findBookingByUser_shouldReturnExceptionByPagination() {
        doThrow(ValidationException.class)
                .when(validationItemRequestService).validatePagination(anyInt(), anyInt());

        assertThrows(ValidationException.class,
                () -> bookingService.findBookingByUser(1L, "ALL", 2, 0));
        verify(bookingStorage, never())
                .findAllByBookerId(anyLong(), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void findBookingAllItemsByUser_shouldReturnBookingDtoOutListAtAll() {
        when(bookingStorage.findAllByItem_owner_idOrderByStartDesc(anyLong()))
                .thenReturn(List.of(Booking.builder().build()));

        bookingService.findBookingAllItemsByUser(1L, "ALL", null, null);

        verify(bookingStorage).findAllByItem_owner_idOrderByStartDesc(1L);
    }

    @SneakyThrows
    @Test
    void findBookingAllItemsByUser_shouldReturnBookingDtoOutListAtPast() {
        when(bookingStorage.findAllByItem_owner_idAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(Booking.builder().build()));

        bookingService.findBookingAllItemsByUser(1L, "PAST", null, null);

        verify(bookingStorage).findAllByItem_owner_idAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class));
    }

    @SneakyThrows
    @Test
    void findBookingAllItemsByUser_shouldReturnBookingDtoOutListAtFuture() {
        when(bookingStorage.findAllByItem_owner_idAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(Booking.builder().build()));

        bookingService.findBookingAllItemsByUser(1L, "FUTURE", null, null);

        verify(bookingStorage).findAllByItem_owner_idAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class));
    }

    @SneakyThrows
    @Test
    void findBookingAllItemsByUser_shouldReturnBookingDtoOutListAtCurrent() {
        when(bookingStorage.findAllByItem_owner_idAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(Booking.builder().build()));

        bookingService.findBookingAllItemsByUser(1L, "CURRENT", null, null);

        verify(bookingStorage).findAllByItem_owner_idAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @SneakyThrows
    @Test
    void findBookingAllItemsByUser_shouldReturnBookingDtoOutListAtWaiting() {
        when(bookingStorage.findAllByItem_owner_idAndStatusOrderByStartDesc(anyLong(), anyString()))
                .thenReturn(List.of(Booking.builder().build()));

        bookingService.findBookingAllItemsByUser(1L, "WAITING", null, null);

        verify(bookingStorage).findAllByItem_owner_idAndStatusOrderByStartDesc(anyLong(), anyString());
    }

    @SneakyThrows
    @Test
    void findBookingAllItemsByUser_shouldReturnBookingDtoOutListAtRejected() {
        when(bookingStorage.findAllByItem_owner_idAndStatusOrderByStartDesc(anyLong(), anyString()))
                .thenReturn(List.of(Booking.builder().build()));

        bookingService.findBookingAllItemsByUser(1L, "REJECTED", null, null);

        verify(bookingStorage).findAllByItem_owner_idAndStatusOrderByStartDesc(anyLong(), anyString());
    }

    @SneakyThrows
    @Test
    void findBookingAllItemsByUser_shouldReturnExceptionByIdOrState() {
        doThrow(ValidationException.class)
                .when(bookingValidationService).validateBeforeSearchByUserId(anyLong(), anyString());

        assertThrows(ValidationException.class,
                () -> bookingService.findBookingAllItemsByUser(1L, "ALL", null, null));
        verify(bookingStorage, never())
                .findAllByItem_owner_idOrderByStartDesc(anyLong());
    }

    @SneakyThrows
    @Test
    void findBookingAllItemsByUser_shouldReturnBookingDtoOutPageAtAll() {
        when(bookingStorage.findAllByItem_owner_id(anyLong(), any(PageRequest.class)))
                .thenReturn(new PageImpl(List.of(Booking.builder().build())));

        bookingService.findBookingAllItemsByUser(1L, "ALL", 0, 2);

        verify(bookingStorage).findAllByItem_owner_id(anyLong(), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void findBookingAllItemsByUser_shouldReturnBookingDtoOutPageAtPast() {
        when(bookingStorage.findAllByItem_owner_idAndEndBefore(anyLong(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(List.of(Booking.builder().build())));

        bookingService.findBookingAllItemsByUser(1L, "PAST", 0, 2);

        verify(bookingStorage)
                .findAllByItem_owner_idAndEndBefore(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void findBookingAllItemsByUser_shouldReturnBookingDtoOutPageAtFuture() {
        when(bookingStorage.findAllByItem_owner_idAndStartAfter(anyLong(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(List.of(Booking.builder().build())));

        bookingService.findBookingAllItemsByUser(1L, "FUTURE", 0, 2);

        verify(bookingStorage)
                .findAllByItem_owner_idAndStartAfter(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void findBookingAllItemsByUser_shouldReturnBookingDtoOutPageAtCurrent() {
        when(bookingStorage.findAllByItem_owner_idAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class),
                any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(List.of(Booking.builder().build())));

        bookingService.findBookingAllItemsByUser(1L, "CURRENT", 0, 2);

        verify(bookingStorage)
                .findAllByItem_owner_idAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class),
                        any(LocalDateTime.class), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void findBookingAllItemsByUser_shouldReturnBookingDtoOutPageAtWaiting() {
        when(bookingStorage.findAllByItem_owner_idAndStatus(anyLong(), anyString(), any(PageRequest.class)))
                .thenReturn(new PageImpl(List.of(Booking.builder().build())));

        bookingService.findBookingAllItemsByUser(1L, "WAITING", 0, 2);

        verify(bookingStorage).findAllByItem_owner_idAndStatus(anyLong(), anyString(), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void findBookingAllItemsByUser_shouldReturnBookingDtoOutPageAtRejected() {
        when(bookingStorage.findAllByItem_owner_idAndStatus(anyLong(), anyString(), any(PageRequest.class)))
                .thenReturn(new PageImpl(List.of(Booking.builder().build())));

        bookingService.findBookingAllItemsByUser(1L, "REJECTED", 0, 2);

        verify(bookingStorage).findAllByItem_owner_idAndStatus(anyLong(), anyString(), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void findBookingAllItemsByUser_shouldReturnExceptionByPagination() {
        doThrow(ValidationException.class)
                .when(validationItemRequestService).validatePagination(anyInt(), anyInt());

        assertThrows(ValidationException.class,
                () -> bookingService.findBookingByUser(1L, "ALL", 2, 0));
        verify(bookingStorage, never())
                .findAllByItem_owner_id(anyLong(), any(PageRequest.class));
    }
}