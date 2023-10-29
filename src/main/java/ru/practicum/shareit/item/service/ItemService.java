package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(long ownerId, ItemDto itemDto);

    ItemDto update(long ownerId, long id, ItemDto itemDto);

    ItemDto getById(long id);

    List<ItemDto> getItemsByUser(long ownerId);

    List<ItemDto> getItemsBySearch(long renterId, String text);

    void deleteById(long id);
}
