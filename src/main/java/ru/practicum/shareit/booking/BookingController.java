package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public Booking create(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long userId,
                          @Valid @RequestBody BookingDtoIn bookingDtoIn) {
        return bookingService.create(userId, bookingDtoIn);
    }

    @PatchMapping("/{bookingId}")
    public Booking setStatusBooking(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId,
                                    @PathVariable Long bookingId,
                                    @RequestParam Boolean approved) {
        return bookingService.setStatusBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking findBookingById(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long userId,
                                   @PathVariable Long bookingId) {
        return bookingService.findBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> findBookingByUser(
            @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId,
            @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.findBookingByUser(ownerId, state);
    }

    @GetMapping("/owner")
    public List<Booking> findBookingAllItemsByUser(
            @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "-1L") Long ownerId,
            @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.findBookingAllItemsByUser(ownerId, state);
    }
}
