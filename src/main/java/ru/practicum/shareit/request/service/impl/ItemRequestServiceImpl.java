package ru.practicum.shareit.request.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemForRequestDto;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.item.storage.ItemDbStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ValidationItemRequestService;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDbStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserDbStorage userStorage;
    private final ItemRequestStorage itemRequestStorage;
    private final ItemDbStorage itemDbStorage;
    private final ValidationItemRequestService validationItemRequestService;

    @Override
    @Transactional
    public ItemRequestDtoOut create(Long ownerId, ItemRequestDtoIn itemRequestDtoIn) {
        validationItemRequestService.validateBeforeCreate(ownerId);
        User user = userStorage.findById(ownerId).get();
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDtoIn, user);
        ItemRequest itemRequestWithId = itemRequestStorage.save(itemRequest);
        List<ItemForRequestDto> items = ItemMapper.toListItemForRequestDto(itemDbStorage
                .findAllByRequestId(itemRequestWithId.getId()));
        return ItemRequestMapper.toItemRequestDtoOut(itemRequestWithId, items);
    }

    @Override
    public List<ItemRequestDtoOut> findItemRequestByOwner(Long ownerId) {
        validationItemRequestService.validateBeforeCreate(ownerId);
        List<ItemRequest> itemRequests = itemRequestStorage.findAllByRequesterId(ownerId);

        return itemRequests.stream()
                .map(i -> ItemRequestMapper.toItemRequestDtoOut(i, findItems(i.getId()))).collect(Collectors.toList());
    }

    @Override
    public ItemRequestDtoOut findItemRequestById(Long userId, Long requestId) {
        validationItemRequestService.validateBeforeSearchById(userId, requestId);
        ItemRequest itemRequest = itemRequestStorage.findById(requestId).get();
        return ItemRequestMapper.toItemRequestDtoOut(itemRequest, findItems(requestId));
    }

    @Override
    public List<ItemRequestDtoOut> findAllItemRequest(Long ownerId, Integer from, Integer size) {
        validationItemRequestService.validateBeforeCreate(ownerId);
        if (from == null || size == null) {
            List<ItemRequest> itemRequests = itemRequestStorage.findAllByRequesterIdNotOrderByCreatedDesc(ownerId);
            return itemRequests.stream()
                    .map(i -> ItemRequestMapper.toItemRequestDtoOut(i, findItems(i.getId())))
                    .collect(Collectors.toList());
        } else {
            validationItemRequestService.validatePagination(from, size);
            Sort sort = Sort.by(("created")).descending();
            PageRequest pageRequest = PageRequest.of(from / size, size, sort);
            Page<ItemRequest> itemRequestsPage = itemRequestStorage.findAllByRequesterIdNot(ownerId, pageRequest);
            return itemRequestsPage.stream()
                    .map(i -> ItemRequestMapper.toItemRequestDtoOut(i, findItems(i.getId())))
                    .collect(Collectors.toList());
        }
    }

    private List<ItemForRequestDto> findItems(Long requestId) {
        List<Item> items = itemDbStorage.findAllByRequestId(requestId);
        return ItemMapper.toListItemForRequestDto(items);
    }
}
