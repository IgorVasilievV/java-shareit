package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.item.service.ValidationItemService;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final ValidationItemService validationItemService;

    @Override
    public ItemDto create(long ownerId, ItemDto itemDto) {
        validationItemService.validateBeforeCreate(ownerId, itemDto);
        Item itemFromMapper = ItemMapper.toItem(itemDto);
        Item item = itemStorage.create(ownerId, itemFromMapper);
        ItemDto itemDtoFromMapper = ItemMapper.toItemDto(item);
        log.info("Created item id = {}", itemDtoFromMapper.getId());
        return itemDtoFromMapper;
    }

    @Override
    public ItemDto update(long ownerId, long id, ItemDto itemDto) {
        validationItemService.validateBeforeUpdate(ownerId, id, itemDto);
        Item itemFromMapper = ItemMapper.toItem(itemDto);
        Item item = itemStorage.update(id, itemFromMapper);
        ItemDto itemDtoFromMapper = ItemMapper.toItemDto(item);
        log.info("Updated item id = {}", itemDtoFromMapper.getId());
        return itemDtoFromMapper;
    }

    @Override
    public ItemDto getById(long id) {
        validationItemService.validateSearch(id);
        Item item = itemStorage.getById(id);
        ItemDto itemDtoFromMapper = ItemMapper.toItemDto(item);
        log.info("Found item id = {}", itemDtoFromMapper.getId());
        return itemDtoFromMapper;
    }

    @Override
    public List<ItemDto> getItemsByUser(long ownerId) {
        validationItemService.validateSearchByUser(ownerId);
        log.info("Found all items by userId = {}", ownerId);
        return itemStorage.getItemsByUser(ownerId).stream()
                .map(i -> ItemMapper.toItemDto(i))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsBySearch(long renterId, String text) {
        validationItemService.validateSearchByUser(renterId);
        log.info("Found items contains text = {} and available", text);
        return itemStorage.getItemsBySearch(text).stream()
                .map(i -> ItemMapper.toItemDto(i))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(long id) {
        validationItemService.validateSearch(id);
        itemStorage.deleteById(id);
        log.info("Deleted item id = {}", id);
    }
}
