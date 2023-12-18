package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @Autowired
    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id,
                                          @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long userId) {
        return itemClient.getById(id, userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId,
                          @Valid @RequestBody ItemDto itemDto) {
        return itemClient.create(ownerId, itemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId,
                          @PathVariable Long id,
                          @RequestBody ItemDto itemDto) {
        return itemClient.update(ownerId, id, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByUser(
            @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size) {
        return itemClient.getItemsByUser(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsBySearch(
            @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long renterId,
            @RequestParam String text,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size) {
        return itemClient.getItemsBySearch(renterId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(
            @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long userId,
            @PathVariable Long itemId,
            @Valid @RequestBody CommentDtoIn commentDtoIn) {
        return itemClient.createComment(userId, itemId, commentDtoIn);
    }
}
