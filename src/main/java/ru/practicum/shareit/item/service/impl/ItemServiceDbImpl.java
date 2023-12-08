package ru.practicum.shareit.item.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.dto.BookingForItemDto;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.CommentDtoIn;
import ru.practicum.shareit.item.model.dto.CommentDtoOut;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.*;
import ru.practicum.shareit.item.storage.ItemDbStorage;
import ru.practicum.shareit.request.service.ValidationItemRequestService;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDbStorage;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service("ItemServiceDbImpl")
@Transactional(readOnly = true)
@Slf4j
public class ItemServiceDbImpl implements ItemService {

    private final ItemDbStorage itemStorage;
    private final UserDbStorage userStorage;
    private final BookingStorage bookingStorage;
    private final CommentStorage commentStorage;
    private final ItemRequestStorage itemRequestStorage;
    private final ValidationItemService validationItemService;
    private final ValidationItemRequestService validationItemRequestService;

    @Autowired
    public ItemServiceDbImpl(ItemDbStorage itemStorage,
                             UserDbStorage userDbStorage,
                             BookingStorage bookingStorage,
                             CommentStorage commentStorage,
                             ItemRequestStorage itemRequestStorage,
                             @Qualifier("ValidationItemServiceDbImpl") ValidationItemService validationItemService,
                             ValidationItemRequestService validationItemRequestService) {
        this.itemStorage = itemStorage;
        this.userStorage = userDbStorage;
        this.bookingStorage = bookingStorage;
        this.commentStorage = commentStorage;
        this.itemRequestStorage = itemRequestStorage;
        this.validationItemService = validationItemService;
        this.validationItemRequestService = validationItemRequestService;
    }

    @Override
    @Transactional
    public ItemDto create(Long ownerId, ItemDto itemDto) {
        validationItemService.validateBeforeCreate(ownerId, itemDto);

        Item itemFromMapper = ItemMapper.toItem(itemDto);
        itemFromMapper.setOwner(userStorage.findById(ownerId).get());
        if (itemDto.getRequestId() != null) {
            validationItemService.validateItemRequest(itemDto.getRequestId());
            itemFromMapper.setRequest(itemRequestStorage.findById(itemDto.getRequestId()).get());
        }
        Item item = itemStorage.save(itemFromMapper);
        ItemDto itemDtoFromMapper = ItemMapper.toItemDto(item);
        log.info("Created item id = {}", itemDtoFromMapper.getId());
        return itemDtoFromMapper;
    }

    @Override
    @Transactional
    public ItemDto update(Long ownerId, Long id, ItemDto itemDto) {
        validationItemService.validateBeforeUpdate(ownerId, id, itemDto);
        Item itemFromMapper = ItemMapper.toItem(itemDto);
        Item lastItem = itemStorage.findById(id).get();
        if (itemFromMapper.getName() != null) {
            lastItem.setName(itemFromMapper.getName());
        }
        if (itemFromMapper.getDescription() != null) {
            lastItem.setDescription(itemFromMapper.getDescription());
        }
        if (itemFromMapper.getAvailable() != null) {
            lastItem.setAvailable(itemFromMapper.getAvailable());
        }
        Item item = itemStorage.save(lastItem);
        ItemDto itemDtoFromMapper = ItemMapper.toItemDto(item);
        log.info("Updated item id = {}", itemDtoFromMapper.getId());
        return itemDtoFromMapper;
    }

    @Override
    public ItemWithBookingDto getById(Long id, Long userId) {
        validationItemService.validateSearch(id);
        Item item = itemStorage.findById(id).get();

        ItemWithBookingDto itemDtoWithBookingFromMapper;
        if (!item.getOwner().getId().equals(userId)) {
            itemDtoWithBookingFromMapper = ItemMapper
                    .toItemWithBookingDto(item, null, null, getComments(id));
        } else {
            itemDtoWithBookingFromMapper = ItemMapper
                    .toItemWithBookingDto(item, getLastBooking(item), getNextBooking(item), getComments(id));
        }
        log.info("Found item id = {}", itemDtoWithBookingFromMapper.getId());
        return itemDtoWithBookingFromMapper;

    }

    private List<CommentDtoOut> getComments(Long id) {
        List<Comment> comments = commentStorage.findByItemId(id);
        if (comments.isEmpty()) {
            return Collections.emptyList();
        } else {
            return comments.stream().map(CommentMapper::toCommentDtoOut).collect(Collectors.toList());
        }
    }

    @Override
    public List<ItemWithBookingDto> getItemsByUser(Long ownerId, Integer from, Integer size) {
        validationItemService.validateSearchByUser(ownerId);
        log.info("Found all items by userId = {}", ownerId);
        if (from == null || size == null) {
            return itemStorage.getItemsByOwnerIdOrderById(ownerId).stream()
                    .map(i -> ItemMapper.toItemWithBookingDto(i, getLastBooking(i), getNextBooking(i),
                            getComments(i.getId())))
                    .collect(Collectors.toList());
        } else {
            validationItemRequestService.validatePagination(from, size);
            Sort sort = Sort.by(("id")).ascending();
            PageRequest pageRequest = PageRequest.of(from / size, size, sort);
            Page<Item> itemsPage = itemStorage.getItemsByOwnerId(ownerId, pageRequest);
            return itemsPage.stream()
                    .map(i -> ItemMapper.toItemWithBookingDto(i, getLastBooking(i), getNextBooking(i),
                            getComments(i.getId())))
                    .collect(Collectors.toList());
        }
    }

    private BookingForItemDto getLastBooking(Item item) {
        return BookingMapper.toBookingForItemDto(bookingStorage.findFirstByItemAndStartBeforeAndStatusOrderByEndDesc(
                item, LocalDateTime.now(), Status.APPROVED.toString()));
    }

    private BookingForItemDto getNextBooking(Item item) {
        return BookingMapper.toBookingForItemDto(bookingStorage.findFirstByItemAndStartAfterAndStatusOrderByStart(
                item, LocalDateTime.now(), Status.APPROVED.toString()));
    }

    @Override
    public List<ItemDto> getItemsBySearch(Long renterId, String text, Integer from, Integer size) {
        if (text.isBlank()) {
            return Collections.emptyList();
        } else {
            validationItemService.validateSearchByUser(renterId);
            log.info("Found items contains text = {} and available", text);
            Pageable pageable;
            if (from == null || size == null) {
                pageable = Pageable.unpaged();
            } else {
                validationItemRequestService.validatePagination(from, size);
                pageable = PageRequest.of(from / size, size);
            }
            List<Long> ids = itemStorage.searchByText(text);
            Page<Item> items = itemStorage.findByIdInOrderById(ids, pageable);
            return items.getContent().stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public CommentDtoOut createComment(Long userId, Long itemId, CommentDtoIn commentDtoIn) {
        validationItemService.validateComment(userId, itemId);
        User user = userStorage.findById(userId).get();
        Item item = itemStorage.findById(itemId).get();
        Comment comment = CommentMapper.toComment(commentDtoIn, user, item, LocalDateTime.now());
        return CommentMapper.toCommentDtoOut(commentStorage.save(comment));
    }
}
