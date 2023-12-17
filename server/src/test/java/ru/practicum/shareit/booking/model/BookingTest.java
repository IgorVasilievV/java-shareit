package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void testEquals() {
        Booking booking1 = Booking.builder().id(1L).status("status").build();
        Booking booking2 = Booking.builder().id(1L).status("status1").build();
        Booking booking3 = Booking.builder().id(2L).status("status").build();
        Booking booking4 = null;

        assertTrue(booking1.equals(booking2));
        assertFalse(booking1.equals(booking3));
        assertFalse(booking1.equals(booking4));
    }

    @Test
    void testToString() {
        Booking booking = Booking.builder().id(1L).booker(User.builder().id(2L).build()).status("status")
                .start(LocalDateTime.parse("2023-12-08T15:43:12"))
                .end(LocalDateTime.parse("2023-12-09T15:43:12"))
                .build();

        assertEquals("Booking{id=1, start=2023-12-08T15:43:12, end=2023-12-09T15:43:12, status='status'}",
                booking.toString());
    }
}