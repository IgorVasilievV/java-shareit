package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.dto.ItemDto;

public interface ValidationItemService {
    void validateBeforeCreate(Long ownerId, ItemDto itemDto);

    void validateBeforeUpdate(Long ownerId, Long id, ItemDto itemDto);

    void validateSearch(Long id);

    void validateSearchByUser(Long ownerId);

    void validateComment(Long userId, Long itemId);

    void validateItemRequest(Long requestId);
}
