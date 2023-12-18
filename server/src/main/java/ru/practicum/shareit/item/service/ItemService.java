package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.dto.CommentDtoIn;
import ru.practicum.shareit.item.model.dto.CommentDtoOut;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.ItemWithBookingDto;

import java.util.List;

public interface ItemService {
    ItemDto create(Long ownerId, ItemDto itemDto);

    ItemDto update(Long ownerId, Long id, ItemDto itemDto);

    ItemWithBookingDto getById(Long id, Long userId);

    List<ItemWithBookingDto> getItemsByUser(Long ownerId, Integer from, Integer size);

    List<ItemDto> getItemsBySearch(Long renterId, String text, Integer from, Integer size);

    CommentDtoOut createComment(Long userId, Long itemId, CommentDtoIn commentDtoIn);
}
