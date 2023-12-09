package ru.practicum.shareit.item.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingForItemDto;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.CommentDtoIn;
import ru.practicum.shareit.item.model.dto.CommentDtoOut;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.impl.ItemServiceDbImpl;
import ru.practicum.shareit.item.storage.ItemDbStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ValidationItemRequestService;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDbStorage;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemDbStorage itemStorage;

    @Mock
    private UserDbStorage userStorage;

    @Mock
    private BookingStorage bookingStorage;

    @Mock
    private CommentStorage commentStorage;

    @Mock
    private ItemRequestStorage itemRequestStorage;

    @Mock
    private ValidationItemService validationItemService;

    @Mock
    @Qualifier("ValidationItemServiceDbImpl")
    private ValidationItemRequestService validationItemRequestService;

    @InjectMocks
    ItemServiceDbImpl itemService;

    @Captor
    ArgumentCaptor<Item> argumentItemCaptor;

    @SneakyThrows
    @Test
    void create_shouldReturnItemDto() {
        ItemDto itemDtoToSave = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1L)
                .build();
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRequestStorage.findById(anyLong())).thenReturn(Optional.of(new ItemRequest()));
        when(itemStorage.save(any(Item.class))).thenReturn(Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .request(ItemRequest.builder().id(1L).build())
                .build());

        ItemDto itemDtoActual = itemService.create(1L, itemDtoToSave);

        assertEquals(itemDtoToSave, itemDtoActual);
        verify(itemStorage).save(any(Item.class));
    }

    @SneakyThrows
    @Test
    void create_shouldReturnExceptionByItem() {
        doThrow(ValidationException.class)
                .when(validationItemService).validateBeforeCreate(anyLong(), any(ItemDto.class));

        assertThrows(ValidationException.class, () -> itemService.create(1L, ItemDto.builder().build()));
        verify(itemStorage, never()).save(any(Item.class));
    }

    @SneakyThrows
    @Test
    void create_shouldReturnExceptionByRequest() {
        doThrow(ValidationException.class)
                .when(validationItemService).validateItemRequest(anyLong());
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(new User()));

        assertThrows(ValidationException.class, () -> itemService.create(1L, ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1L)
                .build()));
        verify(itemStorage, never()).save(any(Item.class));
        verify(itemRequestStorage, never()).findById(anyLong());
    }

    @SneakyThrows
    @Test
    void update_shouldReturnItemDto() {
        ItemDto itemDtoToUpdate = ItemDto.builder()
                .id(1L)
                .name("nameNew")
                .description("descriptionNew")
                .available(true)
                .requestId(1L)
                .build();
        Item itemOld = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(false)
                .build();
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(itemOld));
        when(itemStorage.save(any(Item.class))).thenReturn(new Item());

        itemService.update(1L, 1L, itemDtoToUpdate);
        verify(itemStorage).save(argumentItemCaptor.capture());
        Item itemActual = argumentItemCaptor.getValue();
        assertEquals(itemDtoToUpdate.getName(), itemActual.getName());
        assertEquals(itemDtoToUpdate.getDescription(), itemActual.getDescription());
        assertEquals(itemDtoToUpdate.getAvailable(), itemActual.getAvailable());
    }

    @SneakyThrows
    @Test
    void update_shouldReturnException() {
        doThrow(ValidationException.class)
                .when(validationItemService).validateBeforeUpdate(anyLong(), anyLong(), any(ItemDto.class));

        assertThrows(ValidationException.class, () -> itemService.update(1L, 1L, ItemDto.builder().build()));
        verify(itemStorage, never()).save(any(Item.class));
    }

    @SneakyThrows
    @Test
    void getById_shouldReturnItemWithBookingDtoAndEmptyComments() {
        Item itemFromDb = Item.builder()
                .id(1L)
                .owner(User.builder().id(1L).build())
                .build();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        Booking bookingFromDb = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .booker(User.builder().id(1L).build())
                .build();
        BookingForItemDto bookingForItemDtoFromDb = BookingForItemDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .bookerId(1L)
                .build();
        ItemWithBookingDto itemWithBookingDtoExpected = ItemWithBookingDto.builder()
                .id(1L)
                .comments(Collections.emptyList())
                .lastBooking(bookingForItemDtoFromDb)
                .nextBooking(bookingForItemDtoFromDb)
                .build();
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(itemFromDb));
        when(bookingStorage.findFirstByItemAndStartBeforeAndStatusOrderByEndDesc(any(Item.class),
                any(LocalDateTime.class), anyString())).thenReturn(bookingFromDb);
        when(bookingStorage.findFirstByItemAndStartAfterAndStatusOrderByStart(any(Item.class),
                any(LocalDateTime.class), anyString())).thenReturn(bookingFromDb);
        when(commentStorage.findByItemId(anyLong())).thenReturn(Collections.EMPTY_LIST);

        ItemWithBookingDto itemWithBookingDtoActual = itemService.getById(1L, 1L);

        assertEquals(itemWithBookingDtoExpected, itemWithBookingDtoActual);
    }

    @SneakyThrows
    @Test
    void getById_shouldReturnItemWithNullBookingDtoAndNotEmptyComments() {
        Item itemFromDb = Item.builder()
                .id(1L)
                .owner(User.builder().id(2L).build())
                .build();
        Comment commentFromDb = Comment.builder()
                .author(User.builder().name("name").build())
                .build();
        ItemWithBookingDto itemWithBookingDtoExpected = ItemWithBookingDto.builder()
                .id(1L)
                .comments(List.of(CommentDtoOut.builder().authorName("name").build()))
                .lastBooking(null)
                .nextBooking(null)
                .build();
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(itemFromDb));
        when(commentStorage.findByItemId(anyLong())).thenReturn(List.of(commentFromDb));

        ItemWithBookingDto itemWithBookingDtoActual = itemService.getById(1L, 1L);

        assertEquals(itemWithBookingDtoExpected, itemWithBookingDtoActual);
    }

    @SneakyThrows
    @Test
    void getById_shouldReturnException() {
        doThrow(ValidationException.class)
                .when(validationItemService).validateSearch(anyLong());

        assertThrows(ValidationException.class, () -> itemService.getById(1L, 1L));
        verify(itemStorage, never()).findById(anyLong());
    }

    @SneakyThrows
    @Test
    void getItemsByUser_shouldReturnItemWithBookingDtoList() {
        List<ItemWithBookingDto> itemWithBookingDtoListExpected = List.of(ItemWithBookingDto.builder()
                .id(1L)
                .comments(Collections.EMPTY_LIST)
                .build());
        when(itemStorage.getItemsByOwnerIdOrderById(anyLong()))
                .thenReturn(List.of(Item.builder().id(1L).build()));

        List<ItemWithBookingDto> itemWithBookingDtoListActual = itemService
                .getItemsByUser(1L, null, null);

        assertEquals(itemWithBookingDtoListExpected, itemWithBookingDtoListActual);
        verify(itemStorage).getItemsByOwnerIdOrderById(anyLong());
    }

    @SneakyThrows
    @Test
    void getItemsByUser_shouldReturnItemWithBookingDtoPage() {
        List<ItemWithBookingDto> itemWithBookingDtoListExpected = List.of(ItemWithBookingDto.builder()
                .id(1L)
                .comments(Collections.EMPTY_LIST)
                .build());
        when(itemStorage.getItemsByOwnerId(anyLong(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(Item.builder().id(1L).build())));

        List<ItemWithBookingDto> itemWithBookingDtoListActual = itemService
                .getItemsByUser(1L, 0, 2);

        assertEquals(itemWithBookingDtoListExpected, itemWithBookingDtoListActual);
        verify(itemStorage).getItemsByOwnerId(anyLong(), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void getItemsByUser_shouldReturnExceptionId() {
        doThrow(ValidationException.class)
                .when(validationItemService).validateSearchByUser(anyLong());

        assertThrows(ValidationException.class, () -> itemService.getItemsByUser(1L, 0, 2));
        verify(itemStorage, never()).getItemsByOwnerIdOrderById(anyLong());
        verify(itemStorage, never()).getItemsByOwnerId(anyLong(), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void getItemsByUser_shouldReturnExceptionPagination() {
        doThrow(ValidationException.class)
                .when(validationItemRequestService).validatePagination(anyInt(), anyInt());

        assertThrows(ValidationException.class, () -> itemService.getItemsByUser(1L, 2, 0));
        verify(itemStorage, never()).getItemsByOwnerIdOrderById(anyLong());
        verify(itemStorage, never()).getItemsByOwnerId(anyLong(), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void getItemsBySearch_shouldReturnItemDtoList() {
        when(itemStorage.searchByText(anyString())).thenReturn(Collections.EMPTY_LIST);
        when(itemStorage.findByIdInOrderById(anyList(), any(Pageable.class))).thenReturn(Page.empty());

        List<ItemDto> itemDtoListActual = itemService.getItemsBySearch(1L, "text", null, null);
        assertEquals(Collections.EMPTY_LIST, itemDtoListActual);
    }

    @SneakyThrows
    @Test
    void getItemsBySearch_shouldReturnItemDtoPage() {
        when(itemStorage.searchByText(anyString())).thenReturn(Collections.EMPTY_LIST);
        when(itemStorage.findByIdInOrderById(anyList(), any(PageRequest.class))).thenReturn(Page.empty());

        List<ItemDto> itemDtoListActual = itemService.getItemsBySearch(1L, "text", 0, 2);
        assertEquals(Collections.EMPTY_LIST, itemDtoListActual);
    }

    @SneakyThrows
    @Test
    void getItemsBySearch_shouldReturnEmptyList() {
        List<ItemDto> itemDtoListActual = itemService.getItemsBySearch(1L, "", null, null);
        assertEquals(Collections.EMPTY_LIST, itemDtoListActual);
    }

    @SneakyThrows
    @Test
    void getItemsBySearch_shouldReturnExceptionId() {
        doThrow(ValidationException.class)
                .when(validationItemService).validateSearchByUser(anyLong());

        assertThrows(ValidationException.class,
                () -> itemService.getItemsBySearch(1L, "text", 0, 2));
        verify(itemStorage, never()).searchByText(anyString());
    }

    @SneakyThrows
    @Test
    void getItemsBySearch_shouldReturnExceptionPagination() {
        doThrow(ValidationException.class)
                .when(validationItemRequestService).validatePagination(anyInt(), anyInt());

        assertThrows(ValidationException.class,
                () -> itemService.getItemsBySearch(1L, "text", 2, 0));
        verify(itemStorage, never()).searchByText(anyString());
    }

    @SneakyThrows
    @Test
    void createComment_shouldReturnCommentDtoOut() {
        User userFromDb = User.builder().build();
        Item itemFromDb = Item.builder().build();
        CommentDtoIn commentDtoIn = CommentDtoIn.builder().build();
        CommentDtoOut commentDtoOutExpected = CommentDtoOut.builder()
                .authorName("name")
                .build();
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(userFromDb));
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(itemFromDb));
        when(commentStorage.save(any(Comment.class))).thenReturn(Comment.builder()
                .author(User.builder()
                        .name("name")
                        .build())
                .build());

        CommentDtoOut commentDtoOutActual = itemService.createComment(1L, 1L, commentDtoIn);

        verify(commentStorage).save(any(Comment.class));
        assertEquals(commentDtoOutExpected, commentDtoOutActual);
    }

    @SneakyThrows
    @Test
    void createComment_shouldReturnException() {
        doThrow(ValidationException.class).when(validationItemService).validateComment(anyLong(), anyLong());

        assertThrows(ValidationException.class,
                () -> itemService.createComment(1L, 1L, CommentDtoIn.builder().build()));
        verify(commentStorage, never()).save(any(Comment.class));
    }
}