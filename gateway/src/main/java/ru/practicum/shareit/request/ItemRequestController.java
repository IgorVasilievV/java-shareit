package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;

import javax.validation.Valid;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId,
                                         @Valid @RequestBody ItemRequestDtoIn itemRequestDtoIn) {
        return requestClient.create(ownerId, itemRequestDtoIn);
    }

    @GetMapping
    public ResponseEntity<Object> findItemRequestByOwner(
            @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId) {
        return requestClient.findItemRequestByOwner(ownerId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findItemRequestById(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long userId,
                                                 @PathVariable Long requestId) {
        return requestClient.findItemRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllItemRequest(
            @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size) {
        return requestClient.findAllItemRequest(ownerId, from, size);
    }
}
