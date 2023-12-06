package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

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
}