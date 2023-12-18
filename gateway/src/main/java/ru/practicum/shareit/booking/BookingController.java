package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;

import javax.validation.Valid;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long userId,
                                         @Valid @RequestBody BookingDtoIn bookingDtoIn) {
        return bookingClient.create(userId, bookingDtoIn);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> setStatusBooking(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId,
                                          @PathVariable Long bookingId,
                                          @RequestParam Boolean approved) {
        return bookingClient.setStatusBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBookingById(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long userId,
                                         @PathVariable Long bookingId) {
        return bookingClient.findBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> findBookingByUser(
            @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size) {
        return bookingClient.findBookingByUser(ownerId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findBookingAllItemsByUser(
            @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size) {
        return bookingClient.findBookingAllItemsByUser(ownerId, state, from, size);
    }
}
