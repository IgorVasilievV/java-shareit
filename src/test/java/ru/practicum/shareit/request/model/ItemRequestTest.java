package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestTest {

    @Test
    void testEquals() {
        ItemRequest itemRequest1 = ItemRequest.builder().id(1L).description("description").build();
        ItemRequest itemRequest2 = ItemRequest.builder().id(1L).description("description1").build();
        ItemRequest itemRequest3 = ItemRequest.builder().id(2L).description("description").build();
        ItemRequest itemRequest4 = null;

        assertTrue(itemRequest1.equals(itemRequest2));
        assertFalse(itemRequest1.equals(itemRequest3));
        assertFalse(itemRequest1.equals(itemRequest4));
    }
}