package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{id}")
    public ItemDto getById(@PathVariable long id) {
        return itemService.getById(id);
    }

    @PostMapping
    public ItemDto create(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1") long ownerId,
                          @RequestBody ItemDto itemDto) {
        return itemService.create(ownerId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1") long ownerId,
                          @PathVariable long id,
                          @RequestBody ItemDto itemDto) {
        return itemService.update(ownerId, id, itemDto);
    }

    @GetMapping
    public List<ItemDto> getItemsByUser(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1") long ownerId) {
        return itemService.getItemsByUser(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearch(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1") long renterId,
                                          @RequestParam(required = true) String text) {
        return itemService.getItemsBySearch(renterId, text);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        itemService.deleteById(id);
    }
}
