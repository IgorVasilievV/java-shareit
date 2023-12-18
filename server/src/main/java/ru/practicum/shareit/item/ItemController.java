package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.dto.CommentDtoIn;
import ru.practicum.shareit.item.model.dto.CommentDtoOut;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(@Qualifier("ItemServiceDbImpl") ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public ItemWithBookingDto getById(@PathVariable Long id,
                                      @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long userId) {
        return itemService.getById(id, userId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId,
                          @RequestBody ItemDto itemDto) {
        return itemService.create(ownerId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId,
                          @PathVariable Long id,
                          @RequestBody ItemDto itemDto) {
        return itemService.update(ownerId, id, itemDto);
    }

    @GetMapping
    public List<ItemWithBookingDto> getItemsByUser(
            @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size) {
        return itemService.getItemsByUser(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearch(
            @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long renterId,
            @RequestParam String text,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size) {
        return itemService.getItemsBySearch(renterId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut createComment(
            @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long userId,
            @PathVariable Long itemId,
            @RequestBody CommentDtoIn commentDtoIn) {
        return itemService.createComment(userId, itemId, commentDtoIn);
    }
}
