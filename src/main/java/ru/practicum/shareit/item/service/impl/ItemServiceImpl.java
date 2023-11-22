package ru.practicum.shareit.item.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.dto.CommentDtoIn;
import ru.practicum.shareit.item.model.dto.CommentDtoOut;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.item.service.ValidationItemService;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service("ItemServiceImpl")
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final ValidationItemService validationItemService;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage,
                           @Qualifier("ValidationItemServiceImpl") ValidationItemService validationItemService) {
        this.itemStorage = itemStorage;
        this.validationItemService = validationItemService;
    }

    @Override
    public ItemDto create(Long ownerId, ItemDto itemDto) {
        validationItemService.validateBeforeCreate(ownerId, itemDto);
        Item itemFromMapper = ItemMapper.toItem(itemDto);
        Item item = itemStorage.create(ownerId, itemFromMapper);
        ItemDto itemDtoFromMapper = ItemMapper.toItemDto(item);
        log.info("Created item id = {}", itemDtoFromMapper.getId());
        return itemDtoFromMapper;
    }

    @Override
    public ItemDto update(Long ownerId, Long id, ItemDto itemDto) {
        validationItemService.validateBeforeUpdate(ownerId, id, itemDto);
        Item itemFromMapper = ItemMapper.toItem(itemDto);
        Item item = itemStorage.update(id, itemFromMapper);
        ItemDto itemDtoFromMapper = ItemMapper.toItemDto(item);
        log.info("Updated item id = {}", itemDtoFromMapper.getId());
        return itemDtoFromMapper;
    }

    @Override
    public ItemWithBookingDto getById(Long id, Long userId) {
        validationItemService.validateSearch(id);
        Item item = itemStorage.getById(id);
        ItemWithBookingDto itemDtoFromMapper = ItemMapper.toItemWithBookingDto(item, null, null,
                Collections.emptyList());
        log.info("Found item id = {}", itemDtoFromMapper.getId());
        return itemDtoFromMapper;
    }

    @Override
    public List<ItemWithBookingDto> getItemsByUser(Long ownerId) {
        validationItemService.validateSearchByUser(ownerId);
        log.info("Found all items by userId = {}", ownerId);
        return itemStorage.getItemsByUser(ownerId).stream()
                .map(i -> ItemMapper.toItemWithBookingDto(i, null, null, Collections.emptyList()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsBySearch(Long renterId, String text) {
        validationItemService.validateSearchByUser(renterId);
        log.info("Found items contains text = {} and available", text);
        return itemStorage.getItemsBySearch(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        validationItemService.validateSearch(id);
        itemStorage.deleteById(id);
        log.info("Deleted item id = {}", id);
    }

    @Override
    public CommentDtoOut createComment(Long userId, Long itemId, CommentDtoIn commentDtoIn) {
        return null;
    }
}
