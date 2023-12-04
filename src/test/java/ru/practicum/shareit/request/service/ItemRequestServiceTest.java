package ru.practicum.shareit.request.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemForRequestDto;
import ru.practicum.shareit.item.storage.ItemDbStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDbStorage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    @Mock
    private UserDbStorage userStorage;

    @Mock
    private ItemRequestStorage itemRequestStorage;

    @Mock
    private ItemDbStorage itemDbStorage;

    @Mock
    private ValidationItemRequestService validationItemRequestService;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @SneakyThrows
    @Test
    void create_shouldReturnItemRequestDtoOut() {
        User userFromDb = new User();
        ItemRequestDtoIn itemRequestDtoIn = ItemRequestDtoIn.builder().build();
        ItemRequest itemRequestToDb = ItemRequest.builder()
                .id(1L)
                .build();
        ItemForRequestDto itemForRequestDtoExpected = ItemForRequestDto.builder()
                .id(1L)
                .requestId(1L)
                .build();
        ItemRequestDtoOut itemRequestExpected = ItemRequestDtoOut.builder()
                .id(1L)
                .items(List.of(itemForRequestDtoExpected))
                .build();
        when(itemRequestStorage.save(any(ItemRequest.class))).thenReturn(itemRequestToDb);
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(userFromDb));
        List<Item> itemListFromDb = List.of(Item.builder()
                .id(1L)
                .request(ItemRequest.builder()
                        .requester(User.builder()
                                .id(1L)
                                .build())
                        .build())
                .build());
        when(itemDbStorage.findAllByRequestId(anyLong())).thenReturn(itemListFromDb);

        ItemRequestDtoOut itemRequestDtoOutActual = itemRequestService.create(1L, itemRequestDtoIn);

        assertEquals(itemRequestExpected, itemRequestDtoOutActual);
        verify(itemRequestStorage).save(any(ItemRequest.class));
    }

    @SneakyThrows
    @Test
    void create_shouldReturnException() {
        doThrow(ValidationException.class)
                .when(validationItemRequestService).validateBeforeCreate(anyLong());

        assertThrows(ValidationException.class, () -> itemRequestService.create(1L, new ItemRequestDtoIn()));
        verify(itemRequestStorage, never()).save(any(ItemRequest.class));
    }

    @SneakyThrows
    @Test
    void findItemRequestByOwner_shouldReturnItemRequestDtoOutList() {
        List<ItemRequestDtoOut> expectedItemRequestDtoOutList = List.of(ItemRequestDtoOut.builder()
                .id(1L)
                .items(Collections.EMPTY_LIST)
                .build());
        when(itemRequestStorage.findAllByRequesterId(anyLong())).thenReturn(List.of(ItemRequest.builder()
                .id(1L)
                .build()));
        when(itemDbStorage.findAllByRequestId(anyLong())).thenReturn(Collections.EMPTY_LIST);

        List<ItemRequestDtoOut> actualItemRequestDtoOutList = itemRequestService.findItemRequestByOwner(1L);

        assertEquals(expectedItemRequestDtoOutList, actualItemRequestDtoOutList);
        verify(itemRequestStorage).findAllByRequesterId(anyLong());
    }

    @SneakyThrows
    @Test
    void findItemRequestByOwner_shouldReturnException() {
        doThrow(ValidationException.class)
                .when(validationItemRequestService).validateBeforeCreate(anyLong());

        assertThrows(ValidationException.class, () -> itemRequestService.findItemRequestByOwner(1L));
        verify(itemRequestStorage, never()).findAllByRequesterId(anyLong());
    }

    @SneakyThrows
    @Test
    void findItemRequestById_shouldReturnItemRequestDtoOut() {
        ItemRequestDtoOut expectedItemRequestDtoOut = ItemRequestDtoOut.builder()
                .id(1L)
                .items(Collections.EMPTY_LIST)
                .build();
        when(itemRequestStorage.findById(anyLong())).thenReturn(Optional.of(ItemRequest.builder()
                .id(1L)
                .build()));
        when(itemDbStorage.findAllByRequestId(anyLong())).thenReturn(Collections.EMPTY_LIST);

        ItemRequestDtoOut actualItemRequestDtoOut = itemRequestService.findItemRequestById(1L, 1L);

        assertEquals(expectedItemRequestDtoOut, actualItemRequestDtoOut);
        verify(itemRequestStorage).findById(anyLong());
    }

    @SneakyThrows
    @Test
    void findItemRequestById_shouldReturnException() {
        doThrow(ValidationException.class)
                .when(validationItemRequestService).validateBeforeSearchById(anyLong(), anyLong());

        assertThrows(ValidationException.class, () -> itemRequestService.findItemRequestById(1L, 1L));
        verify(itemRequestStorage, never()).findById(anyLong());
    }

    @SneakyThrows
    @Test
    void findAllItemRequest_shouldReturnItemRequestDtoOutList() {
        List<ItemRequestDtoOut> expectedItemRequestDtoOutList = List.of(ItemRequestDtoOut.builder()
                .id(1L)
                .items(Collections.EMPTY_LIST)
                .build());
        when(itemRequestStorage.findAllByRequesterIdNotOrderByCreatedDesc(anyLong()))
                .thenReturn(List.of(ItemRequest.builder()
                        .id(1L)
                        .build()));
        when(itemDbStorage.findAllByRequestId(anyLong())).thenReturn(Collections.EMPTY_LIST);

        List<ItemRequestDtoOut> actualItemRequestDtoOutList = itemRequestService
                .findAllItemRequest(1L, null, null);

        assertEquals(expectedItemRequestDtoOutList, actualItemRequestDtoOutList);
        verify(itemRequestStorage).findAllByRequesterIdNotOrderByCreatedDesc(anyLong());
    }

    @SneakyThrows
    @Test
    void findAllItemRequest_shouldReturnItemRequestDtoOutPage() {
        List<ItemRequestDtoOut> expectedItemRequestDtoOutList = List.of(ItemRequestDtoOut.builder()
                .id(1L)
                .items(Collections.EMPTY_LIST)
                .build());
        when(itemRequestStorage.findAllByRequesterIdNot(anyLong(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(ItemRequest.builder()
                        .id(1L)
                        .build())));
        when(itemDbStorage.findAllByRequestId(anyLong())).thenReturn(Collections.EMPTY_LIST);

        List<ItemRequestDtoOut> actualItemRequestDtoOutList = itemRequestService
                .findAllItemRequest(1L, 0, 2);

        assertEquals(expectedItemRequestDtoOutList, actualItemRequestDtoOutList);
        verify(itemRequestStorage).findAllByRequesterIdNot(anyLong(), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void findAllItemRequest_shouldReturnExceptionById() {
        doThrow(ValidationException.class)
                .when(validationItemRequestService).validateBeforeCreate(anyLong());

        assertThrows(ValidationException.class, () -> itemRequestService
                .findAllItemRequest(1L, 0, 2));

        verify(itemRequestStorage, never()).findAllByRequesterIdNotOrderByCreatedDesc(anyLong());
        verify(itemRequestStorage, never()).findAllByRequesterIdNot(anyLong(), any(PageRequest.class));
    }

    @SneakyThrows
    @Test
    void findAllItemRequest_shouldReturnExceptionByPagination() {
        doThrow(ValidationException.class)
                .when(validationItemRequestService).validatePagination(anyInt(), anyInt());

        assertThrows(ValidationException.class, () -> itemRequestService
                .findAllItemRequest(1L, 2, 0));

        verify(itemRequestStorage, never()).findAllByRequesterIdNotOrderByCreatedDesc(anyLong());
        verify(itemRequestStorage, never()).findAllByRequesterIdNot(anyLong(), any(PageRequest.class));
    }
}