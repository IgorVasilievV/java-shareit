package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDtoOut create(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId,
                                    @RequestBody ItemRequestDtoIn itemRequestDtoIn) {
        return itemRequestService.create(ownerId, itemRequestDtoIn);
    }

    @GetMapping
    public List<ItemRequestDtoOut> findItemRequestByOwner(
            @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId) {
        return itemRequestService.findItemRequestByOwner(ownerId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoOut findItemRequestById(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long userId,
                                                 @PathVariable Long requestId) {
        return itemRequestService.findItemRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoOut> findAllItemRequest(
            @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size) {
        return itemRequestService.findAllItemRequest(ownerId, from, size);
    }
}
