package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoOut;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDtoOut create(Long ownerId, ItemRequestDtoIn itemRequestDtoIn);

    List<ItemRequestDtoOut> findItemRequestByOwner(Long ownerId);

    ItemRequestDtoOut findItemRequestById(Long userId, Long requestId);

    List<ItemRequestDtoOut> findAllItemRequest(Long ownerId, Integer from, Integer size);
}
