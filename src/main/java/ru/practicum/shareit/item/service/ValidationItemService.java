package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.dto.ItemDto;

public interface ValidationItemService {
    void validateBeforeCreate(long ownerId, ItemDto itemDto);

    void validateBeforeUpdate(long ownerId, long id, ItemDto itemDto);

    void validateSearch(long id);

    void validateSearchByUser(long ownerId);
}
