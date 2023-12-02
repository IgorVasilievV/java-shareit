package ru.practicum.shareit.request.service;

import ru.practicum.shareit.item.model.dto.ItemForRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoOut;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {
    public static ItemRequest toItemRequest(ItemRequestDtoIn itemRequestDtoIn, User user) {
        return ItemRequest.builder()
                .description(itemRequestDtoIn.getDescription())
                .created(LocalDateTime.now())
                .requester(user)
                .build();
    }

    public static ItemRequestDtoOut toItemRequestDtoOut(ItemRequest itemRequest, List<ItemForRequestDto> items) {
       return ItemRequestDtoOut.builder()
               .id(itemRequest.getId())
               .created(itemRequest.getCreated())
               .description(itemRequest.getDescription())
               .items(items)
               .build();
    }

}
